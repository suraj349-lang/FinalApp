package com.example.finalapp.screens._6chat



import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.ChatList
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.screens.common.CommonLoadingScreen
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.FONT_MEDIUM
import com.example.finalapp.utils.constants.Constants.TAG
import com.example.finalapp.viewmodels.ChatViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavHostController,chatViewModel: ChatViewModel) {
    val lifecycleOwner= LocalLifecycleOwner.current
    val user by UserObject.user.collectAsState()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> chatViewModel.connectSocket()
                Lifecycle.Event.ON_DESTROY -> chatViewModel.disconnectSocket()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(key1 = Unit) {
        chatViewModel.getUserChatList(user.user)
    }

    val chatListState by chatViewModel.getUserChatList.collectAsState()
    var showSearchBox by remember { mutableStateOf(false) }


    Scaffold(topBar = {
        ChatTopBar(
            title = "Chats",
            user.profileImage,
            navController = navController
        ){
            showSearchBox=true
        }
    },
       // bottomBar = {BottomBar(navController = navController, state =buttonsVisible )}
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            //todo not needed row
//            LazyRow(modifier = Modifier.background(color = Color.LightGray).fillMaxWidth().height(50.dp)) {
//                items(chatRowListItems){item->
//                    ChatRowItem(item)
//                }
//            }
            Spacer(modifier = Modifier.height(10.dp))
            when(chatListState){
                is RequestState.Loading ->{ CommonLoadingScreen() }
                is RequestState.Error ->{
                    CommonErrorScreen("Error getting users")
                    Log.d(TAG, "ChatListScreen: ${(chatListState as RequestState.Error).error.message}")
                }
                is RequestState.Success ->{
                    val users=remember{ (chatListState as RequestState.Success<List<ChatList>>).data }
                    LazyColumn(modifier = Modifier) {
                        itemsIndexed(users) { i, user ->
                            UserItem(navController, user){imageUrl->
                                chatViewModel.profileImage.value=imageUrl
                            }
                            Divider(modifier = Modifier.fillMaxWidth(), color = Color(0xFFF1EAEA))
                        }
                    }
                }
                is RequestState.Idle ->{}
            }
        }
    }
}

@Composable
fun ChatRowItem(item: String) {
    Card(modifier = Modifier
        .wrapContentSize()
        .padding(8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(
            Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =item,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.dongle_bold)),
                fontSize = 16.sp,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ChatTopBar(title: String,profileImage:String, navController: NavHostController,onSearchClicked:()->Unit) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            modifier = Modifier.shadow(elevation = 10.dp),
            title = {
                Text(
                    title,textAlign= TextAlign.Center, fontFamily = Constants.FONT_MEDIUM, modifier = Modifier.fillMaxWidth(0.6f), color = Color( 0xFF000000), fontSize = 20.sp
                )
            },
            navigationIcon = {
                Row(modifier = Modifier) {
                    Image(
                        painterResource(id = R.drawable.back),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { navController.navigateUp() }

                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Card(
                        modifier = Modifier.size(30.dp),
                        shape = CircleShape,
                    ) {
                        GlideImage(
                            model= imagePrefix+ profileImage,
                            contentDescription = "",
                            contentScale=ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { navController.navigate(SCREENS.PROFILE.route) }

                        )
                    }

                }
            }, actions = {
                Row(modifier = Modifier.padding(end = 16.dp)) {
                    Image(
                        painterResource(id = R.drawable.search_new_filled),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onSearchClicked() }

                    )
                }


            }
        )
    }

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserItem(navController: NavHostController, user: ChatList,setProfileImage:(String)->Unit){

    Card(modifier = Modifier
        .padding(start = 8.dp, end = 8.dp)
        .fillMaxWidth()
        .height(60.dp)
        .clickable {
            setProfileImage(user.withUserId.profileImage)
            navController.navigate(
                SCREENS.SINGLE_CHAT.createPath(
                    user.withUserId.username,
                    user.withUserId._id
                )
            )
        },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFE))
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
//            GlideImage(
//                model =if("${Constants.BASE_URL}${imageUrl}".isNotEmpty()) "" else R.drawable.profile_image_1 ,
//                contentDescription ="",
//                transition= CrossFade,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .padding(2.dp)
//                    .size(40.dp)
//                    .clip(CircleShape)
//                    .border(1.dp, Color.DarkGray, CircleShape)
//            )
            Card(modifier = Modifier.size(50.dp), shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                GlideImage(model =if( imagePrefix+user.withUserId.profileImage != imagePrefix) imagePrefix+user.withUserId.profileImage else R.drawable.femaleprofile, contentDescription = "",modifier = Modifier
                    .padding(2.dp)
                    .size(40.dp)
                    .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = user.withUserId.username, fontFamily =FONT_MEDIUM,fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
                    Text(text = "08:38", fontSize = 12.sp, color = Color.LightGray)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(text =  "hello", fontSize = 14.sp,color= Color.Gray)
                }

            }

        }

    }
}

/*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(userNumber: String?,navController: NavHostController,chatViewModel: ChatViewModel) {
    val context= LocalContext.current
    val viewModel = viewModel<ChatViewModel>()
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
        if (key == 1) {

            LaunchedEffect(key1 = true) {
                scope.launch {
                    chatViewModel.saveChatToDB(
                        Chat(
                            sentTo = userNumber.toString(),
                            message = inputText,
                            received = "false",
                            sent = "Single Tick",
                            seen = "false",
                            timeStamp = System.currentTimeMillis()
                        )
                    )
                }
                key=0
            }
        }

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
*/