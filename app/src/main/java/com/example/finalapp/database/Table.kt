package com.example.finalapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0 ,
    val userId:String,
    val name:String,
    val username:String,
    val number:String,
    val address:String,
    val profileImage:String

){
    constructor():this(0,"","","","","","")
}

@Serializable
@Entity(tableName = "chat_table")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val sentTo:String,
    val sentFrom:String,
    val message:String,
    val sent:Int,
    val received:Boolean,
    val seen:Boolean,
//    val timeStamp:Long?=null
)