package com.example.finalapp.network

import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.ProfileResponse
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.Response
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.model.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/api/v1/auth/login")
    suspend fun postLoginData(@Body loginData:LoginModel): LoginAPIResponse
    @POST("/api/v1/auth/register")
    suspend fun postSignupData(@Body signupData:RegisterUserModel): SignupAPIResponse

    @POST("/api/v1/offer")
    suspend fun createOffer(@Body offerData:OfferModel):OfferResponseModel

    @GET("/api/v1/user/all")
    suspend fun getAllProfiles():ProfileResponse

    @GET("/api/v1/user")
    suspend fun getUserData():ProfileResponse

    @GET("/api/v1/auth/updateUserImage")
    suspend fun updateUserImage(@Query("number") number: String, @Query("imageUrl") imageUrl: String): Response

    @Multipart
    @POST("/api/v1/user/uploadImage")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ImageUploadResponse


}