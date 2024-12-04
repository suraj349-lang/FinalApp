package com.example.finalapp.utils

import com.example.finalapp.model.OfferResponseModel


sealed class OfferApiState{
    class Success(val data: OfferResponseModel) : OfferApiState()
    class Failure(val msg:Throwable) : OfferApiState()
    object Loading : OfferApiState()
    object Empty: OfferApiState()

}