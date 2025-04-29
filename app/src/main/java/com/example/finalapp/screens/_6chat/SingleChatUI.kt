package com.example.finalapp.screens._6chat

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.database.ChatItem
import com.example.finalapp.model.Message
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.DialogError
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.viewmodels.ChatViewModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(sentTo: String,chatListUserId:String,navController: NavHostController,chatViewModel: ChatViewModel) {

    val listState = rememberLazyListState()
    val scope= rememberCoroutineScope()
    var saveToDb by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val userNumber= ProfileObject.profile?.number
    val profileImage by remember {
        mutableStateOf(chatViewModel.profileImage.value)
    }

    val messages by chatViewModel.messagesFromServer.collectAsState(RequestState.Idle)
    LaunchedEffect(chatViewModel.canFetch.value) {
        if (chatViewModel.canFetch.value) {
            chatViewModel.getAllMessages(ProfileObject.profile?.userId!!, chatListUserId)
            chatViewModel.canFetch.value=false
        }
    }
    LaunchedEffect(key1 = messages){
        when(val state=messages){
            is RequestState.Success->{
                if (state.data.isNotEmpty()) {
                    listState.animateScrollToItem(state.data.lastIndex)
                }
            }
            else ->{}
        }
    }


    Scaffold(
        topBar = { SingleChatTopBar(title = sentTo,profileImage, navController =navController ) }) {

        Column(modifier = Modifier
            .padding(it)
           // .background(Color.Black)
        ) {
            //-------------------------------MESSAGE ITEM ---------------------------------------------------
            when ( messages) {
                is RequestState.Success -> {
                    val messageList=(messages as RequestState.Success<List<Message>>).data
                    LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                        items(items = (messageList)){ message ->
                            MessageItemUI(
                                msg = message.message,
                                sent =message.sent,
                                received =message.received,
                                message.timestamp,
                                isSentByLoggedInUser = message.senderId == ProfileObject.profile?.userId!!
                            )
                        }
                    }
                }
                is RequestState.Loading -> {
                    Box(modifier = Modifier.weight(1f)) {
                        DialogLoading()

                    }
                }
                is RequestState.Error -> {
                    Box(modifier = Modifier.weight(1f)) {
                        DialogError {

                        }
                    }
                }
                else -> {
                    Box(modifier = Modifier.weight(1f)) {}
                }
            }

            //------------------------------------------------------------------------------------------------//


            Row(modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 150.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                        .heightIn(min = 48.dp, max = 150.dp),
                    placeholder = {
                        Text(
                            text = "Type message....",
                            fontSize = 14.sp,
                            modifier = Modifier.align(Alignment.Top)
                        )
                    },
                    trailingIcon={
                                 Row(modifier = Modifier
                                     .wrapContentSize()
                                     .padding(end = 16.dp), verticalAlignment = Alignment.Bottom) {
                                     if(inputText.isEmpty()) {
                                         Image(
                                             painter = painterResource(id = R.drawable.cameranew),
                                             contentDescription = "",
                                             colorFilter = ColorFilter.tint(color = Color.DarkGray),
                                             alignment = Alignment.Center,
                                             modifier = Modifier
                                                 .size(28.dp)
                                                 .clickable {
                                                     navController.navigate(SCREENS.CAMERAX_SCREEN.route)
                                                 }
                                         )
                                     }else {
                                         Text(text = "Send", modifier = Modifier.clickable {
                                             if(inputText.trim().isNotEmpty()) {
                                                 val tempId = System.currentTimeMillis().toString()
                                                 val newChat = ChatItem(
                                                     id = tempId,
                                                     message = inputText.trim(),
                                                     sentFrom = ProfileObject.profile?.userId!!,
                                                     sentTo = chatListUserId,
                                                     sent = 0,
                                                     received = false,
                                                     seen = false
                                                 )
                                                 scope.launch {
                                                     /*
                                                 saveToDb = chatViewModel.saveChatToDB(chat)

                                                 Log.d("socketManager", "1: ChatScreenUI:$saveToDb ------->  $chat ")

                                                 if (saveToDb) {
                                                     chatViewModel.sendMessage(
                                                         userNumber,
                                                         sentTo,
                                                         inputText.ifEmpty {
                                                             "testing"
                                                         }
                                                     )
                                                     inputText = ""
                                                 }
                                                 */
                                                     chatViewModel.sendMessage(newChat)
                                                     inputText = ""

                                                 }
                                             }
                                         },fontFamily = DONGLE_BOLD, fontSize = 24.sp, color = Color.DarkGray)
                                     }
                                 }
                    },
                    textStyle = TextStyle(fontSize = 14.sp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines=10,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.DarkGray, unfocusedBorderColor = Color.DarkGray, cursorColor = Color.Red)
                )
            }
        }
    }
}
/*
 Card(modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        val chat = Chat(
                            sentTo = sentTo!!, //sentTo
                            sentFrom = userNumber!!,//loggedInNumber.toString(),
                            message = inputText,
                            received = false,
                            sent = 0,
                            seen = false
                        )
                        scope.launch {
                            saveToDb = chatViewModel.saveChatToDB(chat)

                            Log.d("socketManager", "1: ChatScreenUI:$saveToDb ------->  $chat ")

                            if (saveToDb) {
                                chatViewModel.sendMessage(
                                    userNumber,
                                    sentTo,
                                    inputText.ifEmpty {
                                        "testing"
                                    }
                                )
                                inputText = ""
                            }
                        }
                    }, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.chat_new),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(color = Color.DarkGray),
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(2.dp)
                        )
                    }

                }
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun SingleChatTopBar(title: String,profileImage:String, navController: NavHostController) {
    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.Black
//        ),
        title = {
            Text(
                title,textAlign= TextAlign.Start, modifier = Modifier.fillMaxWidth(0.6f), fontFamily = DONGLE_BOLD,color = floatingActionBtnColor, fontSize = 30.sp
            )
        },
        navigationIcon = {
            Row(modifier = Modifier.fillMaxWidth(0.2f)) {
                Card(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Image(
                        painterResource(id = R.drawable.back),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                        modifier = Modifier
                            .clickable { navController.navigate(SCREENS.PROFILE.route) }
                            .padding(2.dp)

                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Card(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    GlideImage(
                        model =  imagePrefix + profileImage ,
                        contentDescription = "",
                        contentScale=ContentScale.Crop,
                        modifier = Modifier.clickable { navController.navigate(SCREENS.PROFILE.route) })
                }
            }
        }, actions = {
            Card(
                modifier = Modifier.size(30.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Image(
                    painterResource(id = R.drawable.menu),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.DarkGray),
                    modifier = Modifier
                        .clickable { navController.navigate(SCREENS.PROFILE.route) }
                        .padding(8.dp)

                )
            }

        }
    )
}
