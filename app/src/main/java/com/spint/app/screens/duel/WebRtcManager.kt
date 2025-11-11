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

    private var videoCapturer: CameraVideoCapturer? = null
    private var videoSource: VideoSource? = null
    private var localVideoTrack: VideoTrack? = null
    private var localAudioTrack: AudioTrack? = null
    private var remoteVideoTrack: VideoTrack? = null
    private var audioSource: AudioSource? = null
    private var partnerId: String? = null

    // inside class
    private val pendingRemoteCandidates = mutableListOf<IceCandidate>()
    private var isRemoteDescriptionSet = false


    /** Initialize factory + local tracks */
    fun initFactoryAndLocalTracks() {
        initPeerConnectionFactory()
        initAudio()
        initVideoCapturer()
    }

    /** Attach local video to renderer */
    fun setupLocalVideo() {
        localVideoTrack?.addSink(localRenderer)
    }

    /** Start local camera preview */
    fun startLocalPreview() {
        videoCapturer?.startCapture(1024, 720, 30)
    }

    /** Release resources */
    fun release() {
        try {
            videoCapturer?.stopCapture()
        } catch (_: Exception) {}
        videoCapturer?.dispose()
        videoSource?.dispose()
        audioSource?.dispose()
        peerConnection?.close()
        if (::socket.isInitialized) {
            socket.disconnect()
            socket.close()
        }
    }

    /** Socket signaling */
    fun setupSocket() {
        socket = IO.socket("http://${Constants.IP_ADD}:5002")
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) { Log.d(tag, "Connected to server") }

        socket.on("matched") { args ->
            val data = args[0] as JSONObject
            partnerId = data.getString("partnerId")
            val shouldOffer = data.optBoolean("shouldOffer", false)

            runOnMainThread {
                createPeerConnection()
                if (shouldOffer) {
                    createAndSendOffer()
                }
            }
        }

