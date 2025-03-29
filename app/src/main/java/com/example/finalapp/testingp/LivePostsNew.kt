package com.example.finalapp.testingp


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.screens._1home._1_1Events.CommentBottomSheet
import com.example.finalapp.screens._1home.utils.shareImageFromUrl
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.PURPLE
import com.example.finalapp.ui.theme.vectorScreenIcons
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL
import com.example.finalapp.viewmodels.EventsViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ActiveEvents(
    eventsViewModel: EventsViewModel,
    modifier: Modifier = Modifier,
    videos: List<String>,
    initialPage: Int? = 0,
    navController: NavHostController

    ) {
    val pagerState = rememberPagerState(initialPage = initialPage ?: 0, pageCount = { videos.size })
    val fling = PagerDefaults.flingBehavior(
        state = pagerState, lowVelocityAnimationSpec = tween(
            easing = LinearEasing, durationMillis = 300
        )
    )
    val index by remember {
        mutableStateOf(0)
    }
    val height by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VerticalPager(
            pageSize = PageSize.Fill,
            state = pagerState,
            flingBehavior = fling,
            beyondBoundsPageCount = 1,//todo set to 1 to save memory
            modifier = modifier.weight(1f)
        ) {
        if(it%2==0) {
            PrivateLiveEventInUse(navController )
        }else {
            PublicActiveEvent(index, height, imageUrls)
        }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun  PrivateLiveEvent2() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.58f)
                    .border(width = 1.dp, color = Color.LightGray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image_2),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .align(Alignment.TopStart),
                    backgroundColor = Color.White.copy(alpha = 0.0f)
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
                    ShareIcon(){}
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
                    UserData()

                }
            }

            DetailsBar()
            AcceptButtonBar()
            UserComment()
        }
    }

}

@Composable
fun DetailsBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            // .background(brush = Brush.linearGradient(colors = listOf(Color(0xFF4E086D), Color(0xFF9B0842))))
            .background(color = Color(0xFFFAF8F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.coffe_heart),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(
                        Color(0xFFE6D6AF)
                    ),
                    modifier = Modifier.size(30.dp)
                )
                Card(
                    modifier = Modifier.height(25.dp), backgroundColor = Color(
                        0xFFFAF7F8
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp), verticalAlignment = Alignment.Bottom
                    ) {

                        Text(
                            text = "Coffee",
                            fontFamily = FontFamily(Font(R.font.dongle_bold)),
                            fontSize = 20.sp,
                            color = Color(0xFFB9094E)
                        )

                    }

                }
                Card(
                    modifier = Modifier
                        .height(25.dp)
                        .align(Alignment.CenterVertically),
                    backgroundColor = Color(
                        0xFFFAF7F8
                    )
                ) {
                    Text(
                        text = "TONIGHT!!",
                        fontFamily = FontFamily(Font(R.font.dongle_bold)),
                        fontSize = 20.sp,
                        color = Color(0xFF42065C)
                    )
                }
                Card(
                    modifier = Modifier
                        .height(25.dp)
                        .align(Alignment.CenterVertically),
                    backgroundColor = Color(
                        0xFFFAF7F8
                    )
                ) {
                    Text(
                        text = "Delhi/NCR",
                        fontFamily = FontFamily(Font(R.font.dongle_bold)),
                        fontSize = 20.sp,
                        color = Color(0xFF180742)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.drop_profile_filled_rounded),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                        Color.Black
                    )
                )
                Text(
                    text = "Cannaught Place , Delhi",
                    fontFamily = FontFamily(Font(R.font.dongle_bold)),
                    fontSize = 20.sp, color = Color.Black
                )
            }

        }
    }
}

@Composable
fun AcceptButtonBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
    ) {
        Row() {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f)
            ) {
                repeat(6) {
                    Card(modifier = Modifier.size(30.dp), shape = CircleShape) {
                        Image(
                            painter = painterResource(id = R.drawable.girl),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                }

            }
            Button(
                onClick = {}, colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFAD0749)
                )
            ) {
                Text(
                    text = "Accept",
                    fontFamily = FontFamily(Font(R.font.dongle_bold)),
                    color = Color.White
                )

            }
        }
    }

}

