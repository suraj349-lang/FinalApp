package com.spint.app.screens._6chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.database.ChatItem
import com.spint.app.model.Message
import com.spint.app.navigation.SCREENS
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserObject
import com.spint.app.utils.RequestState
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.ChatViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleChatScreenUI(
    sentTo: String,
    chatListUserId: String,
    navController: NavHostController,
    chatViewModel: ChatViewModel
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as Activity
    val user by UserObject.user.collectAsState()

    var isListening by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
    var showLinearIndicator by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val messages by chatViewModel.messagesFromServer.collectAsState(RequestState.Idle)
    val recordAudioPermission = Manifest.permission.RECORD_AUDIO
    val hasMicPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                recordAudioPermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val chatUserImage by chatViewModel.profileImage.collectAsState()


    // Request mic permission on first launch
    LaunchedEffect(Unit) {
        if (!hasMicPermission.value) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }


    LaunchedEffect(Unit) {
        if (!hasMicPermission.value) {
            ActivityCompat.requestPermissions(activity, arrayOf(recordAudioPermission), 0)
        }
    }


    val voiceRecognizer = remember {
        VoiceRecognizerHelper(
            context,
            onResult = {
                inputText = it
                isListening = false
                isRecording = false
            },
            onStart = {
                isListening = true
            },
            onEnd = {
                isListening = false
                isRecording = false
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            voiceRecognizer.destroy()
        }
    }

    // Fetch messages on startup or trigger
    LaunchedEffect(chatViewModel.canFetch.value) {
        if (chatViewModel.canFetch.value) {
            chatViewModel.getAllMessages(user.user, chatListUserId)
            chatViewModel.canFetch.value = false
        }
    }

    // Auto scroll when messages update
    LaunchedEffect(messages) {
        if (messages is RequestState.Success) {
            val list = (messages as RequestState.Success<List<Message>>).data
            if (list.isNotEmpty()) {
                listState.animateScrollToItem(list.lastIndex)
            }
        }
    }
    val systemUiController = rememberSystemUiController()


    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Color.White,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = Constants.HOME_BOTTOM_BAR_COLOR,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }

    Scaffold(
        topBar = { SingleChatTopBar(title = sentTo, chatUserImage, navController) },
        bottomBar = {
            // Input row with text field and mic button
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 150.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 48.dp)
                        .heightIn(min = 48.dp, max = 150.dp),
                    placeholder = {
                        Text(text = "Type message....", fontSize = 14.sp, fontFamily = Constants.FONT_LIGHT, color = Color.Gray)
                    },
                    maxLines = 10,
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Color.Black,
                        letterSpacing = (-0.01f).em
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = floatingActionBtnColor,
                        focusedContainerColor = Color.White.copy(alpha = 0.8f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = Color.Black
                    ),trailingIcon = {
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (inputText.isEmpty()) {
                                // Mic Button with tap & hold functionality
                                MicButton(
                                    isListening = isListening,
                                    isRecording = isRecording,
                                    onTapToListen = {
                                        if (!hasMicPermission.value) return@MicButton
                                        if (!isListening) {
                                            voiceRecognizer.startListening()
                                        } else {
                                            voiceRecognizer.stopListening()
                                        }
                                    },
                                    onHoldToRecordStart = {
                                        if (!hasMicPermission.value) return@MicButton
                                        isRecording = true
                                        voiceRecognizer.startRecording()
                                    },
                                    onHoldToRecordStop = {
                                        voiceRecognizer.stopRecording()
                                        isRecording = false
                                    }
                                )


                                // Camera Icon unchanged
                                Image(
                                    painter = painterResource(id = R.drawable.cameranew),
                                    contentDescription = "Camera",
                                    colorFilter = ColorFilter.tint(color = Color.Black),
                                    alignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clickable {
                                            navController.navigate("camerax/singleChat")
                                        }
                                )
                            } else {
                                // Send button as before
                                Text(
                                    text = "Send",
                                    modifier = Modifier.clickable {
                                        if (inputText.trim().isNotEmpty()) {
                                            val tempId = System.currentTimeMillis().toString()
                                            val newChat = ChatItem(
                                                id = tempId,
                                                message = inputText.trim(),
                                                sentFrom = UserObject.user.value.user ,
                                                sentTo = chatListUserId,
                                                sent = 0,
                                                received = false,
                                                seen = false
                                            )
                                            scope.launch {
                                                chatViewModel.sendMessage(newChat)
                                                inputText = ""
                                            }
                                        }
                                    },
                                    fontFamily = Constants.FONT_MEDIUM,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.whatsapp), contentDescription ="", modifier = Modifier.fillMaxSize(),contentScale = ContentScale.Crop  )
            Column(
                modifier = Modifier
                    .padding(it)
                    .imePadding()
            ) {
                if (showLinearIndicator) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = floatingActionBtnColor
                    )
                }

                if (showError) {
                    NoMessagesScreen(Modifier.weight(1f, true), "Error connecting to server")
                }

                when (messages) {
                    is RequestState.Success -> {
                        showLinearIndicator = false
                        val messageList = (messages as RequestState.Success<List<Message>>).data
                        val sortedMessages = messageList.sortedBy {
                            try {
                                Instant.parse(it.timestamp).toEpochMilli()
                            } catch (e: Exception) {
                                0L
                            }
                        }



                        if (messageList.isNotEmpty()) {

                            LazyColumn(
                                state = listState,
                                modifier = Modifier.weight(1f)
                            ) {
                                itemsIndexed(sortedMessages) { index, message ->
                                    val currentDateText = formatDate(message.timestamp)
                                    val previousDateText =
                                        if (index > 0) formatDate(sortedMessages[index - 1].timestamp)
                                        else null

                                    if (previousDateText != currentDateText) {
                                        DateHeader(text = currentDateText)
                                    }

                                    val isFromUser = message.senderId == user.user
                                    val previousSender = if (index > 0) sortedMessages[index - 1].senderId else null
                                    val isFirstOfBlock = previousSender != message.senderId

                                    MessageItemUI(
                                        msg = message.message,
                                        sent = message.sent,
                                        received = message.received,
                                        timestamp = message.timestamp,
                                        isSentByLoggedInUser = isFromUser,
                                        showTail = isFirstOfBlock
                                    )
                                }

                            }
                        }

                        else {
                            NoMessagesScreen(Modifier.weight(1f, true), "Say hi!")
                        }
                    }

                    is RequestState.Loading -> {
                        showLinearIndicator = true
                    }

                    is RequestState.Error -> {
                        showLinearIndicator = false
                        showError = true
                    }

                    else -> {
                        Box(modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }
    }
}


@Composable
fun DateHeader(text: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text?: "",
            fontSize = 12.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .background(
                    color = Color(0xFFE8E8E8),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 5.dp)
        )
    }
}





