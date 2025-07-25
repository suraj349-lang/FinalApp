package com.example.finalapp.screens._1home.publicEvent


import android.provider.SyncStateContract.Constants
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.EventResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._1home.commentBottomSheet.CommentBottomSheet
import com.example.finalapp.screens._1home.commonUI.shareDeepLink
import com.example.finalapp.screens._1home.eventWarScreen.comments
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.PURPLE
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PublicEvent(  //currently in use
    event: EventResponse,
    navController: NavHostController,
    index: Int,
    height: Boolean,
    imageUrls: List<String>
) {
    var height by remember {
        mutableStateOf(height)
    }
    val context= LocalContext.current
    var image by remember {
        mutableStateOf(imageUrls.get(0))
    }
    var index by remember {
        mutableStateOf(index)
    }
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val systemUiController = rememberSystemUiController()
    val navBarColor = Color.DarkGray
    val backgroundColor= Color(0xFF121212)

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = navBarColor,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(
                color = navBarColor,
                darkIcons = true
            )
            systemUiController.setNavigationBarColor(
                color = navBarColor,
                darkIcons = true
            )
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f)
                   // .border(width = 1.dp, color = Color.LightGray)
            ) {
                GlideImage(
                    model = imagePrefix+event.image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .align(Alignment.TopStart),
                    backgroundColor = Color.Transparent, elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ActiveButton()
                        ReactionBar()
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 8.dp)
                        .wrapContentSize()
                        .align(Alignment.BottomEnd),
                    backgroundColor = Color(0xFF110107).copy(alpha = 0.2f),
                    shape = CircleShape, elevation = 0.dp
                ) {
                    ShareIcon(){
                        shareDeepLink(context,event._id) // TODO postId to be mended again
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(50.dp)
                        .fillMaxWidth(0.9f)
                        .align(Alignment.BottomStart),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {
                   UserData(event.user?.username ?: "testing", event.user?.profileImage ?: "")

                }
            }
            Box(
                modifier = Modifier.zIndex(4f).shadow(elevation = 60.dp, ambientColor = Color.White)
                    .fillMaxWidth()
                    .height(60.dp).padding(end = 8.dp)
                    .background(
                        color = Color.Black
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start =  8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(0.dp), horizontalAlignment = Alignment.Start) {
                        event.title?.let {
                            Text(
                                text = it,
//                                Modifier.fillMaxHeight(1f),
                                fontFamily = com.example.finalapp.utils.constants.Constants.FONT_MEDIUM,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }
                        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                           //  Image(painter = painterResource(id = R.drawable.location_new), contentDescription ="", modifier = Modifier.size(14.dp) )
                            Text(text =event.location, fontSize = 12.sp, fontFamily = com.example.finalapp.utils.constants.Constants.FONT_MEDIUM, color = Color(
                                0xFF4C9FF0
                            )
                            )
                        }
                    }
                    Button(onClick = { navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.route) }, modifier = Modifier.height(40.dp),shape= RoundedCornerShape(2.dp),colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                        Text(text = "+JOIN", fontFamily = com.example.finalapp.utils.constants.Constants.FONT_MEDIUM, fontSize = 16.sp,color=Color.Black)
                    }
                }
            }
            if(height) {
               EventDescription(event.description)
            }


           Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(Color.Black)
            ) {
                UserReactions(imageUrls) {selectedPost-> image = selectedPost}
            }


            PeopleCommentWarAndJoinButton(
                event,
                onCommentClicked = {
                    showBottomSheet=!showBottomSheet
                }
            )

//            if(!event.topComments.isNullOrEmpty())
         //   EventComments(sampleComments)

        }
    }
    CommentBottomSheet(showSheet = showBottomSheet) {
        showBottomSheet=false
    }

}

@Composable
fun PeopleCommentWarAndJoinButton(event: EventResponse,onCommentClicked: () -> Unit) {

    Box(modifier = Modifier
        .padding( 8.dp)
        .fillMaxWidth()
        .height(50.dp)
        .background(Color.Black))
    {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.people), contentDescription ="", modifier = Modifier.size(24.dp), colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f)) )
                Text(text =event.totalJoined.toString()  , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.White)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCommentClicked() }) {
                Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription ="", modifier = Modifier.size(24.dp), colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f)) )
                Text(text = event.totalComments.toString() , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.White)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.war_room), contentDescription ="", modifier = Modifier.size(24.dp),colorFilter = ColorFilter.tint(Color.White.copy(alpha=0.8f)) )
                Text(text= event.totalChildPosts.toString() , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.White
                )
            }
        }
    }
}

@Composable
fun EventDescription(eventDescription:String?) {
    Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .background(color = Color.DarkGray)) {
        Column(modifier= Modifier
            .padding(3.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            if (eventDescription != null) {
                Text(
                    text = eventDescription,
                    fontFamily = FontFamily(Font(R.font.dongle_bold)),
                    color = Color.White,
                    lineHeight=12.sp,
                    softWrap = true,
                    fontSize = 18.sp
                )
            }
        }}
}
