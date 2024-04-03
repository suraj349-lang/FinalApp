package com.example.finalapp.screens.profile

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import android.content.Context
import android.net.Uri
import com.example.finalapp.database.Profile
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.ProfileResponse
import com.example.finalapp.model.User
import com.example.finalapp.network.ApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import okhttp3.RequestBody.Companion.asRequestBody

import java.io.File
import javax.inject.Inject


@ViewModelScoped
class ProfileRepository @Inject constructor(private val api: ApiService) {

    fun getAllProfiles(): Flow<ProfileResponse> = flow  {
        emit(api.getAllProfiles())
    }.flowOn(Dispatchers.IO)


   suspend fun uploadImage(context: Context, imageUri: String?): String {

       return withContext(Dispatchers.IO) {
           val imageFile = File(imageUri)
           val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
           val requestBodyPart =
               MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

           api.uploadImage(requestBodyPart)
       }
   }}



