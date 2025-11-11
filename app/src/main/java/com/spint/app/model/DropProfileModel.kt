package com.spint.app.model

import kotlinx.serialization.Serializable
import java.util.Date

data class DropProfileModel(
    val id:String?=null,
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
@Serializable
data class DropProfileResponse(
    val id:String?=null,
    var image:String,
    val personalEvent:Boolean=true,
    val tag:String="",
    val location:String,
    val message:String,
    val expirationTime:String,
    val validTill:String? =null,
    val createdAt:String?=null,
    val createdBy:User=User(),
)

data class DropProfileResponseModel(
    val message:String,
    val data:DropProfileModel
)

data class GetDropProfileResponseModel(
    val message:String,
    val data:List<DropProfileResponse>
)