package com.spint.app.repository

import com.spint.app.fcm.stateObject.SendFcmTokenDto
import com.spint.app.model.FCMTokenResponse
import com.spint.app.model.SignupAPIResponse
import com.spint.app.model.LoginModel
import com.spint.app.model.RegisterUserModel
import com.spint.app.model.LoginAPIResponse
import com.spint.app.network.NonAuthApiService
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

