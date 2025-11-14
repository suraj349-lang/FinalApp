package com.spint.app.screens._6chat



import BottomBar
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
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
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.model.ChatList
import com.spint.app.navigation.SCREENS
import com.spint.app.ui.imagePrefix
import com.spint.app.screens.common.CommonErrorScreen
import com.spint.app.screens.common.CommonLoadingScreen
import com.spint.app.utils.UserObject
import com.spint.app.utils.RequestState
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.constants.Constants.TAG
import com.spint.app.viewmodels.ChatViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatListScreen(navController: NavHostController,chatViewModel: ChatViewModel) {
    val lifecycleOwner= LocalLifecycleOwner.current
    val user by UserObject.user.collectAsState()
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> chatViewModel.connectSocket()
                Lifecycle.Event.ON_RESUME -> chatViewModel.connectSocket()
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
    val systemUiController = rememberSystemUiController()


    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Constants.HOME_BOTTOM_BAR_COLOR,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = Constants.HOME_BOTTOM_BAR_COLOR,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }

    val chatListState by chatViewModel.getUserChatList.collectAsState()
    var showSearchBox by remember { mutableStateOf(false) }
    val buttonsVisible = remember {
        mutableStateOf(true)
    }


    Scaffold(topBar = {
        ChatTopBar(
            title = "Chats",
            user.profileImage,
            navController = navController
        ){
            showSearchBox=true
        }
    },
        bottomBar = {BottomBar(navController = navController, state =buttonsVisible, containerColor = Constants.HOME_BOTTOM_BAR_COLOR )}
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.LightGray) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                //todo not needed row
//            LazyRow(modifier = Modifier.background(color = Color.LightGray).fillMaxWidth().height(50.dp)) {
//                items(chatRowListItems){item->
//                    ChatRowItem(item)
//                }
//            }
                when (chatListState) {
                    is RequestState.Loading -> {
                        CommonLoadingScreen()
                    }

                    is RequestState.Error -> {
                        CommonErrorScreen("Error getting users list"){
                            chatViewModel.getUserChatList(user.user)
                        }
                        Log.d(
                            TAG,
                            "ChatListScreen: ${(chatListState as RequestState.Error).error.message}"
                        )
                    }

                    is RequestState.Success -> {
                        val users =
                            remember { (chatListState as RequestState.Success<List<ChatList>>).data }
                        LazyColumn(modifier = Modifier) {
                            itemsIndexed(users) { i, user ->
                                UserItem(navController, user) { imageUrl ->
                                    if (!imageUrl.isNullOrEmpty()) {
                                        chatViewModel.profileImage.value = imageUrl
                                    }
                                }
                                Spacer(modifier = Modifier.padding(top = 1.dp))
//                                Divider(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    color = Color(0xFFF1EAEA)
//                                )
                            }
                        }
                    }

                    is RequestState.Idle -> {}
                }
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
                containerColor = Constants.HOME_BOTTOM_BAR_COLOR
            ),
            modifier = Modifier.shadow(elevation = 10.dp),
            title = {
                Text(
                    title,textAlign= TextAlign.Center, fontFamily = Constants.FONT_MEDIUM, modifier = Modifier.fillMaxWidth(0.6f), color = Color.White.copy(alpha = 0.8f), fontSize = 20.sp
                )
            },
            navigationIcon = {
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Image(
                        painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f)),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.navigateUp() }

                    )
//                    Card(
//                        modifier = Modifier.size(45.dp),
//                        shape = CircleShape,
//                    ) {
//                        GlideImage(
//                            model= imagePrefix+ profileImage,
//                            contentDescription = "",
//                            contentScale=ContentScale.Crop,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clickable { navController.navigate(SCREENS.PROFILE.route) }
//
//                        )
//                    }

                }
            }, actions = {
//                Row(modifier = Modifier.padding(end = 16.dp)) {
//                    Image(
//                        painterResource(id = R.drawable.search_new_filled),
//                        contentDescription = "",
//                        colorFilter = ColorFilter.tint(Color.DarkGray),
//                        modifier = Modifier
//                            .size(24.dp)
//                            .clickable { onSearchClicked() }
//
//                    )
//                }


            }
        )
    }

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserItem(navController: NavHostController, user: ChatList,setProfileImage:(String?)->Unit){

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .clickable {
            setProfileImage(user.withUserId.profileImage)
            navController.navigate(
                SCREENS.SINGLE_CHAT.createPath(
                    user.withUserId.name,
                    user.withUserId._id
                )
            )
        },
        shape= RoundedCornerShape(0.dp),
        elevation=CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0ECE5)) //0xFFEEE9DD
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
            Card(modifier = Modifier.size(56.dp), shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                GlideImage(model =if(user.withUserId !=null || user.withUserId.profileImage.isNotEmpty()) imagePrefix+user.withUserId.profileImage else R.drawable.femaleprofile, contentDescription = "",modifier = Modifier
                    .padding(2.dp)
                    .fillMaxSize()
                    .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = user.withUserId.name, fontFamily = Constants.ROBOTO_CONDENSED,fontSize = 18.sp, color = Color.Black, lineHeight = 14.sp)
                    Text(text = "08:38", fontSize = 12.sp, color = Color.Gray, lineHeight = 14.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text(text =  "hello", fontSize = 14.sp,color= Color.Gray, fontFamily = Constants.FONT_LIGHT, lineHeight = 14.sp)
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