package com.example.finalapp.screens._1home


import android.util.Log
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
import com.example.finalapp.utils.UserLocation
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel


@OptIn(ExperimentalMaterial3Api::class)
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

    LaunchedEffect(key1 =shareProfileClickedON){
        if(shareProfileClickedON ){
            eventsViewModel.checked.value=!checked
            eventsViewModel.sendDirectChatData(DirectChatRequest( UserObject.user.value.userId,authViewModel.latitude.value,authViewModel.longitude.value))
        } else {
            if(remove){
                eventsViewModel.removeUserFromDirectChat(UserObject.user.value.userId)
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
                                text = "Users near you",
                                fontFamily = Constants.FONT_MEDIUM,
                                color = floatingActionBtnColor,
                                fontWeight = FontWeight.ExtraBold,
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
                                    checkedTrackColor = Color(0xFFF0E3C5),
                                    uncheckedThumbColor = Color(0xFFE9AB10),
                                    uncheckedTrackColor = Color(0xFFF0E3C5),
                                ),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(end = 16.dp)
                            )
                        }else{
                            Column(modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp, top = 16.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Range : 500 m",fontFamily = Constants.FONT_LIGHT, color = Color(0xFF280636), fontSize = 12.sp, style = TextStyle(textDecoration = TextDecoration.Underline))
                                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Image(painter = painterResource(id = R.drawable.edit_new), contentDescription ="", modifier = Modifier.size(12.dp) )
                                    Text(text = "Edit",fontFamily = Constants.FONT_LIGHT, color = Color(0xFF280636), fontSize = 9.sp)
                                }
                            }
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .align(Alignment.TopCenter), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.direct_chat),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(100.dp)
                                )
                                Text(text = "Connect to nearby people.", fontFamily = Constants.FONT_MEDIUM, color = Color(0xFF520772), fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                                Text(text = "Click connect to share your profile!", style = TextStyle(textDecoration = TextDecoration.Underline),fontFamily = Constants.FONT_MEDIUM, color = Color(0xFF280636), fontSize = 14.sp) //0xFF520772 0xFF256828 0xFF045708
                            }
                        }
                    }
                    DirectChatUI(
                        user!!,
                        scrollBehavior,
                        eventsViewModel,
                        navController,
                        checked
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
    scrollBehavior: TopAppBarScrollBehavior,
    eventsViewModel: EventsViewModel,
    navController: NavHostController,
    checked: Boolean,
    onShareProfileClicked: () -> Unit
) {
    if (!checked ) {
        ShareProfileForDirectChat(user,onShareProfileClicked)
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
    val userList = eventsViewModel.nearByUsersList.collectAsLazyPagingItems()
    val saveToChatListSuccess by eventsViewModel.saveUserToChatListResponseState.collectAsState()
    when(val response=saveToChatListSuccess){
        is RequestState.Error ->  {
            Text(text = response.error.toString())
        }
        is RequestState.Success ->{
            navController.navigate(SCREENS.SINGLE_CHAT.createPath(response.data.withUserId.username,response.data.withUserId._id))
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
            LazyColumn(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
                items(userList) { user ->
                    user?.let {
                        DirectChatItem(
                            user=it,
                            onProfileClicked = { navController.navigate(SCREENS.USER_PUBLIC_PROFILE.createPath(it.userId.userId)) },
                            onSendMessageClicked = { eventsViewModel.saveUserToChatList(UserObject.user.value.userId, it.userId.userId) }
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
    user: DirectChat?,
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
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            GlideImage(
                model = if(user?.userId?.profileImage?.isNotEmpty() == true) imagePrefix+user.userId.profileImage else "",
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if(user?.userId?.name?.isNotEmpty() == true) user.userId.name.capitalize() else "...",
                fontSize = 20.sp,
                fontFamily = Constants.USER_NAME_FONT, fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { onSendMessageClicked() },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth(0.5f) //.wrapContentHeight().fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
            )
        ) {
            Text(text = "Send Message", color = Color.White,fontFamily = Constants.FONT_LIGHT)
        }
        Divider(color = floatingActionBtnColor, thickness = 0.5.dp)

    }
}



@Composable
fun ShareProfileForDirectChat(user:User,onShareProfileClicked: () -> Unit) {
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
                        .padding(1.dp)
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
                        text = user.username,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 18.sp,
                        color= Color(0xFF520772),
                        fontWeight = FontWeight.ExtraBold
                    )
                    //0xFF2CA832  0xFF045708 0xFF77209C

                    UserLocation.address?.let { DroppedProfileLocation(location = it, backgroundColor = Color(0xFF558B26), trim = true) }

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
                }
            }

    }

}