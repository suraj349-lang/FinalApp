package com.example.finalapp.repository

import com.example.finalapp.fcm.stateObject.SendFcmTokenDto
import com.example.finalapp.model.FCMTokenResponse
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.network.NonAuthApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class AuthRepository @Inject constructor(private val noAuthApi:NonAuthApiService){
    fun sendLoginData(loginData: LoginModel): Flow<LoginAPIResponse> = flow  {
        emit(noAuthApi.postLoginData(loginData))
    }.flowOn(Dispatchers.IO)

    fun sendSignupData(signupData: RegisterUserModel): Flow<SignupAPIResponse> = flow {
        emit(noAuthApi.postSignupData(signupData))
    }.flowOn(Dispatchers.IO)

    fun updateFcmToken(data: SendFcmTokenDto): Flow<FCMTokenResponse> = flow  {
        emit(noAuthApi.updateFcmToken(data))
    }.flowOn(Dispatchers.IO)
}

