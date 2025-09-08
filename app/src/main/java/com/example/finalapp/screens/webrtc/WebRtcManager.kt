package com.example.finalapp.screens.webrtc


import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.PeerConnectionFactory
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack

import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.RtpTransceiver
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.os.Handler
import android.util.Log
import com.example.finalapp.utils.constants.Constants
import org.webrtc.*
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

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
    private var remoteVideoTrack: VideoTrack? = null

    private var partnerId: String? = null

    fun init() {
        Log.d(tag, "Initializing WebRTCManager")

        // 1. Init PeerConnectionFactory
        initPeerConnectionFactory()

        // 2. Setup local camera/video
        initVideoCapturer()

        // 3. Connect to signaling server
        setupSocket()
    }
    fun setupLocalVideo() {
        Log.d(tag, "Setting up local video")

        videoCapturer = createCameraCapturer()
        if (videoCapturer == null) {
            Log.e(tag, "No front camera found")
            return
        }

        val surfaceTextureHelper =
            SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)

        videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer!!.initialize(surfaceTextureHelper, context, videoSource!!.capturerObserver)

        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource!!)
        localVideoTrack?.addSink(localRenderer) // ✅ preview always bound

        Log.d(tag, "Local video track created and sink added")
    }

    fun startLocalPreview() {
        Log.d(tag, "Starting local preview")
        videoCapturer?.let {
            try {
                it.startCapture(1024, 720, 30)
            } catch (e: Exception) {
                Log.e(tag, "Error starting camera capture: ${e.localizedMessage}")
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

            peerConnection?.close()
            peerConnection = null

            if (::socket.isInitialized) {
                socket.disconnect()
                socket.close()
            }
        } catch (e: Exception) {
            Log.e(tag, "Error during release: ${e.localizedMessage}")
        }
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
        videoCapturer = createCameraCapturer()
        if (videoCapturer == null) {
            Log.e(tag, "No front camera found")
            return
        }

        val surfaceTextureHelper =
            SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)

        videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer!!.initialize(surfaceTextureHelper, context, videoSource!!.capturerObserver)

        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource!!)
        localVideoTrack?.addSink(localRenderer)

        Log.d(tag, "Local video track created and sink added")
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
                    createPeerConnection()
                    createAndSendOffer()
                }
            }

            socket.on("offer") { args ->
                val data = args[0] as JSONObject
                partnerId = data.getString("partnerId")
                val offer = data.getJSONObject("offer")
                val sdp = offer.getString("sdp")
                val type = offer.getString("type")

                runOnMainThread {
                    createPeerConnection()
                    val sessionDescription = SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(type),
                        sdp
                    )
                    peerConnection?.setRemoteDescription(SdpObserverAdapter(), sessionDescription)
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
                    peerConnection?.setRemoteDescription(SdpObserverAdapter(), sessionDescription)
                }
            }

            socket.on("ice-candidate") { args ->
                val data = args[0] as JSONObject
                val candidate = data.getJSONObject("candidate")
                val sdpMid = candidate.getString("sdpMid")
                val sdpMLineIndex = candidate.getInt("sdpMLineIndex")
                val candidateStr = candidate.getString("candidate")

                runOnMainThread {
                    peerConnection?.addIceCandidate(
                        IceCandidate(sdpMid, sdpMLineIndex, candidateStr)
                    )
                }
            }

            socket.on("partner-disconnected") {
                Log.d(tag, "Partner disconnected")
                // TODO: update UI if needed
            }

        } catch (e: Exception) {
            Log.e(tag, "Socket error: ${e.localizedMessage}")
        }
    }

    private fun createPeerConnection() {
        Log.d(tag, "Creating PeerConnection")
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )
        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)

        peerConnection = peerConnectionFactory.createPeerConnection(
            rtcConfig,
            object : PeerConnection.Observer {
                override fun onIceCandidate(candidate: IceCandidate) {
                    val json = JSONObject()
                    json.put("partnerId", partnerId)
                    json.put("candidate", JSONObject().apply {
                        put("sdpMid", candidate.sdpMid)
                        put("sdpMLineIndex", candidate.sdpMLineIndex)
                        put("candidate", candidate.sdp)
                    })
                    socket.emit("ice-candidate", json)
                }

                override fun onTrack(transceiver: RtpTransceiver?) {
                    transceiver?.receiver?.track()?.let { track ->
                        if (track is VideoTrack) {
                            remoteVideoTrack = track
                            remoteVideoTrack?.addSink(remoteRenderer)
                        }
                    }
                }

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                    Log.d(tag, "IceConnectionState: $state")
                }

                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
                    Log.d(tag, "PeerConnection state: $newState")
                }

                // Unused callbacks
                override fun onDataChannel(dc: DataChannel?) {}
                override fun onSignalingChange(state: PeerConnection.SignalingState?) {}
                override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>) {}
                override fun onIceConnectionReceivingChange(p0: Boolean) {}
                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {}
                override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {}
                override fun onAddStream(stream: MediaStream?) {}
                override fun onRemoveStream(p0: MediaStream?) {}
                override fun onRemoveTrack(receiver: RtpReceiver?) {}
                override fun onRenegotiationNeeded() {}
                override fun onSelectedCandidatePairChanged(event: CandidatePairChangeEvent?) {}
            }
        )

        // Add local video track to peerConnection
        localVideoTrack?.let { track ->
            peerConnection?.addTrack(track)
        }
    }

    private fun createAndSendOffer() {
        peerConnection?.createOffer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                val json = JSONObject()
                json.put("partnerId", partnerId)
                json.put("offer", JSONObject().apply {
                    put("type", desc.type.canonicalForm())
                    put("sdp", desc.description)
                })
                socket.emit("offer", json)
            }
        }, MediaConstraints())
    }

    private fun createAndSendAnswer() {
        peerConnection?.createAnswer(object : SdpObserverAdapter() {
            override fun onCreateSuccess(desc: SessionDescription) {
                peerConnection?.setLocalDescription(SdpObserverAdapter(), desc)
                val json = JSONObject()
                json.put("partnerId", partnerId)
                json.put("answer", JSONObject().apply {
                    put("type", desc.type.canonicalForm())
                    put("sdp", desc.description)
                })
                socket.emit("answer", json)
            }
        }, MediaConstraints())
    }

    private fun runOnMainThread(action: () -> Unit) {
        val mainHandler = Handler(context.mainLooper)
        mainHandler.post { action() }
    }
}



open class SdpObserverAdapter : SdpObserver {
    override fun onCreateSuccess(sessionDescription: SessionDescription) {}
    override fun onSetSuccess() {}
    override fun onCreateFailure(error: String) {}
    override fun onSetFailure(error: String) {}
}
