package com.example.finalapp.screens._6chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.database.Chat
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.TAG
import com.example.finalapp.viewmodels.ChatViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(sentTo: String,navController: NavHostController,chatViewModel: ChatViewModel) {
    val messages by chatViewModel.messagesFromDB.collectAsState()
    val listState = rememberLazyListState()
    val scope= rememberCoroutineScope()
    var saveToDb by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val userNumber= ProfileObject.profile?.number


    LaunchedEffect(messages){
        listState.animateScrollToItem(messages.size)
        chatViewModel.getChat(sentTo)
    }
    Scaffold(
        topBar = { SingleChatTopBar(title = sentTo, navController =navController ) }) {

        Column(modifier = Modifier.padding(it)) {
            LazyColumn(state = listState,reverseLayout=true, modifier = Modifier.weight(1f)) {
                items(items=messages) { message ->
                    MessageItemUI( message.message, message.sentFrom==userNumber)
                }
            }
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
                                                 .size(28.dp).clickable {
                                                     // TODO open the gallery or camera option to pick image from
                                                 }
                                         )
                                     }else {
                                         Text(text = "Send", modifier = Modifier.clickable {
                                             val chat = Chat(
                                                 sentTo = sentTo,
                                                 sentFrom = userNumber!!,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChatTopBar(title: String, navController: NavHostController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        title = {
            Text(
                title,textAlign= TextAlign.Center, modifier = Modifier.fillMaxWidth(0.6f), color = Color( 0xFF000000), fontSize = 20.sp
            )
        },
        navigationIcon = {
            Row(modifier = Modifier.fillMaxWidth(0.2f)) {
                Card(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
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
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Image(
                        painterResource(id = R.drawable.person_new_filled),
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable { navController.navigate(SCREENS.PROFILE.route) }
                            .padding(4.dp)

                    )
                }
            }
        }, actions = {
            Card(
                modifier = Modifier.size(30.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
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