@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(timestamp: String?): String? {
    if (timestamp == null) return null

    return try {
        val date = Instant.parse(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val today = LocalDate.now()

        when (date) {
            today -> "Today"
            today.minusDays(1) -> "Yesterday"
            else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }
    } catch (e: Exception) {
        null
    }
}





/*
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
    var isRecording by remember {
        mutableStateOf(false)
    }
    var showLinearIndicator by remember {
        mutableStateOf(false)
    }
    var showError by remember {
        mutableStateOf(false)
    }
    val context= LocalContext.current
    // Blinking red dot animation
    val blinkAnim = rememberInfiniteTransition(label = "Mic Blink").animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Blink Alpha"
    )

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

    val activity = LocalContext.current as Activity
    val recordAudioPermission = Manifest.permission.RECORD_AUDIO

    val hasMicPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                recordAudioPermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

// Request permission if not granted
    LaunchedEffect(Unit) {
        if (!hasMicPermission.value) {
            ActivityCompat.requestPermissions(activity, arrayOf(recordAudioPermission), 0)
        }
    }


    val voiceRecognizer = remember {
        VoiceRecognizerHelper(context, onResult = {
            inputText = it
            isRecording = false
        }, onStart = {
            isRecording = true
        }, onEnd = {
            isRecording = false
        })
    }


    DisposableEffect(Unit) {
        onDispose {
            voiceRecognizer.destroy()
        }
    }



    Scaffold(
        topBar = { SingleChatTopBar(title = sentTo,profileImage, navController =navController ) }) {

        Column(modifier = Modifier
            .padding(it)
        ) {
            if(showLinearIndicator) {
                Box(modifier = Modifier.weight(1f)) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .height(2.dp), color = floatingActionBtnColor
                    )
                }
            }
            if(showError) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(text = "Error connecting to server", modifier = Modifier.align(Alignment.TopStart),fontFamily = Constants.FONT_MEDIUM, color = Color.Red)
                }

            }
            //-------------------------------MESSAGE ITEM ---------------------------------------------------
            when ( messages) {
                is RequestState.Success -> {
                    showLinearIndicator=false
                    val messageList=(messages as RequestState.Success<List<Message>>).data
                    if(messageList.isNotEmpty()) {
                        LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                            items(items = (messageList)) { message ->
                                MessageItemUI(
                                    msg = message.message,
                                    sent = message.sent,
                                    received = message.received,
                                    message.timestamp,
                                    isSentByLoggedInUser = message.senderId == ProfileObject.profile?.userId!!
                                )
                            }
                        }
                    }else{
                        NoMessagesScreen(modifier = Modifier.weight(1f))
                    }
                }
                is RequestState.Loading -> {
                    showLinearIndicator=true
                }
                is RequestState.Error -> {
                    Log.e("Splint_ERROR", "ChatScreenUI: ${(messages as RequestState.Error).error.message.toString()}", )
                    showLinearIndicator=false;
                    showError=true
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
                                         Row(modifier = Modifier.wrapContentWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                             Row(
                                                 verticalAlignment = Alignment.CenterVertically,
                                                 horizontalArrangement = Arrangement.spacedBy(8.dp)
                                             ) {
                                                 // Mic Icon
                                                 Image(
                                                     painter = painterResource(id = R.drawable.mic),
                                                     contentDescription = "mic",
                                                     colorFilter = ColorFilter.tint(color = Color.DarkGray),
                                                     modifier = Modifier
                                                         .size(28.dp)
                                                         .clickable {
                                                             voiceRecognizer.startListening()
                                                         }
                                                 )

                                                 // Blinking Red Dot (only while recording)
                                                 if (isRecording) {
                                                     Box(
                                                         modifier = Modifier
                                                             .size(10.dp)
                                                             .alpha(blinkAnim.value)
                                                             .background(Color.Red, shape = CircleShape)
                                                     )
                                                 }
                                             }
                                             Image(
                                                 painter = painterResource(id = R.drawable.cameranew),
                                                 contentDescription = "",
                                                 colorFilter = ColorFilter.tint(color = Color.DarkGray),
                                                 alignment = Alignment.Center,
                                                 modifier = Modifier
                                                     .size(28.dp)
                                                     .clickable {
                                                         navController.navigate("camerax/singleChat")
                                                     }
                                             )
                                         }
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
fun SingleChatTopBar(title: String,profileImage:String?, navController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                title,textAlign= TextAlign.Start, overflow= TextOverflow.Ellipsis, fontFamily = Constants.FONT_LIGHT,color = Color.White.copy(alpha = 0.8f), fontSize = 20.sp
            )
        },
        navigationIcon = {
            Row(modifier = Modifier.fillMaxWidth(0.23f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(
                    painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f)),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigateUp() }

                )
                GlideImage(
                    model =  imagePrefix + profileImage ,
                    contentDescription = "",
                    contentScale=ContentScale.Crop,
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(40.dp)
                        .clickable { navController.navigate(SCREENS.PROFILE.route) }
                )


                    Log.i("IMagePrefix", "SingleChatTopBar: $imagePrefix + $profileImage")

            }
        }, actions = {
            Row(modifier = Modifier
                .wrapContentSize()
                .padding(end = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(26.dp)) {

                Image(
                    painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_video),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Constants.HOME_TOP_BAR_ICON_COLOR)
                )
                Image(
                    painter = painterResource(id = androidx.core.R.drawable.ic_call_answer),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(Constants.HOME_TOP_BAR_ICON_COLOR)
                )
            }

        },
        modifier = Modifier.shadow(elevation = 10.dp, spotColor = Color.White),
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Constants.HOME_BOTTOM_BAR_COLOR)
    )
}

@Composable
fun NoMessagesScreen(modifier: Modifier,text:String) {
    Box(modifier) {
        Text(text = text, modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.LightGray,
            fontFamily = Constants.FONT_MEDIUM)
    }
}
class VoiceRecognizerHelper(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onStart: () -> Unit = {},
    private val onEnd: () -> Unit = {}
) {
    private val recognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }

    private var mediaRecorder: MediaRecorder? = null
    private var audioFilePath: String? = null

    init {
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                onStart()
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                onEnd()
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) onResult(matches[0])
            }

            override fun onError(error: Int) {
                onEnd()
                val errorMsg = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission error"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout"
                    else -> "Unknown error"
                }
                Toast.makeText(context, "Speech error: $errorMsg", Toast.LENGTH_SHORT).show()
                Log.e("VoiceRecognizer", "Error: $errorMsg")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onResult(matches[0])
                }
                onEnd()
            }
        })
    }

    fun startListening() {
        recognizer.startListening(intent)
    }

    fun stopListening() {
        recognizer.stopListening()
        onEnd()
    }

    // ===== New methods for audio recording =====
    fun startRecording() {
        try {
            val audioFile = createAudioFile()
            audioFilePath = audioFile.absolutePath

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFilePath)
                prepare()
                start()
            }

            onStart()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to start recording: ${e.message}", Toast.LENGTH_SHORT).show()
            onEnd()
        }
    }

    fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            onEnd()
            // You can now access the audio file at `audioFilePath` for sending/uploading
            Log.d("VoiceRecognizer", "Audio recorded at: $audioFilePath")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to stop recording: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAudioFile(): File {
        val storageDir = context.cacheDir
        return File.createTempFile("voice_", ".m4a", storageDir)
    }

    fun destroy() {
        recognizer.destroy()
        mediaRecorder?.release()
        mediaRecorder = null
    }
}

@Composable
fun MicButton(
    isListening: Boolean,
    isRecording: Boolean,
    onTapToListen: () -> Unit,
    onHoldToRecordStart: () -> Unit,
    onHoldToRecordStop: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scale by animateFloatAsState(
        targetValue = if (isRecording) 1.2f else 1f,
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onTapToListen() },
                    onPress = {
                        onHoldToRecordStart()
                        try {
                            awaitRelease()
                        } finally {
                            onHoldToRecordStop()
                        }
                    }
                )
            }
        ,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mic),
            contentDescription = "Mic",
            colorFilter = ColorFilter.tint(if (isRecording || isListening) Color.Red else Color.Black),
            modifier = Modifier.size(28.dp)
        )

        if (isListening) {
            // Red dot over top-end
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Red, shape = CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
            )
        }
    }
}
