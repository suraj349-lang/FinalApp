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
import androidx.compose.ui.draw.scale
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
import com.spint.app.R
import com.spint.app.screens._1home._1FlashPosts.presentation.util.PulsingGreenButton
import com.spint.app.screens._1home._2directChat.util.DirectChatNotConnectedScreen
import com.spint.app.screens._1home._2directChat.util.NoDirectChatUsersFound
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
                    .background(Color.Black)
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
//                            Column(modifier = Modifier
//                                .wrapContentSize()
//                                .align(Alignment.TopStart)) {
//                                DirectChatHorizontalPager()
//                            }
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
        DirectChatNotConnectedScreen(address = address,onJoinDuelClicked,onShareProfileClicked)
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
    val userLocation by UserLocationObject.userLocation.collectAsState()
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
//        directChatList.itemCount == 0 && loadState.refresh !is LoadState.Loading  -> {
//            NoDirectChatUsersFound(onBackClicked = onCheckedChange,onJoinDuelClicked)
//            return
//        }

        loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && directChatList.itemCount == 0 -> {
            NoDirectChatUsersFound(
                onBackClicked = onCheckedChange,
                onJoinDuelClicked = onJoinDuelClicked
            )
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
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    PulsingGreenButton()
                    Text(
                        text = "Verified users near you",
                        fontFamily = Constants.FONT_LIGHT,
                        color = Constants.HOME_TOP_BAR_ICON_COLOR,
                        fontSize = 14.sp
                    )
                }
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userLocation.city ?: "",
                        fontFamily = Constants.FONT_LIGHT,
                        color = Constants.HOME_TOP_BAR_ICON_COLOR,
                        fontSize = 10.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Switch(
                        checked = true,
                        onCheckedChange = { onCheckedChange() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = floatingActionBtnColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.White
                        ),
                        modifier = Modifier.scale(0.7f),
                    )
                }

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
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Box(modifier = Modifier.align(Alignment.TopStart).zIndex(2f).padding(10.dp)
                .wrapContentSize()
                .clip(RoundedCornerShape(50))
                .background(color = Constants.HOME_TOP_BAR_COLOR.copy(alpha = 0.8f))) {
                Text("50m away", color = Color.White, fontSize = 10.sp, fontFamily = Constants.FONT_MEDIUM, lineHeight = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))

            }
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color(0xFF090808)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .clickable { onProfileClicked() }
                        .fillMaxWidth()//.wrapContentWidth()
                        .height(300.dp)
                ) {
                    GlideImage(
                        model = if (directChatObject?.userId?.profileImage?.isNotEmpty() == true) imagePrefix + directChatObject.userId.profileImage else R.drawable.profile_colored,
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.Center),
                        contentScale = if (directChatObject?.userId?.profileImage?.isNotEmpty() == true) ContentScale.Crop else ContentScale.Fit
                    )
                    Column(modifier = Modifier.padding(10.dp).wrapContentSize().align(Alignment.BottomStart)) {
                        directChatObject?.userId?.name?.capitalize()?.let {
                            Text(
                                text = it,
                                fontSize = 18.sp, lineHeight = 8.sp,
                                color = Color.White,
                                fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.Bold
                            )
                        }
                        directChatObject?.userId?.userName?.capitalize()?.let {
                            Text(
                                text = "@$it",
                                fontSize = 14.sp, lineHeight = 8.sp,
                                color = Color.LightGray,
                                fontFamily = Constants.FONT_LIGHT,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = { onSendMessageClicked() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                        , colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
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
                                modifier = Modifier.size(12.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Text(
                                text = "Message",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = Constants.FONT_MEDIUM
                            )
                        }

                    }
                    Spacer(Modifier.width(10.dp))
                    Button(
                        onClick = { onSendMessageClicked() },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                        , colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                        )
                    ) {
                        Row(
                            modifier = Modifier.wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.cameranew),
                                contentDescription = "",
                                modifier = Modifier.size(12.dp),
                                colorFilter = ColorFilter.tint(floatingActionBtnColor)
                            )
                            Text(
                                text = "Join Duel",
                                color = floatingActionBtnColor,
                                fontSize = 14.sp,
                                fontFamily = Constants.FONT_MEDIUM
                            )
                        }

                    }
                }

            }

   }
}

