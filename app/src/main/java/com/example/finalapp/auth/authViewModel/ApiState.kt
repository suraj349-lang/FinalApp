package com.example.finalapp.auth.authViewModel

import com.example.finalapp.model.ApiResponse
import com.example.finalapp.model.LoginModel

sealed class ApiState{
    class Success(val data: LoginModel) : ApiState()
    class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
    object Empty: ApiState()

}

sealed class RegisterUserApiState{
    class Success(val data: ApiResponse) :RegisterUserApiState()
    class Failure(val msg:Throwable) : RegisterUserApiState()
    object Loading : RegisterUserApiState()
    object Empty: RegisterUserApiState()

}
