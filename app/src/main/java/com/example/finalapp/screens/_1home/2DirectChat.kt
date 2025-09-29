package com.example.finalapp.screens._1home


import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.DirectChatRequest
import com.example.finalapp.model.User
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.UserLocationObject
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel
import com.google.accompanist.pager.ExperimentalPagerApi


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun DirectChatScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    authViewModel: AuthViewModel,
    eventsViewModel: EventsViewModel,
    navController: NavHostController
) {

    val checked by eventsViewModel.checked
    val shareProfileClickedON by eventsViewModel.shareProfileClicked.collectAsState()
    var remove by remember{
        mutableStateOf(false)
    }
    val user by UserObject.user.collectAsState()
    val userLocation by UserLocationObject.userLocation.collectAsState()

    LaunchedEffect(key1 =shareProfileClickedON){
        if(shareProfileClickedON ){
            eventsViewModel.checked.value=!checked
            eventsViewModel.sendDirectChatData(DirectChatRequest(user.user,userLocation.latitude ?: 0.0,userLocation.longitude ?: 0.0))
        } else {
            if(remove){
                eventsViewModel.removeUserFromDirectChat(UserObject.user.value.user)
                remove=false
            }
            // eventsViewModel.emptyNearByUsersList()
        }
        eventsViewModel.shareProfileClicked.value = false

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
                        if(checked) {
                            Text(
                                text = "Verified users near you",
                                fontFamily = Constants.FONT_MEDIUM,
                                color = Constants.HOME_TOP_BAR_ICON_COLOR,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(top = 10.dp, start = 10.dp)
                            )
                            Switch(
                                checked = checked,
                                onCheckedChange = {
                                    eventsViewModel.shareProfileClicked.value = !eventsViewModel.shareProfileClicked.value
                                    remove=!remove
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = floatingActionBtnColor,// MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = Color(0xFFFFFFFF),
                                    uncheckedThumbColor = Color(0xFFFFFFFF),
                                    uncheckedTrackColor = Color(0xFFFFFFFF),
                                ),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 16.dp)
                            )
                        }else{

                            Column(modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp, top = 16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Range : 500 m",fontFamily = Constants.FONT_LIGHT, color = Color(
                                    0xFFEBE5EE
                                ), fontSize = 12.sp, style = TextStyle(textDecoration = TextDecoration.Underline))
                                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Image(painter = painterResource(id = R.drawable.edit_new), contentDescription ="", modifier = Modifier.size(12.dp), colorFilter = ColorFilter.tint(
                                        Color.White) )
                                    Text(text = "Edit",fontFamily = Constants.FONT_LIGHT, color = Color(
                                        0xFFF5EDF8
                                    ), fontSize = 9.sp)
                                }
                            }
                            Column(modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.TopCenter)) {
                                DirectChatHorizontalPager()
                            }
                        }
                    }
                    DirectChatUI(
                        user,
                        address = userLocation.address ?: "",
                        scrollBehavior,
                        eventsViewModel,
                        navController,
                        checked,
                        onOmegleClicked = {navController.navigate(SCREENS.OMEGLE.route)}
                    ) {
                        eventsViewModel.shareProfileClicked.value = true
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatUI(
    user: User,
    address:String,
    scrollBehavior: TopAppBarScrollBehavior,
    eventsViewModel: EventsViewModel,
    navController: NavHostController,
    checked: Boolean,
    onOmegleClicked:()->Unit,
    onShareProfileClicked: () -> Unit
) {
    if (!checked ) {
        ShareProfileForDirectChat(user, address = address,onOmegleClicked,onShareProfileClicked)
    } else {
        DirectChatProfiles(scrollBehavior,navController, eventsViewModel )
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectChatProfiles(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    eventsViewModel: EventsViewModel,
) {
    val chatState by eventsViewModel.directChatResponse.collectAsState()
    val directChatObjectList = eventsViewModel.nearByUsersList.collectAsLazyPagingItems()
    val saveToChatListSuccess by eventsViewModel.saveUserToChatListResponseState.collectAsState()
    val userObject by UserObject.user.collectAsState()
    when(val response=saveToChatListSuccess){
        is RequestState.Error ->  {
            Text(text = response.error.toString())
        }
        is RequestState.Success ->{
            Log.i("Userr", "DirectChatProfiles: ${response.data.withUserId.userName} other user id ${response.data.withUserId._id}")
            navController.navigate(
                SCREENS.SINGLE_CHAT.createPath(
                    userName = response.data.withUserId.userName,
                    chatListUserId = response.data.withUserId._id
                )
            )
            eventsViewModel.resetSaveToChatListSuccessToIdle()
        }
        is RequestState.Loading ->{
            CircularProgressIndicator()
        }
        else  ->{}
    }

    when (chatState) {
        is RequestState.Loading -> {
            DialogLoading()
        }
        is RequestState.Error -> {
            CommonErrorScreen(error = "Unable to get users.")
        }
        is RequestState.Success -> {
            LazyColumn( //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                items(directChatObjectList) {directChatObject->
                    if(directChatObject != null) {

                        DirectChatItem(
                            directChatObject = directChatObject,
                            onProfileClicked = {
                                navController.navigate(SCREENS.USER_PUBLIC_PROFILE.createPath(userId = directChatObject.userId.user))
                            },
                            onSendMessageClicked = {
                                Log.i("Userr", "DirectChatProfiles: ${userObject.user} other ${directChatObject.userId.user}")
                                eventsViewModel.saveUserToChatList(
                                    currentUserId = userObject.user,
                                    otherUserUserId = directChatObject.userId.user
                                )
                            }
                        )
                    }
                }
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DirectChatItem(
    directChatObject: DirectChat?,
    onProfileClicked: () -> Unit,
    onSendMessageClicked: () -> Unit
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .clickable {
                    onProfileClicked()
                }
                .padding(horizontal = 16.dp)
                .wrapContentWidth()
                .height(300.dp),
            shape = RoundedCornerShape(8.dp),
          //  border = BorderStroke(width = 0.5.dp, color = Color.Gray)
        ) {
            GlideImage(
                model = if(directChatObject?.userId?.profileImage?.isNotEmpty()==true) imagePrefix+directChatObject.userId.profileImage else "",
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
                        fontSize = 24.sp,lineHeight=8.sp,
                        color = Color.White,
                        fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.SemiBold
                    )
                }
                directChatObject?.userId?.userName?.capitalize()?.let {
                    Text(
                        text = "@$it",
                        fontSize = 12.sp,lineHeight=8.sp,
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
                ,colors = ButtonDefaults.buttonColors(
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



@Composable
fun ShareProfileForDirectChat(user:User,address:String,onOmegleClicked:()->Unit,onShareProfileClicked: () -> Unit) {
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.1f),
        Color.White.copy(alpha = 0.4f),
        Color.White.copy(alpha = 0.1f)
    )

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

                    DroppedProfileLocation(location = address, backgroundColor = Color(0xFF558B26), trim = true)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
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
                    Row(modifier = Modifier
                        .clickable { onOmegleClicked() }
                        .fillMaxWidth()
                        .height(60.dp)) {
                        Image(painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_video), contentDescription = "", modifier = Modifier.size(24.dp))
                        Text(
                            text = "+ Omegle",
                            fontSize = 16.sp,
                            fontFamily = Constants.FONT_LIGHT,
                            color = Color.White
                        )

                    }
                }
            }

    }
}