package com.example.finalapp.screens._1home.publicEvent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.finalapp.utils.constants.Constants


@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showBackground = true)
@Composable
fun PublicEventNewUI(  //currently in use
//    event: EventResponse,
//    navController: NavHostController,
//    index: Int,
//    height: Boolean,
//    imageUrls: List<String>
) {
    var height by remember {
        mutableStateOf(true)
    }
    val context= LocalContext.current
//    var image by remember {
//        mutableStateOf(imageUrls.get(0))
//    }
//    var index by remember {
//        mutableStateOf(index)
//    }
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()) {
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
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image_3) ,
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
                       // ReactionBar()
                    }
                }
//                Card(
//                    modifier = Modifier
//                        .padding(end = 16.dp, bottom = 8.dp)
//                        .wrapContentSize()
//                        .align(Alignment.BottomEnd),
//                    backgroundColor = Color(0xFF110107).copy(alpha = 0.2f),
//                    shape = CircleShape, elevation = 0.dp
//                ) {
//                    ShareIcon(){
//                        shareDeepLink(context,"jhssdfh5678efjjgeht7743") // TODO postId to be mended again
//                    }
//                }
//                Card(
//                    modifier = Modifier
//                        .padding(start = 8.dp)
//                        .height(50.dp)
//                        .fillMaxWidth(0.9f)
//                        .align(Alignment.BottomStart),
//                    backgroundColor = Color.Transparent,
//                    elevation = 0.dp
//                ) {
//                    UserData("Suraj_3494", R.drawable.profile_image_1.toString())
//
//                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = Color(0xFFFFFFFF)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth().wrapContentHeight()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.9f), verticalArrangement = Arrangement.spacedBy(0.dp), horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "State Elections",
                            fontFamily = Constants.FONT_MEDIUM,
                            color = Color(0xFF303F9F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(modifier = Modifier.wrapContentHeight(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                            Image(
//                                painter = painterResource(id = R.drawable.location_new),
//                                contentDescription = "",
//                                modifier = Modifier.size(20.dp)
//                            )
                            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                Text(text ="Barh, India", fontSize = 16.sp, fontFamily = Constants.FONT_MEDIUM, maxLines = 1, overflow = TextOverflow.Ellipsis, color = Color.DarkGray)
                            }
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            Color(0xFF095985)
                        ),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .size(30.dp)
                            .clickable { })

                }
            }
            Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
//            if(height) {
//                EventDescriptionNewUI("lkjfv fdgbuaid fgasudf gauidg auigiuasggfuiaw")
//            }

//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//                    .background(Color.White)
//            ) {
//                event.topPostsList?.let { UserReactions(it) {selectedPost-> image = selectedPost} }
//            }
            PeopleCommentWarAndJoinButtonNewUI(
              //  event,
                onJoinClicked = {
                   // navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.route)
                },
                onCommentClicked = {
                    showBottomSheet=!showBottomSheet
                }
            )
           // if(!event.topComments.isNullOrEmpty()) EventComments(event.topComments)

        }
    }
    CommentBottomSheet(showSheet = showBottomSheet) {
        showBottomSheet=false
    }

}

@Composable
fun PeopleCommentWarAndJoinButtonNewUI( onJoinClicked:()->Unit, onCommentClicked: () -> Unit) {
     val context= LocalContext.current
    Box(modifier = Modifier
        .padding(top = 12.dp)
        .fillMaxWidth()
        .height(50.dp)
        .background(Color.White))
    {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.people),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            Color.DarkGray
                        )
                    )
                    Text(
                        text = "4",
                        fontSize = 12.sp,
                        fontFamily = Constants.FONT_MEDIUM,
                        color = Color.DarkGray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onCommentClicked() }) {
                    Image(
                        painter = painterResource(id = R.drawable.comment_filled),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            Color.DarkGray
                        )
                    )
                    Text(
                        text = "5",
                        fontSize = 12.sp,
                        fontFamily = Constants.FONT_MEDIUM,
                        color = Color.DarkGray
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.war_room),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            Color.DarkGray
                        )
                    )
                    Text(
                        text = "5",
                        fontSize = 12.sp,
                        fontFamily = Constants.FONT_MEDIUM,
                        color = Color.DarkGray
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { shareDeepLink(context =context,"" ) }) {  //todo add deeplink
                    Image(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            Color.DarkGray
                        )
                    )
                    Text(
                        text = "15",
                        fontSize = 12.sp,
                        fontFamily = Constants.FONT_MEDIUM,
                        color = Color.DarkGray
                    )
                }
            }
            Button(
                onClick = { onJoinClicked() },
                modifier = Modifier.height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF303F9F))
            ) {
                Text(
                    text = "+ Join",
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun EventDescriptionNewUI(eventDescription:String?) {
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
        }
    }
}
