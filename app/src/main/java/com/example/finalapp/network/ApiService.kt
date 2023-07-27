package com.example.finalapp.network

import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.model.SignupAPIResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/v1/auth/login")
    suspend fun postLoginData(@Body loginData:LoginModel): LoginAPIResponse
    @POST("/api/v1/auth/register")
    suspend fun postSignupData(@Body signupData:RegisterUserModel): SignupAPIResponse




    companion object{
        var apiService:ApiService? = null
        fun getInstance():ApiService{
            if(apiService==null){
                apiService= Retrofit.Builder().baseUrl("http://192.168.29.95:5000/").addConverterFactory(
                    GsonConverterFactory.create()).build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }

}