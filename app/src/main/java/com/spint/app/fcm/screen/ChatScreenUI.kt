package com.spint.app.fcm.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spint.app.fcm.dialog.EnterTokenDialog
import com.spint.app.viewmodels.FCMViewModel

@Composable
fun ChatScreenUIFCM(){
     val viewModel= FCMViewModel()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        val state = viewModel.state
        if(state.isEnteringToken) {
            EnterTokenDialog(
                token = state.remoteToken,
                onTokenChange = viewModel::onRemoteTokenChange,
                onSubmit = viewModel::onSubmitRemoteToken
            )
        } else {
            ChatScreen(
                messageText = state.messageText,
                onMessageSend = {
                    viewModel.sendMessage(isBroadcast = false)
                },
                onMessageBroadcast = {
                    viewModel.sendMessage(isBroadcast = true)
                },
                onMessageChange = viewModel::onMessageChange
            )
        }
    }
}