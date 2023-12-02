package com.example.finalapp.apiState

import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.SignupAPIResponse


sealed class LoginApiState{
    class Success(val data: LoginAPIResponse) : LoginApiState()
    class Failure(val msg:Throwable) : LoginApiState()
    object Loading : LoginApiState()
    object Empty: LoginApiState()

}

sealed class SignupApiState{
    class Success(val data: SignupAPIResponse) : SignupApiState()
    class Failure(val msg:Throwable) : SignupApiState()
    object Loading : SignupApiState()
    object Empty: SignupApiState()

}
sealed class OfferApiState{
    class Success(val data: OfferResponseModel) : OfferApiState()
    class Failure(val msg:Throwable) : OfferApiState()
    object Loading : OfferApiState()
    object Empty: OfferApiState()

}