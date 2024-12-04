package com.example.finalapp.repository

import android.content.Context
import android.net.Uri
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.Response
import com.example.finalapp.network.ApiService
import com.example.finalapp.screens._4profile.uriToMultipart
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

import javax.inject.Inject


@ViewModelScoped
class ProfileRepository @Inject constructor(private val api: ApiService) {

    fun updateUserImage(email: String, imageUrl: String): Flow<Response> = flow {
        emit(api.updateUserImage(email,imageUrl))
    }.flowOn(Dispatchers.IO)


    //--------------------------------------Get user data ---------------------------------//
    fun getUserData(number: String): Flow<Response> = flow {
        emit(api.getUserData(number))
    }.flowOn(Dispatchers.IO)

}




