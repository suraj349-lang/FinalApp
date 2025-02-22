package com.example.finalapp.fcm

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.finalapp.R
import com.example.finalapp.viewmodels.FCMViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

class PushNotificationService: FirebaseMessagingService() {
    private val viewModel= FCMViewModel()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        try {
            viewModel.updateToken(token)
        }catch (e:Exception){
            Log.d("Error updating token", e.message.toString())
        }



        // Update server and save the new token generated for the user which is generated te first time user launches app.
    }

//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//
//        // Respond to received messages and you can customize your notification rather than default way by firebase
//    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            showNotification(it.title ?: "Chat", it.body ?: "New message received")
        }
    }
    private fun showNotification(title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(this, "CHAT_CHANNEL")
            .setSmallIcon(R.drawable.notification_new)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}