package com.spint.app.screens._8notification.pushNotificationService


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.spint.app.R
import com.spint.app.database.tables.NotificationEntity
import com.spint.app.enums.Notifications
import com.spint.app.fcm.stateObject.SendFcmTokenDto
import com.spint.app.model.User
import com.spint.app.repository.AuthRepository
import com.spint.app.repository.NotificationRepository
import com.spint.app.utils.UserObject
import com.spint.app.utils.constants.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService:  FirebaseMessagingService() {
    @Inject
    lateinit var authRepository: AuthRepository
    @Inject
    lateinit var notificationRepository: NotificationRepository



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        try {
            CoroutineScope(Dispatchers.IO).launch {
                if(UserObject.user.value != User() && UserObject.user.value.user.isNotEmpty()) {
                    authRepository.updateFcmToken(
                        SendFcmTokenDto(
                            UserObject.user.value.user,
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


//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        Log.d("FCM", "Received message: ${remoteMessage.data} ${remoteMessage.notification}")
//        remoteMessage.notification?.let {
//            showNotification(it.title ?: "Chat", it.body ?: "New message received")
//        }
//    }

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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("NotificationSPINT", "Received message: ${remoteMessage.data} ${remoteMessage.notification}")

        val title = remoteMessage.data["title"]
            ?: Constants.APP_NAME

        val body = remoteMessage.data["body"]
            ?: "You have a new message"

        val type = remoteMessage.data["type"] ?: Notifications.CHAT.value // Optional
        showNotification(title, body,type)
        CoroutineScope(Dispatchers.IO).launch {
            val entity = NotificationEntity(
                title = title,
                body = body,
                type = type
            )
            notificationRepository.saveNotification(entity)
        }



    }




    private var notificationId = 1
    private val groupKey = "com.example.finalapp.CHAT_GROUP"

//    private fun showNotification(title: String, message: String,type:String) {
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.notification_new)
//
//        val messageNotification = NotificationCompat.Builder(this, "CHAT_CHANNEL")
//            .setSmallIcon(R.drawable.notification_new)
//            .setLargeIcon(largeIcon)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setGroup(groupKey)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .build()
//
//        // Create a group summary notification
//        val summaryNotification = NotificationCompat.Builder(this, "CHAT_CHANNEL")
//            .setSmallIcon(R.drawable.notification_new)
//            .setContentTitle("New messages")
//            .setContentText("You have new messages")
//            .setStyle(NotificationCompat.InboxStyle()
//                .addLine("$title: $message") // You can add multiple lines here if you want
//                .setSummaryText("New messages"))
//            .setGroup(groupKey)
//            .setGroupSummary(true)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager.notify(notificationId++, messageNotification) // different ID for each message
//        notificationManager.notify(0, summaryNotification) // 0 = summary
//    }


    private fun showNotification(title: String, message: String, type: String) {
        Log.d("NotificationSPINT", "Showing notification: $title - $message - $type")

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.app_icon_dynamic)

        val builder = NotificationCompat.Builder(this, "CHAT_CHANNEL")
            .setSmallIcon(R.drawable.notification_new)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup("com.example.finalapp.CHAT_GROUP")

        notificationManager.notify(notificationId++, builder.build())
    }


}
//fun createNotificationChannel(context: Context) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channelId = "CHAT_CHANNEL"
//        val channelName = "Chat Notifications"
//        val channelDescription = "Notifications for chat messages"
//        val importance = NotificationManager.IMPORTANCE_HIGH
//
//        val channel = NotificationChannel(channelId, channelName, importance).apply {
//            description = channelDescription
//            enableLights(true)
//            enableVibration(true)
//        }
//
//        val notificationManager = context.getSystemService(NotificationManager::class.java)
//        notificationManager.createNotificationChannel(channel)
//    }
//}

fun createNotificationChannels(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channels = listOf(
            NotificationChannel(
                "CHAT_CHANNEL",
                "Chat Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new chat messages"
            },
            NotificationChannel(
                "OFFER_CHANNEL",
                "Special Offers",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for promotional offers"
            },
            NotificationChannel(
                "REMINDER_CHANNEL",
                "Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for reminders and alerts"
            }
        )

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        channels.forEach { notificationManager.createNotificationChannel(it) }
    }
}

