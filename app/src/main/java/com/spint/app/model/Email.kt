package com.spint.app.model

data class Email(
    val email: String
)
data class VerifyEmailOtp(
    val email: String,
    val otp: String
)

data class ResponseOfEmail(
    val success: Boolean,
    val message: String
)