@Composable
fun UserComment() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
    ) {
        LazyColumn {
            items(100) {
                UsersComment()
            }
        }
    }
    
}
@Composable
fun UserData() {
    Row(modifier = Modifier
        .fillMaxWidth(0.85f)
        .wrapContentHeight(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Start) {
        Card(modifier = Modifier.size(40.dp), shape = CircleShape) {
            Image(painter = painterResource(id = R.drawable.girl), contentDescription = "", contentScale = ContentScale.Crop)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Kriti Shinde",
            fontFamily = FontFamily(Font(R.font.dongle_bold)),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.White.copy(alpha = 0.8f), // Light glow effect
                    offset = Offset(0f, 0f),
                    blurRadius = 8f
                )
            )
        )
    }

}

@Composable
fun ShareIcon(onSharePostClicked:()->Unit) {
        Column(
            modifier = Modifier
                .clickable { onSharePostClicked() }
                .padding(10.dp)
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.share),
                contentDescription = "",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(color = Color.White)
            )

        }
    }

@Composable
fun ActiveButton() {
    val scale by rememberInfiniteTransition(label = "scale")
        .animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "scale"
        )
    val alpha by rememberInfiniteTransition(label = "alpha")
        .animateFloat(
            initialValue = 1f,
            targetValue = 0.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "alpha"
        )
    Card(
        modifier = Modifier
            .wrapContentSize()
            //.graphicsLayer(scaleX = scale, scaleY = scale),
            .alpha(alpha),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(
            0xFFA52C06
        ).copy(alpha = 0.9f), elevation = 0.dp
    ) {
        Text(
            text = "ACTIVE",
            fontFamily = FontFamily(Font(R.font.dongle_bold)),
            fontSize = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ReactionBar() {
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column{
            Image(
                painter = painterResource(id = R.drawable.view),
                contentDescription = "",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(vectorScreenIcons)
            )
            Text(
                text = "120k",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.dongle_bold))
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(bottom = 8.dp), color = Color.LightGray.copy(alpha = 0.2f))
        Column {
            Image(
                painter = painterResource(id = R.drawable.up),
                contentDescription = "",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(vectorScreenIcons)
            )
            Text(
                text = "12k",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.dongle_bold))
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(bottom = 8.dp), color = Color.LightGray.copy(alpha = 0.2f))
    }
}
@Composable
fun UsersComment() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        backgroundColor = Color(0xFFFAF8F6), border = BorderStroke(width = 1.dp, color = Color.LightGray.copy(alpha = 0.7f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
        ) {
            Card(modifier = Modifier.size(20.dp), shape = CircleShape) {
                Image(painter = painterResource(id = R.drawable.girl), contentDescription = "", contentScale = ContentScale.Crop)
            }
            Text(
                text = "Suraj : ",
                fontFamily = FontFamily(Font(R.font.dongle_regular)),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "This girl is fantastic, I want her!!!!",
                fontFamily = FontFamily(Font(R.font.dongle_bold)),
                fontSize = 22.sp,
                color = Color(0xFF2B043C)
            )

        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun Tiktok(
    modifier: Modifier = Modifier,
    videos: List<String>,
    initialPage: Int? = 0,

    ) {
    val pagerState = rememberPagerState(initialPage = initialPage ?: 0, pageCount = { videos.size })
    val fling = PagerDefaults.flingBehavior(
        state = pagerState, lowVelocityAnimationSpec = tween(
            easing = LinearEasing, durationMillis = 300
        )
    )
    Scaffold(topBar ={TopBar()} ) {

        VerticalPager(
            pageSize = PageSize.Fill,
            state = pagerState,
            flingBehavior = fling,
            beyondBoundsPageCount = 1,
            modifier = modifier.padding(it)
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = it.toString())

                }


            }
        }

    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = "Vector")},
        navigationIcon = {
                         Icon(painter = painterResource(id = R.drawable.app_icon_new), contentDescription ="", modifier = Modifier.size(30.dp) )
        },
        actions = {
            Row(modifier = Modifier.wrapContentWidth()) {
                Icon(painter = painterResource(id = R.drawable.notification_new), contentDescription ="", modifier = Modifier
                    .size(30.dp)
                    .padding(end = 16.dp) )
                Icon(painter = painterResource(id = R.drawable.new_qr), contentDescription ="", modifier = Modifier
                    .size(30.dp)
                    .padding(end = 16.dp) )

            }
        },
        backgroundColor = Color.White,
        elevation=0.dp,
        contentColor = Color.Black
    )
}

