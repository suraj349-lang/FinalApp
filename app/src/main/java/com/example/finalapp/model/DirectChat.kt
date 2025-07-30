package com.example.finalapp.model

import com.bumptech.glide.request.RequestCoordinator.RequestState

data class DirectChat(
    val userId:User,
    val lat: Double,
    val long: Double,
)
data class DirectChatRequest(
    val userId:String,
    val lat: Double,
    val long: Double,
)

data class DirectChatApiResponse(
    val success: Boolean,
    val data: List<DirectChat>
)

