package com.spint.app

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
