package com.example.finalapp.screens.profile

import okhttp3.MultipartBody
import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.ProfileResponse
import com.example.finalapp.network.ApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import okhttp3.RequestBody.Companion.asRequestBody

import java.io.File
import javax.inject.Inject


@ViewModelScoped
class ProfileRepository @Inject constructor(private val api: ApiService) {

    fun getAllProfiles(): Flow<ProfileResponse> = flow {
        emit(api.getAllProfiles())
    }.flowOn(Dispatchers.IO)


    suspend fun uploadImage(imageUri: Uri,context: Context): Flow<ImageUploadResponse> = flow {
        emit(withContext(Dispatchers.IO) {
            val filePart = uriToMultipart(imageUri, context)
            api.uploadImage(filePart) })
    }.flowOn(Dispatchers.IO)
}




