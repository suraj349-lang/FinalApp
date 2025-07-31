package com.example.finalapp.screens._6chat

import android.util.Log
import com.example.finalapp.database.ChatItem
import com.example.finalapp.model.Message
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.constants.Constants
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketManager {
    private var mSocket: Socket? = null

    init {
        try {
//            val options = IO.Options()
//            options.path = "/socket.io" // Adjust the path if necessary
            mSocket = IO.socket(Constants.TEMP_SOCKET_URL)
        } catch (e: Exception) {
            Log.e("SocketManager", "Error initializing socket: ${e.message}")
        }
    }


    fun connect(userNumber:String,onMessageReceived:(Message)->Unit) {
        mSocket?.connect()
        mSocket?.on(Socket.EVENT_CONNECT) {
            Log.d("SocketManager", "Connected to server")
            // Emit an event to join a specific room or namespace if needed
            mSocket?.emit("join", userNumber)
        }
        mSocket?.on("receiveMessage") { args ->
            if (args.isNotEmpty()) {
                val messageData = args[0] as JSONObject
                // Process the received message
                val senderId = messageData.getString("senderId")
                val receiverId = messageData.getString("receiverId")
                val message = messageData.getString("message")
                val timestamp=messageData.getString("timestamp")
                onMessageReceived(Message(senderId,receiverId,message,timestamp));
                Log.d("SocketManager", "Message from $senderId: $message")
            }
        }
        mSocket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("SocketManager", "Connection error: ${args[0]}")
        }
    }

    fun sendMessage(newChatItem: ChatItem) {
        val messageData = JSONObject()
        messageData.put("senderId", newChatItem.sentFrom)
        messageData.put("receiverId", newChatItem.sentTo)
        messageData.put("message", newChatItem.message)
        messageData.put("senderUserName",UserObject.user.value.username)
        Log.d("SocketManager", "Emitting sendMessage event with data: $messageData")
        mSocket?.emit("sendMessage", messageData)
        Log.d("SocketManager", "sendMessage event emitted")
    }



    fun disconnect() {
        mSocket?.disconnect()
    }
}