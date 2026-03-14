package com.spint.app.Duel.data

import com.spint.app.utils.constants.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.40:5002/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val webRtcApi: WebRtcApi = retrofit.create(WebRtcApi::class.java)
}

object SocketManager {

    private const val SERVER_URL = "http://192.168.1.40:5002"

    val socket = io.socket.client.IO.socket(SERVER_URL)

    fun connect() {
        if (!socket.connected()) {
            socket.connect()
        }
    }

    fun disconnect() {
        socket.disconnect()
    }
}
