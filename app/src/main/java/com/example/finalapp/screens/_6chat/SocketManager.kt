package com.example.finalapp.screens._6chat

import android.util.Log
import com.example.finalapp.database.Chat
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


    fun connect(userNumber:String,onMessageReceived:(Chat)->Unit) {
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
                onMessageReceived(Chat( sentTo=receiverId, sentFrom = senderId, message = message,sent=1, received = false, seen = false));
                Log.d("SocketManager", "Message from $senderId: $message")
                // Notify your ViewModel or UI about the new message
            }
        }
        mSocket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("SocketManager", "Connection error: ${args[0]}")
        }
    }

    fun sendMessage(senderId: String, receiverId: String, message: String) {
        val messageData = JSONObject()
        messageData.put("senderId", senderId)
        messageData.put("receiverId", receiverId)
        messageData.put("message", message)
        Log.d("SocketManager", "Emitting sendMessage event with data: $messageData")
        mSocket?.emit("sendMessage", messageData)
        Log.d("SocketManager", "sendMessage event emitted")
    }



    fun disconnect() {
        mSocket?.disconnect()
    }
}