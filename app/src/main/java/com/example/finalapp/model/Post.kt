package com.example.finalapp.model

import java.util.Date

data class Post(
  val id:String,
  val image:String,
  val validTime:Date,
  val offer:String?=null,
  val acceptCount:List<String>?=null,
  val winnerUser:String?=null
)
