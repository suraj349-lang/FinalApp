package com.example.finalapp.fcm.screen

import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.finalapp.fcm.dialog.EnterTokenDialog
import com.example.finalapp.fcm.viewmodel.ChatViewModel

@Composable
fun ChatScreenUIFCM(){
     val viewModel= ChatViewModel()

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