package com.example.finalapp

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class OtpSent(val phone: String) : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

interface AuthProvider {
    suspend fun signUpWithState(
        context: Context,
        identifier: String? = null,
        otp: String? = null,
        extraData: Map<String, String>? = null
    ): AuthState

    suspend fun loginWithState(
        context: Context,
        identifier: String? = null,
        otp: String? = null
    ): AuthState

    suspend fun logout(context: Context): Boolean
    fun getProviderName(): String
}

class FirebasePhoneAuthProvider : AuthProvider {
    override fun getProviderName() = "FirebasePhone"
    override suspend fun signUpWithState(
        context: Context,
        identifier: String?,
        otp: String?,
        extraData: Map<String, String>?
    ): AuthState {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithState(
        context: Context,
        identifier: String?,
        otp: String?
    ): AuthState {
        TODO("Not yet implemented")
    }


    override suspend fun logout(context: Context): Boolean {
        FirebaseAuth.getInstance().signOut()
        return true
    }
}
class Msg91AuthProvider : AuthProvider {
    override fun getProviderName() = "Msg91"

    override suspend fun signUpWithState(
        context: Context,
        identifier: String?,
        otp: String?,
        extraData: Map<String, String>?
    ): AuthState {
        return if (otp == null) {
            // Step 1: Send OTP
            // API call -> if success
            AuthState.OtpSent(identifier ?: "")
        } else {
            // Step 2: Verify OTP with backend
            if (otp == "123456") AuthState.Success
            else AuthState.Error("Invalid OTP")
        }
    }

    override suspend fun loginWithState(context: Context, identifier: String?, otp: String?): AuthState {
        return if (otp == null) {
            AuthState.OtpSent(identifier ?: "")
        } else {
            if (otp == "123456") AuthState.Success
            else AuthState.Error("Invalid OTP")
        }
    }

    override suspend fun logout(context: Context) = true
}


class GoogleAuthProvider : AuthProvider {
    override fun getProviderName() = "Google"
    override suspend fun signUpWithState(
        context: Context,
        identifier: String?,
        otp: String?,
        extraData: Map<String, String>?
    ): AuthState {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithState(
        context: Context,
        identifier: String?,
        otp: String?
    ): AuthState {
        TODO("Not yet implemented")
    }


    override suspend fun logout(context: Context): Boolean {
        // Google sign out logic
        return true
    }
}
class AuthManager(private var provider: AuthProvider) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun setProvider(newProvider: AuthProvider) {
        provider = newProvider
    }

    suspend fun signUp(context: Context, identifier: String? = null, otp: String? = null, extraData: Map<String, String>? = null) {
        _authState.value = AuthState.Loading
        val state = provider.signUpWithState(context, identifier, otp, extraData)
        _authState.value = state
    }

    suspend fun login(context: Context, identifier: String? = null, otp: String? = null) {
        _authState.value = AuthState.Loading
        val state = provider.loginWithState(context, identifier, otp)
        _authState.value = state
    }

    suspend fun logout(context: Context) {
        provider.logout(context)
        _authState.value = AuthState.Idle
    }
}
