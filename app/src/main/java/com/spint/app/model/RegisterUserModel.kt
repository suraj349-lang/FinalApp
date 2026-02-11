package com.spint.app.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class RegisterUserModel(
    val name:String,
    val email:String,
    val userName:String,
    var password:String,
    val token:String,
    val address:String
){
    companion object{
        fun empty():RegisterUserModel{
            return RegisterUserModel(
                name="",
                email = "",
                userName ="",
                password = "",
                token="",
                address = ""
            )
        }
    }
}

@Serializable
data class User(
    @SerializedName("_id")
    val user:String,
    val name:String,
    val number:String,
    val userName: String,
    val token: String,
    val address: String,
    val offers:List<String>?=null,
    val profileImage:String ="",
    val backgroundImage:String=""
){
    constructor():this("","","","","","", emptyList(),"","")
}

data class ProfileResponse(
  val success: Boolean,
  val data: List<User>
)

data class OkResponse(
    val success: Boolean,
    val data: User
)



