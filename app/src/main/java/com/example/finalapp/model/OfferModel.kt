package com.example.finalapp.model

import android.location.Location
import com.google.android.datatransport.cct.StringMerger
import java.util.Date

data class OfferModel(
  val image:String,
  val category: String,
  val location: String,
  val offer:String,
  val expirationTime:String
)


