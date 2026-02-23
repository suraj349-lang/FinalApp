package com.spint.app.screens._1home._2directChat


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

import com.spint.app.model.DirectChat
import com.spint.app.model.DirectChatRequest
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._4profile.privateUsername.dynamicText
import com.spint.app.screens.dialogBox.DialogLoading
import com.spint.app.ui.imagePrefix
import com.spint.app.screens.common.CommonErrorScreen
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserObject
import com.spint.app.utils.RequestState
import com.spint.app.utils.UserLocationObject
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.viewmodels.ChatViewModel
import com.spint.app.viewmodels.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.resources.CancelableFontCallback
import com.spint.app.R
import com.spint.app.screens._1home._3dropZone.DroppedProfileLocation
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun DirectChatScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    navController: NavHostController
) {

    val checked by homeViewModel.checked
    val shareProfileClickedON by homeViewModel.shareProfileClicked.collectAsState()
    var remove by remember{
        mutableStateOf(false)
    }
    val user by UserObject.user.collectAsState()
    val userLocation by UserLocationObject.userLocation.collectAsState()

    LaunchedEffect(key1 =shareProfileClickedON){
        if(shareProfileClickedON ){
            homeViewModel.checked.value=!checked
            homeViewModel.sendDirectChatData(DirectChatRequest(user.user,userLocation.latitude ?: 0.0,userLocation.longitude ?: 0.0))
        } else {
            if(remove){
                homeViewModel.removeUserFromDirectChat(UserObject.user.value.user)
                remove=false
            }
            // eventsViewModel.emptyNearByUsersList()
        }
        homeViewModel.shareProfileClicked.value = false

    }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .background(Constants.HOME_TOP_BAR_COLOR)
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
               // Image(painter = painterResource(id = R.drawable.whatsapp), contentDescription ="", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize() )
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()){
                        if(!checked) {

//                            Column(modifier = Modifier
//                                .align(Alignment.TopEnd)
//                                .padding(end = 16.dp, top = 16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
//                                Text(text = "Range : 500 m",fontFamily = Constants.FONT_LIGHT, color = Color(
//                                    0xFFEBE5EE
//                                ), fontSize = 12.sp, style = TextStyle(textDecoration = TextDecoration.Underline))
//                                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//                                    Image(painter = painterResource(id = R.drawable.edit_new), contentDescription ="", modifier = Modifier.size(12.dp), colorFilter = ColorFilter.tint(
//                                        Color.White) )
//                                    Text(text = "Edit",fontFamily = Constants.FONT_LIGHT, color = Color(
//                                        0xFFF5EDF8
//                                    ), fontSize = 9.sp)
//                                }
//                            }
                            Column(modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopStart)) {
                                DirectChatHorizontalPager()
                            }
                        }
                    }
                    DirectChatUI(
                        address = userLocation.address ?: "",
                        scrollBehavior,
                        homeViewModel,
                        chatViewModel ,
                        navController,
                        checked,
                        remove,
                        onCheckedChange = {
                            homeViewModel.shareProfileClicked.value = !homeViewModel.shareProfileClicked.value
                            remove=!remove
                        },
                        onJoinDuelClicked = {navController.navigate(SCREENS.DUEL.route)}
                    ) {
                        homeViewModel.shareProfileClicked.value = true
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatUI(
    address:String,
    scrollBehavior: TopAppBarScrollBehavior,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    navController: NavHostController,
    checked: Boolean,
    remove:Boolean,
    onCheckedChange: () -> Unit,
    onJoinDuelClicked:()->Unit,
    onShareProfileClicked: () -> Unit
) {
    if (!checked ) {
        ShareProfileForDirectChat(address = address,onJoinDuelClicked,onShareProfileClicked)
    } else {
        DirectChatProfiles(scrollBehavior,navController, homeViewModel,chatViewModel, onJoinDuelClicked = onJoinDuelClicked, onCheckedChange = onCheckedChange )
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatProfiles(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    onJoinDuelClicked: () -> Unit,
    onCheckedChange: () -> Unit
) {
    val saveToChatResponse by homeViewModel.saveUserToChatListResponseState.collectAsState()
    val userObject by UserObject.user.collectAsState()
    val directChatList = homeViewModel.nearByUsersList.collectAsLazyPagingItems()

    // -----------------------------
    // HANDLE SAVE-TO-CHAT RESPONSE
    // -----------------------------
    when (val res = saveToChatResponse) {
        is RequestState.Success -> {
            chatViewModel.connectSocket()
            val encodedImageUrl = URLEncoder.encode(res.data.withUserId.profileImage ?: "", StandardCharsets.UTF_8.toString())
            navController.navigate(
                SCREENS.SINGLE_CHAT.createPath(
                    userName = res.data.withUserId.name,
                    profileImage = encodedImageUrl,
                    chatListUserId = res.data.withUserId._id
                )
            )
            homeViewModel.resetSaveToChatListSuccessToIdle()
        }

        is RequestState.Error -> Text(text = res.error.toString())
        is RequestState.Loading -> CircularProgressIndicator()
        else -> {}
    }

    // -----------------------------
    // HANDLE PAGING LOAD STATES
    // -----------------------------
    val loadState = directChatList.loadState
    when {
        loadState.refresh is LoadState.Loading -> {
            DialogLoading()
            return
        }
        loadState.refresh is LoadState.Error -> {
            CommonErrorScreen("Unable to get users.")
            return
        }
        directChatList.itemCount == 0 && loadState.refresh !is LoadState.Loading  -> {
            NoDirectChatUsersFound(onJoinDuelClicked)
            return
        }
    }

    // -----------------------------
    // MAIN LIST
    // -----------------------------
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        // Header + Switch
        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Verified users near you",
                    fontFamily = Constants.FONT_LIGHT,
                    color = Constants.HOME_TOP_BAR_ICON_COLOR,
                    fontSize = 20.sp
                )

                Switch(
                    checked = true,
                    onCheckedChange = { onCheckedChange() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = floatingActionBtnColor,
                        checkedTrackColor = Color.White,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.White
                    ),
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        // Users list
        items(
            count = directChatList.itemCount,
            key = { index -> directChatList[index]?.userId?.user ?: index }
        ) { index ->
            directChatList[index]?.let { item ->
                DirectChatItem(
                    directChatObject = item,
                    onProfileClicked = {
                        navController.navigate(
                            SCREENS.USER_PUBLIC_PROFILE.createPath(
                                userId = item.userId.user
                            )
                        )
                    },
                    onSendMessageClicked = {
                        homeViewModel.saveUserToChatList(
                            currentUserId = userObject.user,
                            otherUserUserId = item.userId.user
                        )
                    }
                )
            }
        }
    }
}




@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DirectChatItem(
    directChatObject: DirectChat?,
    onProfileClicked: () -> Unit,
    onSendMessageClicked: () -> Unit
) {
        Box(
            modifier = Modifier.padding(10.dp).fillMaxWidth().wrapContentHeight()
        ) {
            Column() {
            Box(modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(2.dp)).background(color= Constants.HOME_TOP_BAR_COLOR.copy(alpha = 0.2f))) {
                Text("50m away", color = Color.LightGray, fontSize = 10.sp, fontFamily = Constants.FONT_LIGHT, modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp))

            }
            Column(
                modifier = Modifier.wrapContentSize().clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp, topEnd = 8.dp))
                    .background(color = Color.DarkGray),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier.clickable { onProfileClicked() }
                        .fillMaxWidth()//.wrapContentWidth()
                        .height(300.dp)
                ) {
                    GlideImage(
                        model = if (directChatObject?.userId?.profileImage?.isNotEmpty() == true) imagePrefix + directChatObject.userId.profileImage else "",
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.wrapContentSize()) {
                        directChatObject?.userId?.name?.capitalize()?.let {
                            Text(
                                text = it,
                                fontSize = 24.sp, lineHeight = 8.sp,
                                color = Color.White,
                                fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.SemiBold
                            )
                        }
                        directChatObject?.userId?.userName?.capitalize()?.let {
                            Text(
                                text = "@$it",
                                fontSize = 12.sp, lineHeight = 8.sp,
                                color = Color.Gray,
                                fontFamily = Constants.FONT_LIGHT, fontWeight = FontWeight.Normal
                            )
                        }

                    }


                    Button(
                        onClick = { onSendMessageClicked() },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .wrapContentWidth() //.wrapContentHeight().fillMaxWidth(0.8f)
                        //.align(Alignment.CenterHorizontally),
                        , colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                        )
                    ) {
                        Row(
                            modifier = Modifier.wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.chat_new),
                                contentDescription = "",
                                modifier = Modifier.size(16.dp),
                                colorFilter = ColorFilter.tint(Color.Black)
                            )
                            Text(
                                text = "Send Message",
                                color = Color.Black,
                                fontFamily = Constants.FONT_LIGHT
                            )
                        }

                    }
                }
                Divider(color = floatingActionBtnColor.copy(alpha = 0.23f), thickness = 0.18.dp)

            }
        }

   }
}


