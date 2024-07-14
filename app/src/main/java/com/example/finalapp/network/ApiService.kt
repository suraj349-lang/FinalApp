package com.example.finalapp.network

import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponseModel
import com.example.finalapp.model.GetDropProfileResponseModel
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.model.ProfileResponse
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.Response
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.model.SingleOfferModel
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
    suspend fun createOffer(@Body offerData:OfferModel):SingleOfferModel
    @POST("/api/v1/dropProfile/postDropProfile")
    suspend fun dropProfile(@Body data:DropProfileModel):DropProfileResponseModel

    @GET("/api/v1/dropProfile/getDropProfile")
    suspend fun getDropProfile():GetDropProfileResponseModel

    @GET("/api/v1/offer")
    suspend fun getAllOffers():OfferResponseModel

    @GET("/api/v1/user")
    suspend fun getUserData(@Query("number") number: String):ProfileResponse

    @GET("/api/v1/auth/updateUserImage")
    suspend fun updateUserImage(@Query("number") number: String, @Query("imageUrl") imageUrl: String): Response

    @Multipart
    @POST("/api/v1/user/uploadImage")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ImageUploadResponse


}