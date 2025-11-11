package com.spint.app.utils

import com.spint.app.model.AllEventsResponseDTO


sealed class OfferApiState{
    class Success(val data: AllEventsResponseDTO) : OfferApiState()
    class Failure(val msg:Throwable) : OfferApiState()
    object Loading : OfferApiState()
    object Empty: OfferApiState()

}