package com.spint.app.viewmodels



import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spint.app.fcm.api.FcmApi
import com.spint.app.fcm.stateObject.ChatState
import com.spint.app.fcm.stateObject.NotificationBody
import com.spint.app.fcm.stateObject.SendMessageDto
import com.spint.app.fcm.stateObject.SendTokenDto
import com.spint.app.utils.constants.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class FCMViewModel: ViewModel() {

    var state by mutableStateOf(ChatState())
        private set

    private val api: FcmApi = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
        GsonConverterFactory.create()
    ).build().create(FcmApi::class.java)

    init {
        viewModelScope.launch {
            Firebase.messaging.subscribeToTopic("chat").await()

        }
    }

    fun onRemoteTokenChange(newToken: String) {
        state = state.copy(
            remoteToken = newToken
        )
    }

    fun onSubmitRemoteToken() {
        state = state.copy(
            isEnteringToken = false
        )
    }

    fun onMessageChange(message: String) {
        state = state.copy(
            messageText = message
        )
    }
    fun sendToken(localToken: String) {
       viewModelScope.launch {
          // val token = Firebase.messaging.token.await()
           try {
               val number=FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
               api.sendToken(SendTokenDto(token = localToken,number=if(number!="") number else "+919097870920" ))
               Log.d("Suraj Token sent", localToken)
           }catch (e:Exception){
               Log.d("Suraj Token sent error", e.message.toString())
           }

       }
    }
    fun updateToken(token:String){
        viewModelScope.launch {
            val number=FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
            try {
                api.updateToken(
                    SendTokenDto(token,if(number!="") number else "1234567")
                )
            }catch (e:Exception){
                Log.d("Updating token error", e.message.toString())
            }
        }
    }

    fun sendMessage2(isBroadcast: Boolean) {
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = if(isBroadcast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "New message!",
                    body = state.messageText
                )
            )

            try {
                if(isBroadcast) {
                    api.broadcast(messageDto)
                } else {
                    api.sendMessage(messageDto)
                }

                state = state.copy(
                    messageText = ""
                )
            } catch(e: HttpException) {
                Log.d("error sending ", e.message.toString())
                e.printStackTrace()
            } catch(e: IOException) {
                Log.d("error sending ", e.message.toString())
                e.printStackTrace()
            }
        }
    }


    fun sendMessage(isBroadcast:Boolean=false){
        viewModelScope.launch {
            val message=SendMessageDto(to=Firebase.messaging.token.await(), notification = NotificationBody(title = "ZUNE",body="hello,how are you"))
            try {
                api.sendMessage(message)
            }catch (e:Exception){
                Log.d("Notification", e.message.toString())
            }

        }
    }
}