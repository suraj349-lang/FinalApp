package com.spint.app.model

data class SignupAPIResponse(
    val success:Boolean,
    val code:Int,
    val token:String,
    val data: User,
    val _id:String
)