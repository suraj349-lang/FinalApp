package com.spint.app.Duel.presentation.viewModel

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spint.app.Duel.data.CallState
import com.spint.app.Duel.data.MatchResponse
import com.spint.app.Duel.data.SocketManager
import com.spint.app.utils.UserObject
import io.agora.rtc2.*
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class CallViewModel : ViewModel() {
    var callState by mutableStateOf<CallState>(CallState.Idle)
    var remoteUid by mutableStateOf<Int?>(null)
        private set
    private var rtcEngine: RtcEngine? = null

    var showPostCallDialog by mutableStateOf(false)
    var remainingTime by mutableStateOf(0)
        private set
    var isSpeakerEnabled by mutableStateOf(true)
        private set

    fun toggleSpeaker() {
        isSpeakerEnabled = !isSpeakerEnabled
        rtcEngine?.setEnableSpeakerphone(isSpeakerEnabled)
    }



    fun initAgora(context: Context, appId: String) {
        if (rtcEngine != null) return



        rtcEngine = RtcEngine.create(
            context,
            appId,
            object : IRtcEngineEventHandler() {

                override fun onUserJoined(uid: Int, elapsed: Int) {
                    Log.d("AGORA", "Remote joined: $uid")
                    remoteUid = uid
                }


                override fun onUserOffline(uid: Int, reason: Int) {
                    endCall()
                }

                override fun onLeaveChannel(stats: RtcStats?) {
                    callState = CallState.Matching
                }
            }
        )

        rtcEngine?.enableVideo()
        rtcEngine?.setAudioScenario(
            Constants.AUDIO_SCENARIO_GAME_STREAMING
        )

        rtcEngine?.setDefaultAudioRoutetoSpeakerphone(true)
        rtcEngine?.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE
            )
        )

        rtcEngine?.setDefaultAudioRoutetoSpeakerphone(true)
        rtcEngine?.enableAudio()
    }
    var remoteUserId by mutableStateOf<String?>(null)
    var remoteUserName by mutableStateOf<String?>(null)
    var remoteUserImage by mutableStateOf<String?>(null)


    fun setRemoteUser(id: String, name: String, image: String) {
        remoteUserId = id
        remoteUserName = name
        remoteUserImage = image
    }


    fun joinCall(token: String, channel: String, uid: Int, expiresIn: Int) {
        val result = rtcEngine?.joinChannel(
            token,
            channel,
            uid,
            ChannelMediaOptions().apply {
                clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
                channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION
            }
        )

        Log.d("AGORA", "joinChannel result = $result")

        callState = CallState.InCall(channel)
        rtcEngine?.setEnableSpeakerphone(isSpeakerEnabled)

        // 🔥 60 second auto leave
        viewModelScope.launch {

            remainingTime = expiresIn

            while (remainingTime > 0) {
                delay(1000)
                remainingTime--
            }

            endCall()
            showPostCallDialog = true
        }


    }

    fun setupLocalVideo(surfaceView: SurfaceView) {
        rtcEngine?.setupLocalVideo(
            VideoCanvas(
                surfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                0
            )
        )
    }

    fun setupRemoteVideo(surfaceView: SurfaceView, uid: Int) {
        rtcEngine?.setupRemoteVideo(
            VideoCanvas(
                surfaceView,
                VideoCanvas.RENDER_MODE_HIDDEN,
                uid
            )
        )
    }

//    fun endCall() {
//        rtcEngine?.leaveChannel()
//        remoteUid = null
//        rtcEngine?.setEnableSpeakerphone(false)
//    }

    fun endCall() {

        rtcEngine?.stopPreview()

        rtcEngine?.leaveChannel()

        rtcEngine?.setEnableSpeakerphone(false)

        remoteUid = null

        callState = CallState.Idle
    }



    override fun onCleared() {
        super.onCleared()
        RtcEngine.destroy()
    }
    fun startPreview(){
        rtcEngine?.startPreview()
    }


}

