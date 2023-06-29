package com.example.finalapp.auth.authViewModel

import com.example.finalapp.model.LoginAPIResponse
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