//        socket.on("offer") { args ->
//            val data = args[0] as JSONObject
//            val offer = data.getJSONObject("offer")
//            val sdp = offer.getString("sdp")
//            val type = offer.getString("type")
//            runOnMainThread {
//                if (peerConnection == null) createPeerConnection()
//                peerConnection?.setRemoteDescription(
//                    SdpObserverAdapter(),
//                    SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp)
//                )
//                createAndSendAnswer()
//            }
//        }
//        socket.on("offer") { args ->
//            val data = args[0] as JSONObject
//            val offer = data.getJSONObject("offer")
//            val sdp = offer.getString("sdp")
//            val type = offer.getString("type")
//
//            runOnMainThread {
//                if (peerConnection == null) {
//                    createPeerConnection()
//                    // ✅ Make sure local tracks are attached
//                    localVideoTrack?.let { peerConnection?.addTrack(it) }
//                    localAudioTrack?.let { peerConnection?.addTrack(it) }
//                }
//
//                val remoteDesc = SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp)
//                peerConnection?.setRemoteDescription(SdpObserverAdapter(), remoteDesc)
//
//                // ✅ Wait until remote description is set before answering
//                createAndSendAnswer()
//            }
//        }
        socket.on("offer") { args ->
            val data = args[0] as JSONObject
            val offer = data.getJSONObject("offer")
            val sdp = offer.getString("sdp")
            val type = offer.getString("type")

            runOnMainThread {
                if (peerConnection == null) {
                    createPeerConnection()
                    // ensure local tracks are attached
                    localVideoTrack?.let { peerConnection?.addTrack(it) }
                    localAudioTrack?.let { peerConnection?.addTrack(it) }
                }

                // reset candidate buffer and flag for this negotiation
                pendingRemoteCandidates.clear()
                isRemoteDescriptionSet = false

                val remoteDesc = SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp)
                // Use a SdpObserverAdapter which calls createAndSendAnswer() only onSetSuccess
                peerConnection?.setRemoteDescription(object : SdpObserverAdapter() {
                    override fun onSetSuccess() {
                        super.onSetSuccess()
                        Log.d(tag, "Remote description set successfully — creating answer")
                        isRemoteDescriptionSet = true
                        // flush any queued remote candidates
                        pendingRemoteCandidates.forEach { candidate ->
                            Log.d(tag, "Flushing queued remote candidate: $candidate")
                            peerConnection?.addIceCandidate(candidate)
                        }
                        pendingRemoteCandidates.clear()

                        // Now create and send the answer
                        createAndSendAnswer()
                    }

                    override fun onSetFailure(error: String) {
                        super.onSetFailure(error)
                        Log.e(tag, "Failed to set remote description: $error")
                    }
                }, remoteDesc)
            }
        }



        socket.on("answer") { args ->
            val data = args[0] as JSONObject
            val answer = data.getJSONObject("answer")
            val sdp = answer.getString("sdp")
            val type = answer.getString("type")
            runOnMainThread {
                peerConnection?.setRemoteDescription(
                    SdpObserverAdapter(),
                    SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp)
                )
            }
        }

        socket.on("ice-candidate") { args ->
            val data = args[0] as JSONObject
            val candidate = data.getJSONObject("candidate")
            val sdpMid = candidate.getString("sdpMid")
            val sdpMLineIndex = candidate.getInt("sdpMLineIndex")
            val candidateStr = candidate.getString("candidate")
            runOnMainThread {
                peerConnection?.addIceCandidate(IceCandidate(sdpMid, sdpMLineIndex, candidateStr))
            }
        }
    }

    /** --- Private helpers --- */

    private fun initPeerConnectionFactory() {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .createInitializationOptions()
        )

        val options = PeerConnectionFactory.Options()
        val encoderFactory = DefaultVideoEncoderFactory(eglBase.eglBaseContext, true, true)
        val decoderFactory = DefaultVideoDecoderFactory(eglBase.eglBaseContext)

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .createPeerConnectionFactory()
    }

    private fun initAudio() {
        val audioConstraints = MediaConstraints()
        audioSource = peerConnectionFactory.createAudioSource(audioConstraints)
        localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource!!)
    }

    private fun initVideoCapturer() {
        val enumerator = Camera2Enumerator(context)
        val cameraName = enumerator.deviceNames.firstOrNull { enumerator.isFrontFacing(it) }
        if (cameraName == null) {
            Log.e(tag, "No front camera found")
            return
        }
        videoCapturer = enumerator.createCapturer(cameraName, null)
        val surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)
        videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer!!.initialize(surfaceTextureHelper, context, videoSource!!.capturerObserver)
        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource!!)
        localVideoTrack?.addSink(localRenderer)
    }

    private fun createPeerConnection() {
        // Add TURN server along with STUN
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("turn:YOUR_TURN_SERVER_IP:3478")
                .setUsername("username")
                .setPassword("password")
                .createIceServer()
        )

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }

        peerConnection?.close()
