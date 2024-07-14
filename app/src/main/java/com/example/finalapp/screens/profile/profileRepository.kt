package com.example.finalapp.screens.profile

import android.content.Context
import android.net.Uri
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.ProfileResponse
import com.example.finalapp.model.Response
import com.example.finalapp.network.ApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

import javax.inject.Inject


@ViewModelScoped
class ProfileRepository @Inject constructor(private val api: ApiService) {

    fun getAllOffers(): Flow<OfferResponseModel> = flow {
        emit(api.getAllOffers())
    }.flowOn(Dispatchers.IO)
    fun updateUserImage(number: String, imageUrl: String): Flow<Response> = flow {
        emit(api.updateUserImage(number,imageUrl))
    }.flowOn(Dispatchers.IO)


    suspend fun uploadImage(imageUri: Uri,context: Context): Flow<ImageUploadResponse> = flow {
        emit(withContext(Dispatchers.IO) {
            val filePart = uriToMultipart(imageUri, context)
            api.uploadImage(filePart) })
    }.flowOn(Dispatchers.IO)
}




