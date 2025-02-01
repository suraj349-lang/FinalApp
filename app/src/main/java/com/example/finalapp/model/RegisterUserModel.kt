package com.example.finalapp.model

data class RegisterUserModel(
    val name:String,
    val number:String,
    val username:String,
    var password:String,
    val token:String,
    val address:String
)

data class User(
  val _id:String,
  val name:String,
  val number:String,
  val username: String,
  val token: String,
  val address: String,
  val offers:List<String>?=null,
  val profileImage:String
){
    constructor():this("","","","","","", emptyList(),"")
}
data class ChatUser(
  val name:String,
  val number:String
)
data class ProfileResponse(
  val success: Boolean,
  val data: List<User>
)

data class OkResponse(
    val success: Boolean,
    val data: User
)