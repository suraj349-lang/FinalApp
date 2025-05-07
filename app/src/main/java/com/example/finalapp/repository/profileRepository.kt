package com.example.finalapp.repository

import android.util.Log
import com.example.finalapp.model.PreSignedUrlResponse
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.OkResponse
import com.example.finalapp.network.ApiService
import com.example.finalapp.network.NonAuthApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

import javax.inject.Inject


@ViewModelScoped
class ProfileRepository @Inject constructor(private val api: ApiService,private val nonAuthApiService: NonAuthApiService) {
    fun getPreSignedUrl(userId:String):Flow<PreSignedUrlResponse> = flow{
        emit(api.getPreSignedUrl(userId))
    }.flowOn(Dispatchers.IO)

    //-----------------------------------------------------------------------------------------------//
    suspend fun uploadImageToS3(presignedUrl: String, file: File): Boolean {
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        val response = try {
            nonAuthApiService.uploadImageToS3(presignedUrl, requestBody)
        } catch (e: Exception) {
            Log.e("S3 Upload", "Upload failed: ${e.localizedMessage}")
            return false
        }

        return if (response.isSuccessful) {
            Log.d("S3 Upload", "Upload successful!")
            true
        } else {
            Log.e("S3 Upload", "Failed with status: ${response.code()}")
            false
        }
    }


    fun updateUserImage(email: String, imageUrl: String): Flow<OkResponse> = flow {
        emit(api.updateUserImage(email,imageUrl))
    }.flowOn(Dispatchers.IO)

    fun updateProfileImage(userId: String, imageUrl: String): Flow<OkResponse> = flow {
        Log.i("profileImage", "updateProfileImage:called in repo ")
        emit(api.updateProfileImage(userId,imageUrl))
    }.flowOn(Dispatchers.IO)


    //--------------------------------------Get user data ---------------------------------//
    fun getUserData(number: String): Flow<OkResponse> = flow {
        emit(api.getUserData(number))
    }.flowOn(Dispatchers.IO)

    fun sendDropProfileData(data: DropProfileModel): Flow<DropProfileResponseModel> = flow  {
        emit(api.dropProfile(data))
    }.flowOn(Dispatchers.IO)

}




