package com.example.finalapp.screens.webrtc

import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.finalapp.utils.constants.Constants
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
    private val tag = "OMEGLE"

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

    fun init() {
        Log.d(tag, "Initializing WebRTCManager")

        // NOTE: Initialize renderers only from one place. If your Composable already
        // initializes the renderers, remove the init calls here to avoid "Already initialized".
        try {
            // If you are managing renderer lifecycle in Compose, comment the two lines below.
            remoteRenderer.init(eglBase.eglBaseContext, null)
            remoteRenderer.setMirror(false)
        } catch (e: Exception) {
            Log.w(tag, "remoteRenderer.init() skipped or failed: ${e.localizedMessage}")
        }

        // PeerConnectionFactory + local sources
        initPeerConnectionFactory()
        initAudio()
        // We create the capturer and local video track in initVideoCapturer or setupLocalVideo.
        initVideoCapturer()

        // Socket connects and signaling handlers
        setupSocket()
    }

    /**
     * Call when permissions are granted to bind preview immediately
     */
    fun setupLocalVideo() {
        Log.d(tag, "Setting up local video")

        // If we already created video source/track in initVideoCapturer, this may be redundant.
        if (videoCapturer == null) {
            videoCapturer = createCameraCapturer()
        }

        if (videoCapturer == null) {
            Log.e(tag, "No front camera found")
            return
        }

        val surfaceTextureHelper =
            SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)

        videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer!!.initialize(surfaceTextureHelper, context, videoSource!!.capturerObserver)

        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource!!)
        // Ensure local renderer initialized by whichever side owns it
        try {
            localVideoTrack?.addSink(localRenderer)
        } catch (e: Exception) {
            Log.w(tag, "Failed to add sink to localRenderer: ${e.localizedMessage}")
        }

        Log.d(tag, "Local video track created and sink added (setupLocalVideo)")
    }

    fun startLocalPreview() {
        Log.d(tag, "Starting local preview")
        videoCapturer?.let {
            try {
                it.startCapture(1024, 720, 30)
            } catch (e: Exception) {
                Log.e(tag, "Error starting camera capture: ${e.localizedMessage}", e)
            }
        } ?: Log.e(tag, "VideoCapturer is null")
    }

    fun release() {
        Log.d(tag, "Releasing WebRTCManager resources")
        try {
            videoCapturer?.stopCapture()
            videoCapturer?.dispose()
            videoCapturer = null

            videoSource?.dispose()
            videoSource = null

            audioSource?.dispose()
            audioSource = null

            peerConnection?.close()
            peerConnection = null

            if (::socket.isInitialized) {
                socket.disconnect()
                socket.close()
            }
        } catch (e: Exception) {
            Log.e(tag, "Error during release: ${e.localizedMessage}", e)
        }
    }

    private fun initAudio() {
        val audioConstraints = MediaConstraints()
        audioSource = peerConnectionFactory.createAudioSource(audioConstraints)
        localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource!!)
    }

    private fun initPeerConnectionFactory() {
        Log.d(tag, "Initializing PeerConnectionFactory")

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

        Log.d(tag, "PeerConnectionFactory created")
    }

    private fun initVideoCapturer() {
        Log.d(tag, "Initializing VideoCapturer")
        // If the capturer is already created by setupLocalVideo, this will be skipped.
        if (videoCapturer == null) {
            videoCapturer = createCameraCapturer()
        }
        if (videoCapturer == null) {
            Log.e(tag, "No front camera found")
            return
        }

        val surfaceTextureHelper =
            SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)

        videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer!!.initialize(surfaceTextureHelper, context, videoSource!!.capturerObserver)

        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource!!)
        try {
            localVideoTrack?.addSink(localRenderer)
        } catch (e: Exception) {
            Log.w(tag, "Failed to add sink to localRenderer in initVideoCapturer: ${e.localizedMessage}")
        }

        Log.d(tag, "Local video track created and sink added (initVideoCapturer)")
    }

    private fun createCameraCapturer(): CameraVideoCapturer? {
        val enumerator = Camera2Enumerator(context)
        for (deviceName in enumerator.deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Log.d(tag, "Front camera found: $deviceName")
                return enumerator.createCapturer(deviceName, null)
            }
        }
        Log.e(tag, "No front camera found on device")
        return null
    }

    fun setupSocket() {
        try {
            socket = IO.socket("http://${Constants.IP_ADD}:5002")
            socket.connect()

            socket.on(Socket.EVENT_CONNECT) {
                Log.d(tag, "Connected to signaling server")
            }

            socket.on("matched") { args ->
                Log.d(tag, "Matched with partner")
                val partnerJson = args[0] as JSONObject
                partnerId = partnerJson.getString("partnerId")

                runOnMainThread {
                    // Ensure local tracks are prepared before creating the peer connection
                    if (localVideoTrack == null || localAudioTrack == null) {
                        Log.w(tag, "Local tracks not ready yet; ensure setupLocalVideo() called before connecting")
                    }
                    createPeerConnection()
                    createAndSendOffer()
                }
            }

            socket.on("offer") { args ->
                val data = args[0] as JSONObject
                val offer = data.getJSONObject("offer")
                val sdp = offer.getString("sdp")
                val type = offer.getString("type")

                runOnMainThread {
                    // Don't overwrite partnerId here; matched should set partnerId
                    if (peerConnection == null) createPeerConnection()
                    val sessionDescription = SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(type), sdp
                    )
                    try {
                        peerConnection?.setRemoteDescription(SdpObserverAdapter(), sessionDescription)
                    } catch (e: Exception) {
                        Log.e(tag, "setRemoteDescription error (offer): ${e.localizedMessage}", e)
                    }
                    createAndSendAnswer()
                }
            }

            socket.on("answer") { args ->
                val data = args[0] as JSONObject
                val answer = data.getJSONObject("answer")
                val sdp = answer.getString("sdp")
                val type = answer.getString("type")

                runOnMainThread {
                    val sessionDescription = SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(type),
                        sdp
                    )
                    try {
                        peerConnection?.setRemoteDescription(SdpObserverAdapter(), sessionDescription)
                    } catch (e: Exception) {
                        Log.e(tag, "setRemoteDescription error (answer): ${e.localizedMessage}", e)
                    }
                }
            }

            socket.on("ice-candidate") { args ->
                val data = args[0] as JSONObject
                val candidate = data.getJSONObject("candidate")
                val sdpMid = candidate.getString("sdpMid")
                val sdpMLineIndex = candidate.getInt("sdpMLineIndex")
                val candidateStr = candidate.getString("candidate")

                runOnMainThread {
                    try {
                        peerConnection?.addIceCandidate(
                            IceCandidate(sdpMid, sdpMLineIndex, candidateStr)
                        )
                        Log.d(tag, "Added remote ICE candidate")
                    } catch (e: Exception) {
                        Log.e(tag, "Failed to add remote ICE candidate: ${e.localizedMessage}", e)
                    }
                }
            }

            socket.on("partner-disconnected") {
                Log.d(tag, "Partner disconnected")
                // you can update UI / clear remote renderer here
            }

        } catch (e: Exception) {
            Log.e(tag, "Socket error: ${e.localizedMessage}", e)
        }
    }

    private fun createPeerConnection() {
        Log.d(tag, "Creating PeerConnection")

        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }

        // Close any old PeerConnection
        peerConnection?.close()

        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate) {
                Log.d(tag, "onIceCandidate: $candidate")
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
                } ?: Log.w(tag, "onIceCandidate: partnerId is null, not sending")
            }

            override fun onTrack(transceiver: RtpTransceiver?) {
                transceiver?.receiver?.track()?.let { track ->
                    if (track is VideoTrack) {
                        remoteVideoTrack = track
                        runOnMainThread {
                            remoteVideoTrack?.addSink(remoteRenderer)
                            Log.d(tag, "✅ Remote video track added to renderer")
                        }
                    } else {
                        Log.d(tag, "onTrack: non-video track = ${track?.kind()}")
                    }
                }
            }

            override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
                Log.d(tag, "onConnectionChange: $newState")
            }

            override fun onIceConnectionReceivingChange(p0: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onSignalingChange(state: PeerConnection.SignalingState?) {
                Log.d(tag, "onSignalingChange: $state")
            }

            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                Log.d(tag, "onIceConnectionChange: $state")
            }

            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {
                Log.d(tag, "onIceGatheringChange: $state")
            }

            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {
                Log.d(tag, "onIceCandidatesRemoved: ${candidates?.size ?: 0}")
            }

            override fun onAddStream(p0: MediaStream?) {
                TODO("Not yet implemented")
            }

            override fun onRemoveStream(p0: MediaStream?) {
                TODO("Not yet implemented")
            }

            override fun onRenegotiationNeeded() {
                Log.d(tag, "onRenegotiationNeeded")
                // Optional: createAndSendOffer()
            }

            override fun onDataChannel(dc: DataChannel?) {
                Log.d(tag, "onDataChannel: ${dc?.label()}")
            }
        })

        // 🚨 Do NOT addTransceiver here — only add actual tracks
        try {
            localVideoTrack?.let {
                peerConnection?.addTrack(it)
                Log.d(tag, "✅ Local video track added to PeerConnection")
            }
            localAudioTrack?.let {
                peerConnection?.addTrack(it)
                Log.d(tag, "✅ Local audio track added to PeerConnection")
            }
        } catch (e: Exception) {
            Log.e(tag, "Error adding local tracks: ${e.localizedMessage}", e)
        }
    }


    private fun createAndSendOffer() {
        // Use empty constraints for Unified Plan; legacy OfferToReceive* is not required
        val constraints = MediaConstraints()

        peerConnection?.createOffer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                Log.d(tag, "createOffer onCreateSuccess")
                val setObserver = object : SdpObserverAdapter() {
                    override fun onSetSuccess() {
                        Log.d(tag, "setLocalDescription succeeded for offer")
                    }

                    override fun onSetFailure(error: String) {
                        Log.e(tag, "setLocalDescription failed for offer: $error")
                    }
                }

                safeSetLocalDescription(setObserver, desc)

                partnerId?.let { pid ->
                    runOnMainThread {
                        try {
                            val json = JSONObject().apply {
                                put("partnerId", pid)
                                put("offer", JSONObject().apply {
                                    put("type", desc.type.canonicalForm())
                                    put("sdp", desc.description)
                                })
                            }
                            socket.emit("offer", json)
                            Log.d(tag, "Offer emitted to $pid")
                        } catch (e: Exception) {
                            Log.e(tag, "Failed to emit offer: ${e.localizedMessage}", e)
                        }
                    }
                } ?: Log.e(tag, "createAndSendOffer: partnerId is null")
            }

            override fun onCreateFailure(error: String) {
                Log.e(tag, "createOffer onCreateFailure: $error")
            }
        }, constraints)
    }

    private fun createAndSendAnswer() {
        val constraints = MediaConstraints()

        peerConnection?.createAnswer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                Log.d(tag, "createAnswer onCreateSuccess")
                val setObserver = object : SdpObserverAdapter() {
                    override fun onSetSuccess() {
                        Log.d(tag, "setLocalDescription succeeded for answer")
                    }

                    override fun onSetFailure(error: String) {
                        Log.e(tag, "setLocalDescription failed for answer: $error")
                    }
                }

                safeSetLocalDescription(setObserver, desc)

                partnerId?.let { pid ->
                    runOnMainThread {
                        try {
                            val json = JSONObject().apply {
                                put("partnerId", pid)
                                put("answer", JSONObject().apply {
                                    put("type", desc.type.canonicalForm())
                                    put("sdp", desc.description)
                                })
                            }
                            socket.emit("answer", json)
                            Log.d(tag, "Answer emitted to $pid")
                        } catch (e: Exception) {
                            Log.e(tag, "Failed to emit answer: ${e.localizedMessage}", e)
                        }
                    }
                } ?: Log.e(tag, "createAndSendAnswer: partnerId is null")
            }

            override fun onCreateFailure(error: String) {
                Log.e(tag, "createAnswer onCreateFailure: $error")
            }
        }, constraints)
    }

    private fun safeSetLocalDescription(observer: SdpObserver, desc: SessionDescription) {
        runOnMainThread {
            try {
                if (peerConnection == null) {
                    Log.e(tag, "safeSetLocalDescription: peerConnection is null, skipping setLocalDescription")
                    return@runOnMainThread
                }
                peerConnection?.setLocalDescription(observer, desc)
            } catch (e: Exception) {
                Log.e(tag, "Exception in setLocalDescription: ${e.localizedMessage}", e)
            }
        }
    }

    private fun runOnMainThread(action: () -> Unit) {
        val mainHandler = Handler(context.mainLooper)
        mainHandler.post { action() }
    }
}

open class SdpObserverAdapter : SdpObserver {
    private val tag = "WEBRTC_SDP"

    override fun onCreateSuccess(sessionDescription: SessionDescription) {
        Log.d(tag, "onCreateSuccess: ${sessionDescription.type}")
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
