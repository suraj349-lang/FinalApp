package com.example.finalapp.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.database.Chat
import com.example.finalapp.database.ChatDatabaseRepository
import com.example.finalapp.utils.Constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.net.URISyntaxException
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(private val chatDatabaseRepository: ChatDatabaseRepository): ViewModel() {

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages

    private var socket: Socket? = null

    init {
        connectToSocket()
    }
// this function connects to the dynamic socket created on the basis of number of the user to which message is being sent.
    private fun connectToSocket() {

        try {
            socket = IO.socket(Constants.BASE_URL)
            socket?.connect()

            socket?.on("+9179034") { args ->
                if (args[0] is String) {
                    val message = args[0] as String
                    _messages.value = _messages.value + message
                }
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

// this function connects to the main socket of individual chat
    fun sendMessage(userNumber:String,message: String) {
        val userData= UserData(number = userNumber ,message=message)
        socket?.emit("chatMessage", Json.encodeToString(serializer(),userData))
        //_messages.value=_messages.value+message
    }
    suspend fun saveChatToDB(chat: Chat): Boolean {
        return try {
            // Attempt to save the chat
            viewModelScope.async {
                chatDatabaseRepository.saveChat(chat)
            }.await()

            // Return true if successful
            true
        } catch (e: Exception) {
            // Return false if an exception occurs
            false
        }
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