package com.spint.app.model

import kotlinx.serialization.Serializable

data class ChatList(
    val userId:String,
    val withUserId: WithUserId,
    val lastMessage:String,
    val lastMessageTime: String
)

data class WithUserId(
    val _id: String,
    val name:String,
    val profileImage:String?
)

@Serializable
data class Message(
    val senderId:String,
    val receiverId:String,
    val message:String,
    val timestamp: String?,
    val sent: Int = 0, // 0 = pending, 1 = sent
    val received: Boolean = false
)