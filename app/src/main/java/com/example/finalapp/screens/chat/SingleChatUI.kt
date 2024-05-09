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
fun ChatScreenUI(userNumber: String?,navController: NavHostController,chatViewModel: ChatViewModel) {
    val context= LocalContext.current
    val messages by chatViewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    var key by remember {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()
    var saveToDb by remember {
        mutableStateOf(false)
    }
    var inputText by remember { mutableStateOf("") }
    val datastore=StoreUserData(context )
    val number by  datastore.getUserNumber.collectAsState("")
    Log.d(TAG, "ChatScreenUI: $number")





    Scaffold(
        topBar = {
           ChatTopBar(title = userNumber.toString(), navController =navController )
    }) {

        // Auto-scroll to the bottom when messages list is updated
        LaunchedEffect(messages) {
            listState.animateScrollToItem(messages.size)
        }

        if (key == 1) {

            LaunchedEffect(key1 = true) {
            scope.launch {
                saveToDb=chatViewModel.saveChatToDB(
                    Chat(
                        sentTo = userNumber.toString(),
                        sentFrom="",
                        message = inputText,
                        received = false,
                        sent = 0,
                        seen = false,
                       // timeStamp = System.currentTimeMillis()
                    )
                )
            }
                key=0
        }
    }

        Column(modifier = Modifier.padding(it)) {
            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(messages) { message ->
                    MessageItem("you", message,false)
                }
            }


            Row {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp)
                )
                if(saveToDb) {
                    Log.d(TAG, "ChatScreenUI:saveToDB -> socket sent ")
                    chatViewModel.sendMessage(Chat(
                        id=1,
                        sentTo = "917250260100",
                        sentFrom=number.toString(),
                        message = inputText,
                        received = false,
                        sent = 0,
                        seen = false,
                      //  timeStamp = System.currentTimeMillis()
                    ))
                    inputText = ""
                    saveToDb=false
                }
                Button(onClick = {
                    key=1
//                    chatViewModel.messages.value
//                    inputText = ""
                }) {
                    Text("Send")
                }
            }
        }
    }
}



@Composable
fun MessageItem(name: String, msg: String, isSentByUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        contentAlignment = if (isSentByUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isSentByUser) Color.LightGray else Color.White
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
                    textAlign = if (isSentByUser) TextAlign.End else TextAlign.Start,
                    softWrap = true // Enable auto line wrapping
                )
                Text(
                    text = "08:38", // Replace with actual timestamp
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    textAlign = if (isSentByUser) TextAlign.End else TextAlign.Start

                )
            }
        }
    }
}
