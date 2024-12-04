package com.example.finalapp.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.database.Chat
import com.example.finalapp.repository.ChatDatabaseRepository
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.TAG
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.net.URISyntaxException
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatDatabaseRepository: ChatDatabaseRepository
): ViewModel() {



    private val _messagesFromDB = MutableStateFlow<List<Chat>>(emptyList())
    val messagesFromDB: StateFlow<List<Chat>> = _messagesFromDB
    val loggedInNumber= mutableStateOf("")

    private var socket: Socket? = null
//
//    init {
//     connectToSocket()
//    }




// this function connects to the dynamic socket created on the basis of number of the user to which message is being sent.
       fun connectToSocket() {

        try {
            socket = IO.socket(Constants.BASE_URL)
            socket?.connect()

            socket?.on(loggedInNumber.value) { data ->
                Log.d(TAG, "4: connectToSocket: response from server ${loggedInNumber.value} ${data[0]}")
                val jsonString = data[0].toString()
                val chatMessage = Gson().fromJson(jsonString, Chat::class.java)
               // _messagesFromDB.value= _messagesFromDB.value+ chatMessage
                viewModelScope.launch{
                    if (!isDuplicateMessage(chatMessage)) {
                        saveChatToDB(chatMessage)
                        _messagesFromDB.value += chatMessage
                    }
                    Log.d(TAG, "5: connectToSocket: save chat to db $chatMessage")
                }
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }


// this function connects to the main socket of individual chat
    fun sendMessage(chat: Chat) {
    Log.d(TAG, "2: SendMessage: called $chat")
        socket?.emit("chatMessage", Json.encodeToString(serializer(),chat))
    Log.d(TAG, "3: SendMessage:on chatMessage $chat ")
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

    suspend fun getChat(userNumber: String) {
        try {
            viewModelScope.launch {
                chatDatabaseRepository.getChat(userNumber).collect{
                    _messagesFromDB.value=it
                }
            }
        }catch (e:Exception){
            Log.d(TAG, "getChat: ${e.printStackTrace()}")
        }
    }
    private  fun isDuplicateMessage(chat: Chat): Boolean {
        return _messagesFromDB.value.any { it.id == chat.id }
    }



    override fun onCleared() {
        super.onCleared()
        socket?.disconnect()
    }
}
