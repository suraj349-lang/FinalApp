package com.spint.app.model

data class LoginAPIResponse(
    val success:Boolean,
    val code:Int,
    val data: User
)

data class FCMTokenResponse(
    val success: Boolean,
    val message:String
)