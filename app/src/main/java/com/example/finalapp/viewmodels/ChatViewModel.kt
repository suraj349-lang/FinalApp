package com.example.finalapp.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.database.Chat
import com.example.finalapp.database.ChatItem
import com.example.finalapp.model.ChatList
import com.example.finalapp.model.Message
import com.example.finalapp.repository.ChatDatabaseRepository
import com.example.finalapp.screens._6chat.SocketManager
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatDatabaseRepository: ChatDatabaseRepository
) : ViewModel() {

    val canFetch = mutableStateOf(true)
    private val _messagesFromDB = MutableStateFlow<List<Chat>>(emptyList())
    val messagesFromDB: StateFlow<List<Chat>> = _messagesFromDB

    private val _messagesFromServer = MutableStateFlow<RequestState<List<Message>>>(RequestState.Idle)
    val messagesFromServer: StateFlow<RequestState<List<Message>>> = _messagesFromServer

    val profileImage = MutableStateFlow<String?>(null)

    private val socketManager by lazy { SocketManager() }

    private var isSocketConnected = false

    fun connectSocket() {
        val userId = UserObject.user.value.user
        if (isSocketConnected) {
            Log.d("SocketManager", "Socket already connected, skipping connect.")
            return
        }

        socketManager.connect(userId) { receivedMessage ->
            Log.d("Messageschat", "connectSocket: $receivedMessage")

            _messagesFromServer.update { oldState ->
                when (oldState) {
                    is RequestState.Success -> {
                        val existingPendingMessage = oldState.data.find {
                            it.message == receivedMessage.message &&
                                    it.senderId == receivedMessage.receiverId &&
                                    it.receiverId == receivedMessage.senderId &&
                                    it.timestamp == receivedMessage.timestamp
                        }

                        if (existingPendingMessage != null) {
                            val updatedChats = oldState.data.map { chat ->
                                if (chat.message == receivedMessage.message &&
                                    chat.senderId == receivedMessage.receiverId
                                ) {
                                    chat.copy(
                                        sent = 1,
                                        received = true,
                                        timestamp = receivedMessage.timestamp
                                    )
                                } else chat
                            }
                            RequestState.Success(updatedChats)
                        } else {
                            RequestState.Success(oldState.data + receivedMessage)
                        }
                    }
                    else -> RequestState.Success(listOf(receivedMessage))
                }
            }
        }

        isSocketConnected = true
    }

    fun disconnectSocket() {
        socketManager.disconnect()
        isSocketConnected = false
    }

    fun sendMessage(newChatItem: ChatItem) {
        Log.d(
            "SocketManager",
            "sendMessage in viewmodel: ${newChatItem.sentFrom}, ${newChatItem.sentTo}, ${newChatItem.message}"
        )
        socketManager.sendMessage(newChatItem)
    }

    fun getAllMessages(userID: String, otherUserID: String) = viewModelScope.launch {
        Log.d("Messageschat", "getAllMessages: called in viewmodel")
        chatDatabaseRepository.getMessages(userID, otherUserID)
            .onStart {
                _messagesFromServer.value = RequestState.Loading
            }
            .catch {
                Log.e("Messageschat", "getAllMessages:${it.message}", it)
                _messagesFromServer.value = RequestState.Error(it)
            }
            .collect { response ->
                Log.d("Messageschat", "getAllMessages: $response")
                _messagesFromServer.update { oldState ->
                    when (oldState) {
                        is RequestState.Success -> {
                            val combined = (oldState.data + response.data).distinctBy { it.timestamp }
                            RequestState.Success(combined)
                        }
                        else -> RequestState.Success(response.data)
                    }
                }
            }
    }

    fun resetProfileImage(){
        profileImage.value=""
    }





//    // this function connects to the dynamic socket created on the basis of number of the user to which message is being sent.
//    fun connectToSocket() {
//
//        try {
//            socket = IO.socket(Constants.TEMP_SOCKET_URL)
//            socket?.connect()
//
//            socket?.on(loggedInNumber.value) { data ->
//                Log.d(
//                    TAG,
//                    "4: connectToSocket: response from server ${loggedInNumber.value} ${data[0]}"
//                )
//                val jsonString = data[0].toString()
//                val chatMessage = Gson().fromJson(jsonString, Chat::class.java)
//               // _messagesFromDB.value= _messagesFromDB.value+ chatMessage
//                viewModelScope.launch{
//                    if (!isDuplicateMessage(chatMessage)) {
//                        saveChatToDB(chatMessage)
//                        _messagesFromDB.value += chatMessage
//                    }
//                    Log.d(TAG, "5: connectToSocket: save chat to db $chatMessage")
//                }
//            }
//        } catch (e: URISyntaxException) {
//            e.printStackTrace()
//        }
//    }




//----------------------------------------------Database Chat -----------------------------------------------------------------//
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

    suspend fun getChat(sentTo: String) {
        try {
            viewModelScope.launch {
                chatDatabaseRepository.getChat(sentTo).collect{
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


    //------------------------------------------------Chat list ----------------------------------------------------

    private val _getUserChatList = MutableStateFlow<RequestState<List<ChatList>>>(RequestState.Idle)
    val getUserChatList: StateFlow<RequestState<List<ChatList>>> = _getUserChatList;
    fun getUserChatList(userID: String) = viewModelScope.launch(Dispatchers.IO) {
        chatDatabaseRepository.getUserChatList(userID)
            .onStart {
                _getUserChatList.value = RequestState.Loading
            }.catch {
                _getUserChatList.value = RequestState.Error(it)
            }.collect {
                _getUserChatList.value = RequestState.Success(it.data)
            }
    }

    //-------------------------------------------------------------------------------------------//


    override fun onCleared() {
        super.onCleared()
        socketManager.disconnect() // Ensure socket disconnects when ViewModel is destroyed
    }

}
