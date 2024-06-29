package com.example.finalapp.screens.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.database.Chat
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.screens.HomeTopBar
import com.example.finalapp.utils.Constants.Constants.TAG
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(sentTo: String?,navController: NavHostController,chatViewModel: ChatViewModel) {
    val context= LocalContext.current
    val messages by chatViewModel.messagesFromDB.collectAsState()
    val listState = rememberLazyListState()
    val scope= rememberCoroutineScope()
    var saveToDb by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val datastore=StoreUserData(context)
    val loggedInNumber by datastore.getUserNumber.collectAsState(initial = "")
    chatViewModel.loggedInNumber.value=loggedInNumber.toString()


    LaunchedEffect(messages){
        listState.animateScrollToItem(messages.size)
        chatViewModel.getChat(sentTo.toString())
    }
    LaunchedEffect(key1 = true){
        chatViewModel.connectToSocket()
    }



    Scaffold(
        topBar = { ChatTopBar(title = sentTo.toString(), navController =navController ) }) {

        Column(modifier = Modifier.padding(it)) {
            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(messages) { message ->
                    MessageItem( message.message, message.sentFrom==loggedInNumber.toString())
                }
            }


            Row {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp)
                )
                Button(onClick = {
                    val chat = Chat(
                        sentTo = sentTo.toString(),
                        sentFrom = loggedInNumber.toString(),
                        message = inputText,
                        received = false,
                        sent = 0,
                        seen = false
                    )
                    scope.launch {
                        saveToDb=chatViewModel.saveChatToDB(chat)
                        Log.d(TAG, "1: ChatScreenUI:$saveToDb $chat ")
                        if (saveToDb){
                            chatViewModel.sendMessage(chat)
                            inputText=""
                        }
                    }
                }) {
                    Text("Send")
                }
            }
        }
    }
}



@Composable
fun MessageItem(msg: String,isSentByLoggedInUser:Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = if (isSentByLoggedInUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isSentByLoggedInUser) Color.LightGray else Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .widthIn(max = 240.dp)
            ) {
                Text(
                    text = msg,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    textAlign = if (isSentByLoggedInUser) TextAlign.End else TextAlign.Start,
                    softWrap = true // Enable auto line wrapping
                )
                Text(
                    text = "08:38", // Replace with actual timestamp
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    textAlign = if (isSentByLoggedInUser) TextAlign.End else TextAlign.Start

                )
            }
        }
    }
}
