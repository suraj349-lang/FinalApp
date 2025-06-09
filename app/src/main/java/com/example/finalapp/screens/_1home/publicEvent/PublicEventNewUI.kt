package com.example.finalapp.screens._1home.publicEvent

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
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
import androidx.compose.ui.text.capitalize
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
import com.example.finalapp.screens._1home.commentBottomSheet.CommentBottomSheet
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PublicEventNewUI(
    event: EventResponse,
    navController: NavHostController,
    index: Int,
    height: Boolean,
    imageUrls: List<String>
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
                GlideImage(
                    model  = imagePrefix+event.image ,
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
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.9f), verticalArrangement = Arrangement.spacedBy(0.dp), horizontalAlignment = Alignment.Start) {
                        event.title?.let {
                            Text(
                                text = it.capitalize(),
                                fontFamily = Constants.FONT_MEDIUM,
                                color = Color(0xFF303F9F),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
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
            EventDescriptionNewUI(event.description)

            Card(modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(), elevation = 0.dp) {
                  Row(modifier = Modifier
                      .fillMaxWidth()
                      .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                      Image(painter = painterResource(id = R.drawable.location_new), contentDescription ="", modifier = Modifier.size(24.dp) )
                      Text(event.location, fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                  }
                  
              }
            Card(modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(), elevation = 0.dp) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.create_event_new), contentDescription ="", modifier = Modifier.size(22.dp) )
                    Text("Created at: ${event.expirationTime}", fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp)
                }

            }

            PeopleCommentWarAndJoinButtonNewUI(
                  event,
                onJoinClicked = {
                    // navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.route)
                },
                onCommentClicked = {
                    showBottomSheet=!showBottomSheet
                }
            )
            Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF303F9F))
                ) {
                    Text(
                        text = "Join",
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
            }
           // if(!event.topComments.isNullOrEmpty()) EventComments(event.topComments)

        }
    }
    CommentBottomSheet(showSheet = showBottomSheet) {
        showBottomSheet=false
    }

}

@Composable
fun PeopleCommentWarAndJoinButtonNewUI(event:EventResponse, onJoinClicked:()->Unit, onCommentClicked: () -> Unit) {
     val context= LocalContext.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.White))
    {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp,horizontal = 16.dp)
                .fillMaxWidth(0.8f)
                .wrapContentHeight(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Card(modifier = Modifier
                    .wrapContentSize()) {
                    Column(modifier=Modifier.padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.people),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(
                                Color.DarkGray
                            )
                        )
                        Text(
                            text = "4",
                            fontSize = 16.sp,
                            fontFamily = Constants.FONT_MEDIUM,
                            color = Color.DarkGray
                        )
                    }
                }
                Card(modifier = Modifier
                    .wrapContentSize()) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.comment_filled),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(
                                Color.DarkGray
                            )
                        )
                        Text(
                            text = "5",
                            fontSize = 16.sp,
                            fontFamily = Constants.FONT_MEDIUM,
                            color = Color.DarkGray
                        )
                    }
                }
                Card(modifier = Modifier
                    .wrapContentSize()) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.war_room),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(
                                Color.DarkGray
                            )
                        )
                        Text(
                            text = "5",
                            fontSize = 16.sp,
                            fontFamily = Constants.FONT_MEDIUM,
                            color = Color.DarkGray
                        )
                    }
                }
                Card(modifier = Modifier
                    .wrapContentSize()) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp),
                            colorFilter = ColorFilter.tint(
                                Color.DarkGray
                            )
                        )
                        Text(
                            text = "15",
                            fontSize = 16.sp,
                            fontFamily = Constants.FONT_MEDIUM,
                            color = Color.DarkGray
                        )
                    }
                }
        }
    }
}

@Composable
fun EventDescriptionNewUI(eventDescription:String?) {
    Box(modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .heightIn(min = 60.dp, max = 200.dp)
        .background(color = Color.White)) {
        Column(modifier= Modifier
            .padding(3.dp)
            .wrapContentSize()
            .verticalScroll(rememberScrollState())) {
            if (eventDescription != null) {
                Text(
                    text = eventDescription,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.Black,
                    lineHeight=20.sp,
                    softWrap = true,
                    fontSize = 18.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
