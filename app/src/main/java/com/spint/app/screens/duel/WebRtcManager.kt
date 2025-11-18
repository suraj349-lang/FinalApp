package com.spint.app.screens.duel

import android.content.Context
import android.os.Handler
import android.util.Log
import com.spint.app.utils.constants.Constants
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.webrtc.*

class WebRTCManager(
    private val context: Context,
    private val localRenderer: SurfaceViewRenderer,
    private val remoteRenderer: SurfaceViewRenderer,
    private val eglBase: EglBase
) {
    private val tag = "WEBRTC_MANAGER"

    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private var peerConnection: PeerConnection? = null
    private lateinit var socket: Socket

    // optional callback so UI can react to partner disconnect
    var onPartnerDisconnected: (() -> Unit)? = null

    private var videoCapturer: CameraVideoCapturer? = null
    private var videoSource: VideoSource? = null
    private var localVideoTrack: VideoTrack? = null
    private var localAudioTrack: AudioTrack? = null
    private var remoteVideoTrack: VideoTrack? = null
    private var audioSource: AudioSource? = null
    private var partnerId: String? = null

    private val pendingRemoteCandidates = mutableListOf<IceCandidate>()
    private var isRemoteDescriptionSet = false

    /** Initialize factory + local tracks */
    fun initFactoryAndLocalTracks() {
        initPeerConnectionFactory()
        initAudio()
        initVideoCapturer()
    }

    fun setupLocalVideo() {
        localVideoTrack?.addSink(localRenderer)
    }

    fun startLocalPreview() {
        try {
            videoCapturer?.startCapture(1024, 720, 30)
        } catch (e: Exception) {
            Log.e(tag, "startLocalPreview failed: ${e.message}")
        }
    }

    fun release() {
        try { videoCapturer?.stopCapture() } catch (_: Exception) {}

        try { peerConnection?.close() } catch (_: Exception) {}
        peerConnection = null
        partnerId = null


        videoCapturer?.dispose()
        videoSource?.dispose()
        audioSource?.dispose()
        pendingRemoteCandidates.clear()
        isRemoteDescriptionSet = false

        if (::socket.isInitialized) {
            try {
                socket.disconnect()
                socket.close()
            } catch (e: Exception) {
                Log.w(tag, "Socket disconnect error: ${e.message}")
            }
        }
    }

    /** Setup socket */
    fun setupSocket() {
        // prevent double initialization
        if (::socket.isInitialized && socket.connected()) {
            Log.d(tag, "Socket already connected, ignoring setupSocket()")
            return
        }

        try {
            val opts = IO.Options().apply {
            path = "/signaling/socket.io/"   // FIX
            reconnection = true
            }

            socket = IO.socket("http://3.108.55.84", opts)


            socket.connect()
        } catch (e: Exception) {
            Log.e(tag, "Socket connect error: ${e.message}")
        }

        socket.on(Socket.EVENT_CONNECT) {
            Log.d(tag, "Connected to signaling server")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d(tag, "Socket disconnected")
        }

        socket.on("matched") { args ->
            val data = args[0] as JSONObject
            partnerId = data.getString("partnerId")
            val shouldOffer = data.optBoolean("shouldOffer", false)

            runOnMainThread {
                createPeerConnection()
                if (shouldOffer) createAndSendOffer()
            }
        }

        /** Receive OFFER */
        socket.on("offer") { args ->
            val data = args[0] as JSONObject
            val offer = data.getJSONObject("offer")

            val remoteDesc = SessionDescription(
                SessionDescription.Type.fromCanonicalForm(offer.getString("type")),
                offer.getString("sdp")
            )

            runOnMainThread {
                if (peerConnection == null) createPeerConnection()

                // reset buffer/flag for this negotiation
                pendingRemoteCandidates.clear()
                isRemoteDescriptionSet = false

                peerConnection?.setRemoteDescription(object : SdpObserverAdapter() {
                    override fun onSetSuccess() {
                        Log.d(tag, "Remote description (offer) set OK")
                        isRemoteDescriptionSet = true

                        // flush queued remote candidates
                        pendingRemoteCandidates.forEach { candidate ->
                            Log.d(tag, "Flushing queued remote candidate: $candidate")
                            peerConnection?.addIceCandidate(candidate)
                        }
                        pendingRemoteCandidates.clear()

                        createAndSendAnswer()
                    }

                    override fun onSetFailure(error: String) {
                        super.onSetFailure(error)
                        Log.e(tag, "Failed to set remote description (offer): $error")
                    }
                }, remoteDesc)
            }
        }

        /** Receive ANSWER */
        socket.on("answer") { args ->
            val data = args[0] as JSONObject
            val answer = data.getJSONObject("answer")

            val remoteDesc = SessionDescription(
                SessionDescription.Type.fromCanonicalForm(answer.getString("type")),
                answer.getString("sdp")
            )

            runOnMainThread {
                // when we receive answer, we consider remote description set for this negotiation
                peerConnection?.setRemoteDescription(object : SdpObserverAdapter() {
                    override fun onSetSuccess() {
                        Log.d(tag, "Remote description (answer) set OK")
                        isRemoteDescriptionSet = true

                        // flush any buffered candidates
                        pendingRemoteCandidates.forEach { candidate ->
                            Log.d(tag, "Flushing queued remote candidate after answer: $candidate")
                            peerConnection?.addIceCandidate(candidate)
                        }
                        pendingRemoteCandidates.clear()
                    }

                    override fun onSetFailure(error: String) {
                        super.onSetFailure(error)
                        Log.e(tag, "Failed to set remote description (answer): $error")
                    }
                }, remoteDesc)
            }
        }

        /** Receive ICE CANDIDATE */
        socket.on("ice-candidate") { args ->
            val data = args[0] as JSONObject
            val c = data.getJSONObject("candidate")

            val candidate = IceCandidate(
                c.getString("sdpMid"),
                c.getInt("sdpMLineIndex"),
                c.getString("candidate")
            )

            runOnMainThread {
                if (!isRemoteDescriptionSet) {
                    Log.d(tag, "Remote SDP not set → buffering ICE candidate")
                    pendingRemoteCandidates.add(candidate)
                } else {
                    Log.d(tag, "Applying ICE candidate now: $candidate")
                    peerConnection?.addIceCandidate(candidate)
                }
            }
        }

        // partner disconnected event from server
        socket.on("partner-disconnected") {
            Log.d(tag, "Partner disconnected event received")
            runOnMainThread {
                // cleanup our connection
                try {
                    peerConnection?.close()
                } catch (_: Exception) {}
                peerConnection = null
                pendingRemoteCandidates.clear()
                isRemoteDescriptionSet = false
                partnerId = null

                // notify UI
                onPartnerDisconnected?.invoke()
            }
        }
    }

    /** Helpers */

    private fun initPeerConnectionFactory() {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .createInitializationOptions()
        )

        val encoderFactory = DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true)
        val decoderFactory = DefaultVideoDecoderFactory(eglBase.eglBaseContext)

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .createPeerConnectionFactory()
    }

    private fun initAudio() {
        audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource!!)
    }

    private fun initVideoCapturer() {
        val enumerator = Camera2Enumerator(context)
        val cameraName = enumerator.deviceNames.firstOrNull { enumerator.isFrontFacing(it) }
            ?: return

        videoCapturer = enumerator.createCapturer(cameraName, null)
        val helper = SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)

        videoSource = peerConnectionFactory.createVideoSource(false)
        videoCapturer!!.initialize(helper, context, videoSource!!.capturerObserver)

        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource!!)
        localVideoTrack?.addSink(localRenderer)
    }

    private fun createPeerConnection() {
        // Close existing and reset state
        try { peerConnection?.close() } catch (_: Exception) {}
        peerConnection = null
        pendingRemoteCandidates.clear()
        isRemoteDescriptionSet = false

        /** STUN ONLY */
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )

        val config = PeerConnection.RTCConfiguration(iceServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }

        peerConnection = peerConnectionFactory.createPeerConnection(config,
            object : PeerConnection.Observer {
                override fun onIceCandidate(candidate: IceCandidate) {
                    Log.d(tag, "New ICE candidate generated: $candidate")
                    partnerId?.let {
                        val json = JSONObject().apply {
                            put("partnerId", it)
                            put("candidate", JSONObject().apply {
                                put("sdpMid", candidate.sdpMid)
                                put("sdpMLineIndex", candidate.sdpMLineIndex)
                                put("candidate", candidate.sdp)
                            })
                        }
                        try {
                            socket.emit("ice-candidate", json)
                        } catch (e: Exception) {
                            Log.w(tag, "Failed to emit ice-candidate: ${e.message}")
                        }
                    }
                }

                override fun onTrack(transceiver: RtpTransceiver?) {
                    (transceiver?.receiver?.track() as? VideoTrack)?.let { videoTrack ->
                        remoteVideoTrack = videoTrack
                        runOnMainThread { remoteVideoTrack?.addSink(remoteRenderer) }
                    }
                }

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                    Log.d(tag, "ICE → $state")
                }

                override fun onConnectionChange(state: PeerConnection.PeerConnectionState?) {
                    Log.d(tag, "PC State → $state")
                }

                override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
                override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
                override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
                override fun onDataChannel(p0: DataChannel?) {}
                override fun onAddStream(p0: MediaStream?) {}
                override fun onRemoveStream(p0: MediaStream?) {}
                override fun onRenegotiationNeeded() {}
                override fun onIceConnectionReceivingChange(p0: Boolean) {}
            })

        // Add local tracks after PeerConnection is created
        enableSpeakerphone()
        localVideoTrack?.let { peerConnection?.addTrack(it) }
        localAudioTrack?.let { peerConnection?.addTrack(it) }
    }

    private fun createAndSendOffer() {
        peerConnection?.createOffer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)

                partnerId?.let {
                    val json = JSONObject().apply {
                        put("partnerId", it)
                        put("offer", JSONObject().apply {
                            put("type", desc.type.canonicalForm())
                            put("sdp", desc.description)
                        })
                    }
                    try {
                        socket.emit("offer", json)
                    } catch (e: Exception) {
                        Log.w(tag, "Failed to emit offer: ${e.message}")
                    }
                }
            }
        }, MediaConstraints())
    }

    private fun createAndSendAnswer() {
        peerConnection?.createAnswer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)

                partnerId?.let {
                    val json = JSONObject().apply {
                        put("partnerId", it)
                        put("answer", JSONObject().apply {
                            put("type", desc.type.canonicalForm())
                            put("sdp", desc.description)
                        })
                    }
                    try {
                        socket.emit("answer", json)
                    } catch (e: Exception) {
                        Log.w(tag, "Failed to emit answer: ${e.message}")
                    }
                }
            }
        }, MediaConstraints())
    }

    private fun runOnMainThread(action: () -> Unit) {
        Handler(context.mainLooper).post { action() }
    }
    private fun enableSpeakerphone() {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
        audioManager.mode = android.media.AudioManager.MODE_IN_COMMUNICATION
        audioManager.isSpeakerphoneOn = true
    }
}

open class SdpObserverAdapter : SdpObserver {
    private val tag = "WEBRTC_SDP"

    override fun onCreateSuccess(sdp: SessionDescription) {
        Log.d(tag, "onCreateSuccess: ${sdp.type}")
    }

    override fun onSetSuccess() {
        Log.d(tag, "onSetSuccess")
    }

    override fun onCreateFailure(error: String) {
        Log.e(tag, "onCreateFailure: $error")
    }

    override fun onSetFailure(error: String) {
        Log.e(tag, "onSetFailure: $error")
    }



}
