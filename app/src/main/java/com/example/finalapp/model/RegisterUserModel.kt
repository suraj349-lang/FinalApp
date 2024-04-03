package com.example.finalapp.model

data class RegisterUserModel(
  val name:String,
  val number:String,
  val username:String,
  val password:String,
  val token:String,
  val address:String
)
data class SignupAPIResponse(
  val success:Boolean,
  val code:Int,
  val token:String,
  val data:User,
  val _id:String
)
data class User(
  val name:String,
  val number:String,
  val username: String,
  val _id:String,
  val token: String,
  val address: String,
  val offers:List<String>
)

data class ProfileResponse(
  val success: Boolean,
  val data: List<User>
)