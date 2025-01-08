package com.example.finalapp.utils

data class ApiResponse<T>(
    val status:String,
    val message:String,
    val data:T,
    val error:String
)
