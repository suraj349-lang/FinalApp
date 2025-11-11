package com.spint.app.fcm.api

import com.spint.app.fcm.stateObject.SendMessageDto
import com.spint.app.fcm.stateObject.SendTokenDto
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