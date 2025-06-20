package com.example.finalapp.model

import com.example.finalapp.model.pings.PingResponse

data class PreSignedUrlResponse(
    val key:String,
    val url:String,
)

data class CreatePingResponse(
    val success:String,
    val message:String,
    val data:String
)
