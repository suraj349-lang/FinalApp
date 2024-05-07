package com.example.finalapp.screens.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.database.Chat
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.screens.HomeTopBar
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(userNumber: String?,navController: NavHostController,viewModel: ChatViewModel) {
    val context= LocalContext.current

    val authViewModel = hiltViewModel<AuthViewModel>()
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    var key by remember {
        mutableStateOf(0)
    }
    var inputText by remember { mutableStateOf("") }
    val datastore=StoreUserData(context )
//    val number by  datastore.getUserNumber.collectAsState("")
//    LaunchedEffect(key1 = true){
//        datastore.saveUserNumber("+9179034")
//    }




    Scaffold(topBar = { HomeTopBar(
        title = userNumber!!,
        navController = navController,
        navIcon =false ,
        actionIcon =false,
        icon = R.drawable.profile_image_1
    )
    }) {

        // Auto-scroll to the bottom when messages list is updated
        LaunchedEffect(messages) {
            listState.animateScrollToItem(messages.size)
        }
        val scope = rememberCoroutineScope()
//        if (key == 1) {
//
//            LaunchedEffect(key1 = true) {
//            scope.launch {
//                chatViewModel.saveChatToDB(
//                    Chat(
//                        sentTo = userNumber.toString(),
//                        message = inputText,
//                        received = "false",
//                        sent = "Single Tick",
//                        seen = "false",
//                        timeStamp = System.currentTimeMillis()
//                    )
//                )
//            }
//                key=0
//        }
//    }

        Column(modifier = Modifier.padding(it)) {
            LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                items(messages) { message ->
                    MessageItem("you", message,navController)
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
                    key=1
                    viewModel.sendMessage(userNumber!!,inputText)
                    viewModel.messages.value
                    inputText = ""
                }) {
                    Text("Send")
                }
            }
        }
    }
}

@Composable
fun MessageItem(name:String,msg:String,navController: NavHostController){
    Card(modifier = Modifier
        .padding(start = 8.dp, top = 4.dp)
        .wrapContentSize(),
        shape= RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        border = BorderStroke(1.dp, Color.DarkGray)

    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text =name, modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.titleMedium)
            Text(text =msg, modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.titleMedium)
        }



    }
}