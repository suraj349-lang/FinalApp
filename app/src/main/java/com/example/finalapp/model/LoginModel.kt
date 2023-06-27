package com.example.finalapp.model

data class LoginModel(
  val number:String,
  val password:String
)
data class LoginAPIResponse(
  val success:Boolean,
  val code:Int,
  val token:String
)
