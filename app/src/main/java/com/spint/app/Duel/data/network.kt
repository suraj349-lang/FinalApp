package com.spint.app.Duel.data


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
