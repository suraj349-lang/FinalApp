package com.spint.app.Duel.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WebRtcApi {

    @POST("/findMatch")
    suspend fun findMatch(@Body request: MatchRequest): MatchResponse

    @POST("endSession")
    suspend fun endSession(@Body request: EndSessionRequest): Response<Unit>
}