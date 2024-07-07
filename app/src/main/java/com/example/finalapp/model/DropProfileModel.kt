package com.example.finalapp.model

data class DropProfileModel(
    val image:String,
    val location:String,
    val landmark:String,
    val message:String?=null,
    val expirationTime:String
)

data class DropProfileResponseModel(
    val message:String,
    val data:DropProfileModel
)