package com.example.finalapp.utils

import com.example.finalapp.model.AllEventsResponseDTO


sealed class OfferApiState{
    class Success(val data: AllEventsResponseDTO) : OfferApiState()
    class Failure(val msg:Throwable) : OfferApiState()
    object Loading : OfferApiState()
    object Empty: OfferApiState()

}