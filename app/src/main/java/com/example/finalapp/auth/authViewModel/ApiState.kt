package com.example.finalapp.auth.authViewModel

import com.example.finalapp.model.ApiResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.User

sealed class ApiState{
    class Success(val data: User) : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
    object Empty: ApiState()

}

