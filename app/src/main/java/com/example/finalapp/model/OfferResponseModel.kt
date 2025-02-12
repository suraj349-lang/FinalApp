package com.example.finalapp.model

import com.example.finalapp.model.OfferModel

data class OfferResponseModel(
    val success:Boolean,
    val code:Int,
    val data:List<OfferModel>
)
