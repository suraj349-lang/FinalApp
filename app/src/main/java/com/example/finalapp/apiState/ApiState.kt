package com.example.finalapp.apiState

import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.SignupAPIResponse



sealed class OfferApiState{
    class Success(val data: OfferResponseModel) : OfferApiState()
    class Failure(val msg:Throwable) : OfferApiState()
    object Loading : OfferApiState()
    object Empty: OfferApiState()

}