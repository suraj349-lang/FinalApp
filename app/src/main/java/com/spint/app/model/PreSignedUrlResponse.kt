package com.spint.app.model

data class PreSignedUrlResponse(
    val key:String,
    val url:String,
)

data class CreateFlashPostResponse(
    val success:String,
    val message:String,
    val data:String
)
