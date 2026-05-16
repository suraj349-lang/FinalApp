package com.spint.app.Duel.data

import com.spint.app.utils.constants.Constants


object SocketManager {


    val socket = io.socket.client.IO.socket(Constants.SIGNAL_SERVER)

    fun connect() {
        if (!socket.connected()) {
            socket.connect()
        }
    }

    fun disconnect() {
        socket.disconnect()
    }
}
