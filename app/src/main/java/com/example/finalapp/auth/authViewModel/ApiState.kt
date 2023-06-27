package com.example.finalapp.auth.authViewModel

import com.example.finalapp.model.LoginAPIResponse


sealed class ApiState{
    class Success(val data: LoginAPIResponse) : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
    object Empty: ApiState()

}

