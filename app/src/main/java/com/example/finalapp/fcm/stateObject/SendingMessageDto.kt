package com.example.finalapp.fcm.stateObject

data class SendMessageDto(
    val to: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String
)

data class SendTokenDto(
    val token:String,
    val number:String
)

data class SendFcmTokenDto(
    val fcmToken:String,
    val userId:String
)