@Composable
fun  PrivateLiveEventInUse(navController:NavHostController) {
    var showCommentBottomSheet by remember {
        mutableStateOf(false)
    }
    val image= imagePrefix+"suraj3494/96fa89af-7a33-489f-8c06-134fc4aa25d4.jpeg";
    val context=LocalContext.current
    val scope= rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .border(width = 1.dp, color = Color.LightGray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image_2),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .align(Alignment.TopStart),
                    backgroundColor = Color.White.copy(alpha = 0.0f)
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
                        scope.launch {
                            shareImageFromUrl(context, image, "new Post")
                        }

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
                    UserData()
                }
            }
            OfferRequest()
            OfferLocation()
            //MovingText()
            ProfileReactionBar() {showCommentBottomSheet=true}
            DateAndTime()
        }
        CommentBottomSheet(
            showSheet = showCommentBottomSheet,
            onDismiss = { showCommentBottomSheet = false },
            //navHostController = navController
        )
    }

}
@Composable
fun MovingText() {
    val textOffsetX = remember { Animatable(0f) }
    val textWidth = remember { mutableStateOf(0f) } // Stores text width
    val parentWidth = remember { mutableStateOf(0f) } // Stores parent width
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            textOffsetX.snapTo(parentWidth.value) // Start from right edge
            textOffsetX.animateTo(
                targetValue = -textWidth.value, // Move left completely
                animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .onSizeChanged { parentWidth.value = it.width.toFloat() } // Capture parent width
    ) {
        Text(
            text = "This is a moving text animation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .graphicsLayer { translationX = textOffsetX.value } // Move text left
                .onSizeChanged { textWidth.value = it.width.toFloat() } // Capture text width
        )
    }
}

@Composable
fun ProfileReactionBar(onCommentClicked:()->Unit) {
    Box(modifier = Modifier
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
                Image(painter = painterResource(id = R.drawable.people), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(PURPLE) )
                Text(text = "300k" , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCommentClicked() }) {
                Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text = "144k" , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.war_room), contentDescription ="", modifier = Modifier.size(30.dp),colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text = "100" , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Button(onClick = {  }, modifier = Modifier.height(40.dp),shape= RoundedCornerShape(8.dp),colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)) {
                Text(text = "+JOIN", fontFamily = DONGLE_BOLD, fontSize = 20.sp,color=Color.White)
            }
        }}
}

@Composable
fun OfferRequest() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 20.dp, max = 200.dp)
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
            .clip(RoundedCornerShape(4.dp)) // Clip first to ensure proper rounded corners
            .background(Color.White) // Background should be applied after clip()
            .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(4.dp)) // Border after background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(4.dp)) // Ensures the inner content also respects rounded corners
                .background(Color(0xFFB6D6F5)) // Correctly applied to fit inside the Box
                .padding(start = 6.dp, end = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Need 4 people for going to club",
                fontSize = 30.sp,
                fontFamily = DONGLE_BOLD
            )
            Image(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun OfferLocation() {
    Box(modifier = Modifier
        .padding(start = 16.dp)
        .fillMaxWidth()
        .height(40.dp)){
        Row(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.location_new), contentDescription = "", modifier = Modifier.size(20.dp))
            Text(text = "Cannaught Place , New Delhi", fontFamily = DONGLE_NORMAL, fontSize = 16.sp)
        }
    }
}

@Composable
fun DateAndTime() {
    Box(modifier = Modifier
        .padding(start = 16.dp, top = 6.dp)
        .wrapContentSize()
        .border(width = 0.5.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))
        .clip(shape = RoundedCornerShape(12.dp))){
      Column(modifier = Modifier
          .wrapContentSize()
          .padding(4.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
          Text("TONIGHT", fontFamily = DONGLE_BOLD, fontSize = 30.sp)
      }

    }
}
