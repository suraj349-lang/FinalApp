package com.example.finalapp.model

import java.util.Date

data class DropProfileModel(
    var image:String,
    val personalEvent:Boolean=true,
    val tag:String="",
    val location:String,
    val message:String,
    val expirationTime:String,
    val validTill:Date?=null,
    val createdAt:Date?=null,
    val createdBy:String,
)

data class DropProfileResponseModel(
    val message:String,
    val data:DropProfileModel
)

data class GetDropProfileResponseModel(
    val message:String,
    val data:List<DropProfileModel>
)