package com.example.finalapp.viewmodels

import android.util.Log
import android.view.PixelCopy.Request
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalapp.database.Chat
import com.example.finalapp.model.ChatList
import com.example.finalapp.repository.ChatDatabaseRepository
import com.example.finalapp.screens._6chat.SocketManager
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(private val chatDatabaseRepository: ChatDatabaseRepository): ViewModel() {


    private val _messagesFromDB = MutableStateFlow<List<Chat>>(emptyList())
    val messagesFromDB: StateFlow<List<Chat>> = _messagesFromDB
    private val _messagesFromServer = MutableStateFlow<RequestState<List<Chat>>>(RequestState.Idle)
    val messagesFromServer: StateFlow<RequestState<List<Chat>>> = _messagesFromServer
    val loggedInNumber = mutableStateOf("")
    val profileImage= mutableStateOf("")

    private val socketManager by lazy { SocketManager()}
    fun connectSocket() {
//        socketManager.connect(ProfileObject.profile?.userId!!) { newChat ->
//
//            val currentState = _messagesFromServer.value
//
//            if (currentState is RequestState.Success) {
//                val updatedList = currentState.data.toMutableList().apply {
//                    add(newChat)
//                }
//                _messagesFromServer.value = RequestState.Success(updatedList)
//            } else {
//                _messagesFromServer.value = RequestState.Success(listOf(newChat))
//            }
//
//        }
        socketManager.connect(ProfileObject.profile?.userId!!) { receivedChat ->
            _messagesFromServer.update { oldState ->
                when (oldState) {
                    is RequestState.Success -> {
                        val updatedChats = oldState.data.map { chat ->
                            if (chat.message == receivedChat.message && chat.sent == 0) {
                                chat.copy(sent = 1, received = true)
                            } else chat
                        }
                        RequestState.Success(updatedChats)
                    }
                    else -> oldState
                }
            }
        }

    }

    fun sendMessage(senderId: String, receiverId: String, message: String) {
        Log.d("SocketManager", "sendMessage in viewmodel:called ${senderId},${receiverId},${message} ")
        socketManager.sendMessage(senderId, receiverId, message)
    }
    private val _messages=MutableStateFlow<RequestState<List<ChatList>>>(RequestState.Idle)
    val messages:StateFlow<RequestState<List<ChatList>>> =_messages
    fun getAllMessages(userID: String,otherUserID:String)=viewModelScope.launch {
         chatDatabaseRepository.getMessages(userID,otherUserID)
             .onStart {
                 _messages.value=RequestState.Loading
             }.catch {
                 _messages.value=RequestState.Error(it)
             }.collect { response ->
                 if(response.status.uppercase()=="SUCCESS") {
                     _messages.value = RequestState.Success(response.data)
                 }else{
                     _messages.value=RequestState.Error(Throwable("Error getting chats"))
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

    fun disconnectSocket() {
        socketManager.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        socketManager.disconnect() // Ensure socket disconnects when ViewModel is destroyed
    }

}
