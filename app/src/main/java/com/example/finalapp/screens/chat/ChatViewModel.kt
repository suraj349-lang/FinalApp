package com.example.finalapp.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.database.Chat
import com.example.finalapp.database.ChatDatabaseRepository
import com.example.finalapp.utils.Constants.Constants
import com.example.finalapp.utils.Constants.Constants.TAG
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.net.URISyntaxException
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatDatabaseRepository: ChatDatabaseRepository
): ViewModel() {

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages
    private val _messagesFromDB = MutableStateFlow<List<Chat>>(emptyList())
    val messagesFromDB: StateFlow<List<Chat>> = _messagesFromDB

    private var socket: Socket? = null
//    val datastore= StoreUserData( )
//    private val _userNumber = MutableStateFlow<String?>(null)
//    val userNumber: StateFlow<String?> = _userNumber
    init {
        connectToSocket()
       // collectUserNumber()
    }
//    private fun collectUserNumber() {
//        viewModelScope.launch {
//            datastore.getUserNumber
//                .collect {
//                    _userNumber.value = it
//                }
//        }
//    }



// this function connects to the dynamic socket created on the basis of number of the user to which message is being sent.
    private fun connectToSocket() {

        try {
            socket = IO.socket(Constants.BASE_URL)
            socket?.connect()

            socket?.on("+916376099670") { data ->
                val jsonString = data[0].toString()

                val chatMessage = Gson().fromJson(jsonString, Chat::class.java)
                viewModelScope.async{
                    saveChatToDB(chatMessage)
                }.invokeOnCompletion {
                    if (chatMessage.message.isNotEmpty()) {
                        val message = chatMessage.message
                        _messages.value = _messages.value + message
                    }
                }
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }


// this function connects to the main socket of individual chat
    fun sendMessage(chat: Chat) {
//        val userData= UserData(number = userNumber ,message=message)
    Log.d(TAG, "sendMessage: called $chat")
        socket?.emit("chatMessage", Json.encodeToString(serializer(),chat))
    }
    suspend fun saveChatToDB(chat: Chat): Boolean {
        return try {
            // Attempt to save the chat
           viewModelScope.launch {
                chatDatabaseRepository.saveChat(chat)
               Log.d(TAG, "saveChatToDB: $chat")
            }
            true
        } catch (e: Exception) {
            Log.d(TAG, "saveChatToDB: ${e.printStackTrace()}")
            Log.d(TAG, "saveChatToDB: ${e.message}")
            false
        }
    }

    suspend fun getChat(userNumber: String):List<Chat> {
        return try {
            viewModelScope.launch {
                chatDatabaseRepository.getChat(userNumber)
            }
            _messagesFromDB.value
        }catch (e:java.lang.Exception){
            Log.d(TAG, "getChat: ${e.printStackTrace()}")
            emptyList<Chat>()
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