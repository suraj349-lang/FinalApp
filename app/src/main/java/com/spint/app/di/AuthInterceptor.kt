package com.spint.app.di
import android.util.Log
import com.spint.app.utils.TokenObject
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        Log.d("AuthInterceptor", "Adding token: Bearer $token")
        val localToken: String = token.ifEmpty { TokenObject.token }
        val authenticatedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $localToken")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}