package com.example.finalapp.model


data class User(
  val id:String,
  val name:String,
  val username:String,
  val email:String,
  val number:Int,
  val post: List<Post>?=null
  )
