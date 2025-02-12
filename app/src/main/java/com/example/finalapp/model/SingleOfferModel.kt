package com.example.finalapp.model

import com.example.finalapp.model.OfferModel

data class SingleOfferModel(
    val success:Boolean,
    val code:Int,
    val data: OfferModel
)
