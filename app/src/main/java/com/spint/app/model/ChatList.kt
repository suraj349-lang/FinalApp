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
    val userName:String,
    val profileImage:String
)
//"_id": "6809e73d70b553b7cea89da1",
//"userId": "6809e6039efb6f51d256749c",
//"withUserId": {
//    "_id": "6809e6879efb6f51d25674a8",
//    "username": "shreya_123"
//},
//"lastMessage": "",
//"lastMessageTime": "2025-04-24T07:24:45.684Z",
//"__ v": 0

@Serializable
data class Message(
    val senderId:String,
    val receiverId:String,
    val message:String,
    val timestamp: String?,
    val sent: Int = 0, // 0 = pending, 1 = sent
    val received: Boolean = false
)