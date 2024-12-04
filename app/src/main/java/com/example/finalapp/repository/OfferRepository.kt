package com.example.finalapp.repository

import android.content.Context
import android.net.Uri
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.GetDropProfileResponseModel
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.Response
import com.example.finalapp.model.SingleOfferModel
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
class OfferRepository @Inject constructor(private val api: ApiService) {

    fun sendCreateEventData(offerData: OfferModel): Flow<SingleOfferModel> = flow  {
        emit(api.createEvent(offerData))
    }.flowOn(Dispatchers.IO)

    fun sendDropProfileData(data: DropProfileModel): Flow<DropProfileResponseModel> = flow  {
        emit(api.dropProfile(data))
    }.flowOn(Dispatchers.IO)

    fun getDropProfileData(): Flow<GetDropProfileResponseModel> = flow  {
        emit(api.getDropProfile())
    }.flowOn(Dispatchers.IO)
    fun getAllOffers(): Flow<OfferResponseModel> = flow {
        emit(api.getAllOffers())
    }.flowOn(Dispatchers.IO)


    suspend fun uploadImage(imageUri: Uri, context: Context): Flow<ImageUploadResponse> = flow {
        emit(withContext(Dispatchers.IO) {
            val filePart = uriToMultipart(imageUri, context)
            api.uploadImage(filePart) })
    }.flowOn(Dispatchers.IO)
}

