package com.spint.app.utils

data class ApiResponse<T>(
    val status:String,
    val message:String,
    val data:T,
    val error:String
)


data class AllPingsResponse<T>(
    val success:Boolean,
    val code:Int,
    val data:T,
)
