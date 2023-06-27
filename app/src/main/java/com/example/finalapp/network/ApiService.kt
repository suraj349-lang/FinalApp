package com.example.finalapp.network

import com.example.finalapp.model.ApiResponse
import com.example.finalapp.model.LoginAPIResponse
import com.example.finalapp.model.LoginModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/api/v1/auth/login")
    suspend fun postData(@Body post:LoginModel): LoginAPIResponse




    companion object{
        var apiService:ApiService? = null
        fun getInstance():ApiService{
            if(apiService==null){
                apiService= Retrofit.Builder().baseUrl("http://10.0.2.2:5000/").addConverterFactory(
                    GsonConverterFactory.create()).build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }

}