package com.example.finalapp.fcm

import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import com.example.finalapp.fcm.viewmodel.ChatViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

class PushNotificationService: FirebaseMessagingService() {
    private val viewModel=ChatViewModel()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        try {
            viewModel.updateToken(token)
        }catch (e:Exception){
            Log.d("Error updating token", e.message.toString())
        }



        // Update server and save the new token generated for the user which is generated te first time user launches app.
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        // Respond to received messages and you can customize your notification rather than default way by firebase
    }
}