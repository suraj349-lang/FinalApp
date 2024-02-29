package com.example.finalapp

import androidx.lifecycle.ViewModel
import com.example.finalapp.utils.Constants.Constants
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.URISyntaxException

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    private var socket: Socket? = null

    init {
        connectToSocket()
    }

    private fun connectToSocket() {
        try {
            socket = IO.socket(Constants.BASE_URL)
            socket?.connect()

            socket?.on("chatMessage") { args ->
                if (args[0] is String) {
                    val message = args[0] as String
                    _messages.value = _messages.value + message
                }
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun sendMessage(message: String) {
        socket?.emit("chatMessage", message)
    }

    override fun onCleared() {
        super.onCleared()
        socket?.disconnect()
    }
}
