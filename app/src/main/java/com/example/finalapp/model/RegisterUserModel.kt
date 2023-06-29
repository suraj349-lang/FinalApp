package com.example.finalapp.model

data class RegisterUserModel(
  val name:String,
  val number:String,
  val username:String,
  val password:String
)
data class SignupAPIResponse(
  val success:Boolean,
  val code:Int,
  val token:String
)