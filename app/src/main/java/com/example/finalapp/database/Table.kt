package com.example.finalapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name:String,
    val username:String,
    val number:String,
    val token:String,
    val address:String

)

@Entity(tableName = "chat_table")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val sentTo:String,
    val message:String,
    val sent:String,
    val received:String,
    val seen:String,
    val timeStamp:Long
)