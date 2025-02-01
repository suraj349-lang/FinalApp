package com.example.finalapp.model.DTO.Response

import com.example.finalapp.model.User

data class SignupAPIResponse(
    val success:Boolean,
    val code:Int,
    val token:String,
    val data: User,
    val _id:String
)