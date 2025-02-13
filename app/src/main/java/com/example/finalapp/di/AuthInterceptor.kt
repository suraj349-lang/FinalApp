package com.example.finalapp.di
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
//
//class AuthInterceptor(private val token: String) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//        val originalUrl = originalRequest.url.toString()
//
//        // Check if the request URL is for S3 upload
//        val isS3Upload = originalUrl.contains("s3.amazonaws.com") // Adjust this condition as needed
//
//        val requestBuilder = originalRequest.newBuilder()
//
//        if (isS3Upload) {
//            // Remove the Authorization header for S3 upload requests
//            requestBuilder.removeHeader("Authorization")
//        } else {
//            // Add the Authorization header for other requests
//            requestBuilder.addHeader("Authorization", "Bearer $token")
//        }
//
//        val newRequest = requestBuilder.build()
//        return chain.proceed(newRequest)
//    }
//}


class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        Log.d("AuthInterceptor", "Adding token: Bearer $token")
        val authenticatedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}