package com.example.finalapp

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.utils.Constants.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.net.URISyntaxException

class ChatViewModel() : ViewModel() {
    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    private var socket: Socket? = null

//    val dataStore=StoreUserData(context = Context.)
//    val userNumber=dataStore.getUserNumber

    init {
        connectToSocket()
    }

    private fun connectToSocket() {
        try {
            socket = IO.socket(Constants.BASE_URL)
            socket?.connect()

            socket?.on("+916376099670") { args ->
                if (args[0] is String) {
                    val message = args[0] as String
                    _messages.value = _messages.value + message
                }
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }


    fun sendMessage(number:String,message: String) {
        val userData=UserData(number = number,message=message)
        socket?.emit("chatMessage", Json.encodeToString(serializer(),userData))
       // _messages.value=_messages.value+message
    }


    override fun onCleared() {
        super.onCleared()
        socket?.disconnect()
    }
}
@Serializable
data class UserData(
    val number: String,
    val message: String
)