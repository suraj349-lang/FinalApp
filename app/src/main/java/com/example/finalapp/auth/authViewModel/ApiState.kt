package com.example.finalapp.auth.authViewModel

import com.example.finalapp.model.ApiResponse
import com.example.finalapp.model.LoginModel

sealed class ApiState{
    class Success(val data: LoginModel) : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
    object Empty: ApiState()

}