//        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
//            override fun onIceCandidate(candidate: IceCandidate) {
//                Log.d(tag, "New ICE candidate: ${candidate.sdp}")
//                partnerId?.let {
//                    val json = JSONObject().apply {
//                        put("partnerId", it)
//                        put("candidate", JSONObject().apply {
//                            put("sdpMid", candidate.sdpMid)
//                            put("sdpMLineIndex", candidate.sdpMLineIndex)
//                            put("candidate", candidate.sdp)
//                        })
//                    }
//                    socket.emit("ice-candidate", json)
//                }
//            }
//
//            override fun onTrack(transceiver: RtpTransceiver?) {
//                val track = transceiver?.receiver?.track()
//                if (track is VideoTrack) {
//                    remoteVideoTrack = track
//                    runOnMainThread { remoteVideoTrack?.addSink(remoteRenderer) }
//                }
//            }
//
//            override fun onSignalingChange(state: PeerConnection.SignalingState?) {}
//            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
//                Log.d(tag, "ICE connection state changed: $state")
//            }
//            override fun onConnectionChange(state: PeerConnection.PeerConnectionState?) {
//                Log.d(tag, "PeerConnection state changed: $state")
//            }
//            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {}
//            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
//            override fun onDataChannel(dc: DataChannel?) {}
//            override fun onAddStream(stream: MediaStream?) {
//                Log.d(tag, "onAddStream: tracks=${stream?.videoTracks?.size}")
//                stream?.videoTracks?.firstOrNull()?.let { track ->
//                    remoteVideoTrack = track
//                    runOnMainThread { remoteVideoTrack?.addSink(remoteRenderer) }
//                }
//            }
//            override fun onRemoveStream(p0: MediaStream?) {}
//            override fun onRenegotiationNeeded() {}
//            override fun onIceConnectionReceivingChange(p0: Boolean) {}
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate) {
                Log.d(tag, "New ICE candidate: $candidate") // <-- full candidate info
                partnerId?.let {
                    val json = JSONObject().apply {
                        put("partnerId", it)
                        put("candidate", JSONObject().apply {
                            put("sdpMid", candidate.sdpMid)
                            put("sdpMLineIndex", candidate.sdpMLineIndex)
                            put("candidate", candidate.sdp)
                        })
                    }
                    socket.emit("ice-candidate", json)
                }
            }

            override fun onTrack(transceiver: RtpTransceiver?) {
                val track = transceiver?.receiver?.track()
                if (track is VideoTrack) {
                    remoteVideoTrack = track
                    runOnMainThread { remoteVideoTrack?.addSink(remoteRenderer) }
                }
            }

            override fun onSignalingChange(state: PeerConnection.SignalingState?) {}
            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                Log.d(tag, "ICE connection state changed: $state")
            }
            override fun onConnectionChange(state: PeerConnection.PeerConnectionState?) {
                Log.d(tag, "PeerConnection state changed: $state")
            }
            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {}
            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
            override fun onDataChannel(dc: DataChannel?) {}
            override fun onAddStream(stream: MediaStream?) {
                Log.d(tag, "onAddStream: tracks=${stream?.videoTracks?.size}")
                stream?.videoTracks?.firstOrNull()?.let { track ->
                    remoteVideoTrack = track
                    runOnMainThread { remoteVideoTrack?.addSink(remoteRenderer) }
                }
            }
            override fun onRemoveStream(p0: MediaStream?) {}
            override fun onRenegotiationNeeded() {}
            override fun onIceConnectionReceivingChange(p0: Boolean) {}
    })

        // Add local tracks after PeerConnection is created
        localVideoTrack?.let { peerConnection?.addTrack(it) }
        localAudioTrack?.let { peerConnection?.addTrack(it) }
    }


    private fun createAndSendOffer() {
        val constraints = MediaConstraints()
        peerConnection?.createOffer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                partnerId?.let { pid ->
                    val json = JSONObject().apply {
                        put("partnerId", pid)
                        put("offer", JSONObject().apply {
                            put("type", desc.type.canonicalForm())
                            put("sdp", desc.description)
                        })
                    }
                    socket.emit("offer", json)
                }
            }
        }, constraints)
    }

    private fun createAndSendAnswer() {
        val constraints = MediaConstraints()
        peerConnection?.createAnswer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                partnerId?.let { pid ->
                    val json = JSONObject().apply {
                        put("partnerId", pid)
                        put("answer", JSONObject().apply {
                            put("type", desc.type.canonicalForm())
                            put("sdp", desc.description)
                        })
                    }
                    socket.emit("answer", json)
                }
            }
        }, constraints)
    }
    private fun createIceServers(): List<PeerConnection.IceServer> {
        return listOf(
            // STUN server (existing)
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),

            // TURN server (add your own)
            PeerConnection.IceServer.builder("turn:YOUR_TURN_SERVER_IP:3478")
                .setUsername("TURN_USERNAME")
                .setPassword("TURN_PASSWORD")
                .createIceServer()
        )
    }


    private fun runOnMainThread(action: () -> Unit) {
        Handler(context.mainLooper).post { action() }
    }
}

open class SdpObserverAdapter : SdpObserver {
    private val tag = "WEBRTC_SDP"

    override fun onCreateSuccess(sessionDescription: SessionDescription) {
        Log.d(tag, "onCreateSuccess: ${sessionDescription.type}")
    }

    override fun onSetSuccess() { Log.d(tag, "onSetSuccess") }
    override fun onCreateFailure(error: String) { Log.e(tag, "onCreateFailure: $error") }
    override fun onSetFailure(error: String) { Log.e(tag, "onSetFailure: $error") }
}
