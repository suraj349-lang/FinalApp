package com.spint.app.repository

import android.util.Log
import com.spint.app.model.PreSignedUrlResponse
import com.spint.app.model.DropProfileModel
import com.spint.app.model.DropProfileResponseModel
import com.spint.app.model.OkResponse
import com.spint.app.network.ApiService
import com.spint.app.network.NonAuthApiService
import com.spint.app.utils.ApiResponse
import com.spint.app.utils.UserObject
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
            Log.e("S3 Upload", "Failed with status: ${response}")
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
    fun getUserData(userId: String): Flow<OkResponse> = flow {
        emit(api.getUserData(userId))
    }.flowOn(Dispatchers.IO)

    //--------------------------------------Get user data ---------------------------------//
    fun updateUserData(id: String, backgroundImage: Map<String, String>): Flow<OkResponse> = flow {
        emit(api.updateUserData(id,backgroundImage))
    }.flowOn(Dispatchers.IO)

    fun sendDropProfileData(data: DropProfileModel): Flow<DropProfileResponseModel> = flow  {
        emit(api.dropProfile(data))
    }.flowOn(Dispatchers.IO)

    //-----------------------------------------------------------------------------------------

    fun updateName(name: String): Flow<ApiResponse<String>> = flow {
        emit(api.updateName(UserObject.user.value.user,name))
    }.flowOn(Dispatchers.IO)
    fun updateUserName(userName: String): Flow<ApiResponse<String>> = flow {
        emit(api.updateUserName(UserObject.user.value.user,userName))
    }.flowOn(Dispatchers.IO)

}




