package com.example.finalapp.screens._1home.publicEvent


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.EventResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._1home.commentBottomSheet.CommentBottomSheet
import com.example.finalapp.screens._1home.commonUI.shareDeepLink
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.PURPLE
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL


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

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp)
        .border(width = 1.dp, color = Color.LightGray)) {
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
                    .fillMaxHeight(0.5f)
                    .border(width = 1.dp, color = Color.LightGray)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.18f)
                    .background(
                        color = Color(0xFFF5F5F5)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(0.dp), horizontalAlignment = Alignment.Start) {
                        Row(modifier = Modifier.fillMaxHeight(0.4f), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Image(painter = painterResource(id = R.drawable.location_new), contentDescription ="" )
                            Text(text =event.location, fontSize = 18.sp, fontFamily = DONGLE_BOLD)
                        }
                        event.title?.let {
                            Text(
                                text = it,
                                Modifier.fillMaxHeight(1f),
                                fontFamily = FontFamily(Font(R.font.dongle_bold)),
                                color =PURPLE,
                                fontSize = 30.sp
                            )
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            //Color.White
                        Color(0xFF095985)),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { height = !height })

                    Button(
                        onClick = { image= imageUrls.get(++index)},
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        border = BorderStroke(width = 0.5.dp, color = floatingActionBtnColor.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "Next",
                            fontFamily = FontFamily(Font(R.font.dongle_bold)), fontSize = 22.sp
                        )

                    }
                }
            }
            if(height) {
               EventDescription(event.description)
            }

           Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White)
            ) {
               event.topPostsList?.let { UserReactions(it) {selectedPost-> image = selectedPost} }
            }
            PeopleCommentWarAndJoinButton(
                event,
                onJoinClicked = {
                    navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.route)
                },
                onCommentClicked = {
                    showBottomSheet=!showBottomSheet
                }
            )
            if(!event.topComments.isNullOrEmpty()) EventComments(event.topComments)

        }
    }
    CommentBottomSheet(showSheet = showBottomSheet) {
        showBottomSheet=false
    }

}

@Composable
fun PeopleCommentWarAndJoinButton(event: EventResponse,onJoinClicked:()->Unit,onCommentClicked: () -> Unit) {

    Box(modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxWidth()
        .height(50.dp)
        .background(Color.White))
    {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.people), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text =event.peopleJoined.toString()  , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCommentClicked() }) {
                Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text = event.totalComments.toString() , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.war_room), contentDescription ="", modifier = Modifier.size(30.dp),colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text= event.totalChildPosts.toString() , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Button(onClick = {onJoinClicked() }, modifier = Modifier.height(40.dp),shape= RoundedCornerShape(8.dp),colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)) {
                Text(text = "+JOIN", fontFamily = DONGLE_BOLD, fontSize = 20.sp,color=Color.White)
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
