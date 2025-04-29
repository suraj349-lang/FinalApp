package com.example.finalapp.screens._8notification.pushNotificationService


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.finalapp.MainActivity
import com.example.finalapp.R
import com.example.finalapp.fcm.stateObject.SendFcmTokenDto
import com.example.finalapp.repository.AuthRepository
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.FCMViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService: FirebaseMessagingService() {
    @Inject
    lateinit var authRepository: AuthRepository


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        try {
            CoroutineScope(Dispatchers.IO).launch {
                if(ProfileObject.profile !=null && ProfileObject.profile?.userId !=null) {
                    authRepository.updateFcmToken(
                        SendFcmTokenDto(
                            ProfileObject.profile?.userId!!,
                            token
                        )
                    )
                        .collect { response ->
                            Log.d("FMCTOKENUPDATE", "Token updated on server: $response")
                        }
                }
            }
        }catch (e:Exception){
            Log.d("FMCTOKENUPDATE","onNewToken"+ e.message.toString())
        }



        // Update server and save the new token generated for the user which is generated te first time user launches app.
    }
//
//        override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//
//        // Respond to received messages and you can customize your notification rather than default way by firebase
//    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Received message: ${remoteMessage.data} ${remoteMessage.notification}")
        remoteMessage.notification?.let {
            showNotification(it.title ?: "Chat", it.body ?: "New message received")
        }
    }

//    private fun showNotification(title: String, message: String) {
//        val notificationBuilder = NotificationCompat.Builder(this, "CHAT_CHANNEL")
//            .setSmallIcon(R.drawable.notification_new)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0, notificationBuilder.build())
//    }


//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    private fun showNotification(title: String, message: String) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        val notificationBuilder = NotificationCompat.Builder(this, "CHAT_CHANNEL")
//            .setSmallIcon(R.drawable.notification_new)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setColor(ContextCompat.getColor(this, androidx.cardview.R.color.cardview_dark_background))
//            .setContentIntent(pendingIntent)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Expandable big text
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.app_icon))
//            .setVibrate(longArrayOf(0, 500, 1000)) // vibration pattern
//            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // default sound
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(0, notificationBuilder.build())
//    }


    private var notificationId = 1
    private val groupKey = "com.example.finalapp.CHAT_GROUP"

    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.notification_new)

        val messageNotification = NotificationCompat.Builder(this, "CHAT_CHANNEL")
            .setSmallIcon(R.drawable.notification_new)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setGroup(groupKey)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Create a group summary notification
        val summaryNotification = NotificationCompat.Builder(this, "CHAT_CHANNEL")
            .setSmallIcon(R.drawable.notification_new)
            .setContentTitle("New messages")
            .setContentText("You have new messages")
            .setStyle(NotificationCompat.InboxStyle()
                .addLine("$title: $message") // You can add multiple lines here if you want
                .setSummaryText("New messages"))
            .setGroup(groupKey)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId++, messageNotification) // different ID for each message
        notificationManager.notify(0, summaryNotification) // 0 = summary
    }


}