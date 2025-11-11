package com.spint.app.utils

sealed class LoginState<out T>{
    object Idle:LoginState<Nothing>()
    object Loading: LoginState<Nothing>()
    data class Success<out T>(val data:T) :LoginState<T>()
    data class Error(val error:String) :LoginState<Nothing>()
}
