package com.spint.app.Duel.data



sealed class CallState {
    object Idle : CallState()
    object Searching : CallState()
    object Matching : CallState()
    data class InCall(val channel: String) : CallState()
}