@Composable
fun ShareProfileForDirectChat(address:String,onOmegleClicked:()->Unit,onShareProfileClicked: () -> Unit) {
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.1f),
        Color.White.copy(alpha = 0.4f),
        Color.White.copy(alpha = 0.1f)
    )
    val user by UserObject.user.collectAsState()

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -200f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "translateAnim"
    )

    Box(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth()
            .fillMaxHeight(1f)
    ) {

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier
                .size(200.dp), shape = CircleShape) {
                AsyncImage(
                    model = imagePrefix + user.profileImage,
                    contentDescription = "",
                    filterQuality = FilterQuality.High,
                    modifier = Modifier
                        // .padding(1.dp)
                        .clip(shape = CircleShape)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = user.userName,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 18.sp,
                        color= Color(0xFFFFFFFF),
                        fontWeight = FontWeight.ExtraBold
                    )
                    //0xFF2CA832  0xFF045708 0xFF77209C

                    DroppedProfileLocation(
                        location = address,
                        backgroundColor = Color(0xFF558B26),
                        trim = true
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .graphicsLayer { clip = true }
                            .drawWithCache {
                                val shimmerBrush = Brush.linearGradient(
                                    colors = shimmerColors,
                                    start = Offset(translateAnim - 200f, 0f),
                                    end = Offset(translateAnim, size.height)
                                )
                                onDrawWithContent {
                                    // Draw original background
                                    drawRoundRect(
                                        color = Color(0xFF045708), //0xFF520772
                                        cornerRadius = CornerRadius(12.dp.toPx())
                                    )

                                    drawContent() // Draw the text

                                    // Draw shimmer glance
                                    drawRect(
                                        brush = shimmerBrush,
                                        blendMode = BlendMode.SrcOver // prevents color washing
                                    )
                                }
                            }
                            .clickable { onShareProfileClicked() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ CONNECT",
                            fontSize = 16.sp,
                            fontFamily = Constants.FONT_LIGHT,
                            color = Color.White
                        )
                    }
                    Divider(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp), thickness = 0.5.dp, color = Color.Gray)
                    Text(
                        text = "or",
                        fontSize = 12.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF1976D2), Color(0xFF460761)
                                )
                            )
                        )) {

                        Row(modifier = Modifier
                            .clickable { onOmegleClicked() }
                            .fillMaxWidth()
                            .height(50.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center) {
                            Image(
                                painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_video),
                                contentDescription = "",
                                modifier = Modifier.size(40.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(
                                text = "Join a Duel",
                                fontSize = 16.sp,
                                fontFamily = Constants.FONT_LIGHT,
                                color = Color.White
                            )

                        }
                    }
                }
            }

    }
}

@Composable
fun NoDirectChatUsersFound(onJoinDuelClicked:()->Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()){
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.smiley), contentDescription ="", modifier = Modifier
                .size(100.dp)
                .padding(bottom = 20.dp) )
        dynamicText(text = "No users found near you!", fontSize = 20, fontFamily = Constants.FONT_MEDIUM, lineHeight = 12)
        dynamicText(text = "Try the Duel instead",fontSize = 16, fontFamily = Constants.FONT_LIGHT)
            Spacer(modifier = Modifier.height(30.dp))
        Box(modifier = Modifier
            .fillMaxWidth(0.6f)
            .wrapContentHeight()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1976D2), Color(0xFF460761)
                    )
                )
            )) {

            Row(modifier = Modifier
                .clickable { onJoinDuelClicked() }
                .fillMaxWidth()
                .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_video),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Join a Duel",
                    fontSize = 16.sp,
                    fontFamily = Constants.FONT_LIGHT,
                    color = Color.White
                )

            }
    }
}}}