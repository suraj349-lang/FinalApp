package com.example.finalapp.auth.repository

import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class AuthRepository {
    fun sendLoginData(loginData: LoginModel): Flow<LoginAPIResponse> = flow  {
        emit(ApiService.getInstance().postLoginData(loginData))
    }.flowOn(Dispatchers.IO)

    fun sendSignupData(signupData: RegisterUserModel): Flow<SignupAPIResponse> = flow  {
        emit(ApiService.getInstance().postSignupData(signupData))
    }.flowOn(Dispatchers.IO)
}

