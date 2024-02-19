package com.example.finalapp.fcm.api

import com.example.finalapp.fcm.stateObject.SendMessageDto
import com.example.finalapp.fcm.stateObject.SendTokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/notification/token")
    suspend fun sendToken(@Body body: SendTokenDto)

    @POST("/notification/updateToken")
    suspend fun updateToken(@Body body: SendTokenDto)

    @POST("/notification/send")
    suspend fun sendMessage(@Body body: SendMessageDto)

    @POST("/notification/broadcast")
    suspend fun broadcast(@Body body: SendMessageDto)

}