package com.example.finalapp.auth.repository

import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.network.ApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ViewModelScoped
class AuthRepository @Inject constructor(private val api:ApiService){
    fun sendLoginData(loginData: LoginModel): Flow<LoginAPIResponse> = flow  {
        emit(api.postLoginData(loginData))
    }.flowOn(Dispatchers.IO)

    fun sendSignupData(signupData: RegisterUserModel): Flow<SignupAPIResponse> = flow  {
        emit(api.postSignupData(signupData))
    }.flowOn(Dispatchers.IO)
}

