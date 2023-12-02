package com.example.finalapp.model

import java.util.Date

data class OfferModel(
  val name:String,
  val owner:String?=null
)

data class OfferResponseModel(
  val success:Boolean,
  val code:Int,
  val data:OfferModel
)
