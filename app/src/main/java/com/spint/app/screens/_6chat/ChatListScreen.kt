package com.spint.app.screens._6chat



import BottomBar
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.runtime.ShouldPauseCallback
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.computeHorizontalBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
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
import com.spint.app.screens.dialogBox.ShowDialog
import com.spint.app.screens.dialogBox.ShowQRDialog
import com.spint.app.screens.qrcode.QRCode
import com.spint.app.screens.qrcode.rememberQrBitmapPainter
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserLocationObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


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
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Gray) {
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
                        Column(modifier = Modifier.fillMaxHeight(0.99f)) {
                            LazyColumn(modifier = Modifier.padding(top=5.dp)) {
                                item(){
                                   ChatSections()
                                }
                                items(users) { user ->
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
                            AddPeopleFromContacts()
                            ShareYourProfileInstead()
                        }
                    }

                    is RequestState.Idle -> {}
                }
            }
        }
    }
}

@Composable
fun ChatSections() {
    val list=listOf("Connections","")
}

@Composable
fun AddPeopleFromContacts(modifier: Modifier = Modifier) {
    Card(Modifier
        .padding(top = 30.dp)
        .padding(16.dp)
        .fillMaxWidth()
        .height(160.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(
        0xFFEAE0D3
    )
    )) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Add more people!", fontFamily = Constants.ROBOTO_FLEX, fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Get your friends from your contacts on ${Constants.APP_NAME}", fontFamily = Constants.ROBOTO_FLEX, fontSize = 12.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)) {
                Text("Get Contacts", fontFamily = Constants.FONT_LIGHT, fontSize = 16.sp, color = Color.White)
            }
        }

    }
}
@Composable
fun ShareYourProfileInstead(onShareProfileClicked:()-> Unit={}) {
    val user= UserObject.user.collectAsState()
    val qrPainter = rememberQrBitmapPainter("https://spint.com/profile/${user.value.user}", qrColor = Color.Black, backgroundColor = Color(0xFFE9F0F7).copy(alpha = 0.8f) )//0xFFEEF707
    Card(Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(100.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(
        0xFFC5B8E5
    )
    )) {
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.size(100.dp)) {
                    Image(
                        painter = qrPainter,
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp).fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                        )
                    AsyncImage(model = imagePrefix+user.value.profileImage, contentDescription = "", modifier = Modifier.align(Alignment.Center).clip(shape = CircleShape).size(30.dp), contentScale = ContentScale.Crop)
                }
                Column(modifier = Modifier.padding(vertical = 10.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {

                   Text("Share your Profile instead!", fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp, color = Color.Black.copy(alpha = 0.9f), fontWeight = FontWeight.Bold)
                   Button(onClick = {onShareProfileClicked()}, colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)) {
                     Image(painter = painterResource(R.drawable.share_event), contentDescription = "", modifier = Modifier.size(16.dp), colorFilter = ColorFilter.tint(Color.White))
                     Spacer(modifier = Modifier.width(10.dp))
                     Text("Share", fontFamily = Constants.FONT_LIGHT, fontSize = 16.sp, color = Color.White)
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
                Row(modifier = Modifier.padding(end = 16.dp)) {
                    Image(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {/* onSearchClicked() TODO: "  */ }

                                )
                            }


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
           // setProfileImage(user.withUserId.profileImage)
            val encodedImageUrl = URLEncoder.encode(user.withUserId.profileImage, StandardCharsets.UTF_8.toString())
            navController.navigate(
                SCREENS.SINGLE_CHAT.createPath(
                    user.withUserId.name,
                    encodedImageUrl,
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

