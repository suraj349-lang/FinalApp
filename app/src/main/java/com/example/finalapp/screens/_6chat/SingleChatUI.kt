package com.example.finalapp.screens._6chat

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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.database.ChatItem
import com.example.finalapp.model.Message
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.viewmodels.ChatViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
            chatViewModel.getAllMessages(UserObject.user.value.user ?: "", chatListUserId)
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

    Scaffold(
        topBar = { SingleChatTopBar(title = sentTo, chatUserImage, navController) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (showLinearIndicator) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = floatingActionBtnColor
                )
            }

            if (showError) {
                Text(
                    text = "Error connecting to server",
                    modifier = Modifier.padding(8.dp),
                    color = Color.Red,
                    fontFamily = Constants.FONT_MEDIUM
                )
            }

            when (messages) {
                is RequestState.Success -> {
                    showLinearIndicator = false
                    val messageList = (messages as RequestState.Success<List<Message>>).data
                    if (messageList.isNotEmpty()) {
                        LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                            items(messageList) { message ->
                                MessageItemUI(
                                    msg = message.message,
                                    sent = message.sent,
                                    received = message.received,
                                    timestamp = message.timestamp,
                                    isSentByLoggedInUser = message.senderId == UserObject.user.value.user
                                )
                            }
                        }
                    } else {
                        NoMessagesScreen(modifier = Modifier.weight(1f))
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

            // Input row with text field and mic button
            Row(
                modifier = Modifier
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
                        Text(text = "Type message....", fontSize = 14.sp)
                    },
                    maxLines = 10,
                    textStyle = TextStyle(fontSize = 14.sp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.DarkGray,
                        unfocusedBorderColor = Color.DarkGray,
                        cursorColor = Color.Red
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
                                    colorFilter = ColorFilter.tint(color = Color.DarkGray),
                                    alignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(28.dp)
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
                                    fontFamily = DONGLE_BOLD,
                                    fontSize = 24.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                )
            }
        }
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
fun SingleChatTopBar(title: String,profileImage:String, navController: NavHostController) {
    TopAppBar(
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
                            .clickable { navController.navigateUp() }
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
            // TODO we will make this later on
//            Card(
//                modifier = Modifier.size(30.dp),
//                shape = CircleShape,
//                colors = CardDefaults.cardColors(containerColor = Color.White)
//            ) {
//                Image(
//                    painterResource(id = R.drawable.menu),
//                    contentDescription = "",
//                    colorFilter = ColorFilter.tint(Color.DarkGray),
//                    modifier = Modifier
//                        .clickable {  }
//                        .padding(8.dp)
//
//                )
//            }

        },
        modifier = Modifier.shadow(elevation = 10.dp, spotColor = Color.White)
    )
}

@Composable
fun NoMessagesScreen(modifier: Modifier) {
    Box(modifier) {
        Text(text = "Say hi!", modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center), textAlign = TextAlign.Center, fontSize = 20.sp, color = Color.LightGray, fontFamily = Constants.FONT_MEDIUM)
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
            colorFilter = ColorFilter.tint(if (isRecording || isListening) Color.Red else Color.DarkGray),
            modifier = Modifier.size(32.dp)
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
