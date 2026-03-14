package com.spint.app.Duel.data



data class MatchRequest(val userId: String)
data class EndSessionRequest(val sessionId: String)

data class MatchResponse(
    val status: String,
    val sessionId: String?,
    val channelName: String?,
    val expiresIn: Int?,
    val users: List<UserToken>?
)

data class UserToken(
    val userId: String,
    val token: String
)


