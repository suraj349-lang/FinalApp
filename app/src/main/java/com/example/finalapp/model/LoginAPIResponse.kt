package com.example.finalapp.model

import com.example.finalapp.model.User

data class LoginAPIResponse(
    val success:Boolean,
    val code:Int,
    val data: User
)