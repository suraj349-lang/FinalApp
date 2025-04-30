package com.example.finalapp.screens._1home.privateEvent

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.EventResponse
import com.example.finalapp.screens._1home._1_1ExperimentScreenEvents.ActiveButton
import com.example.finalapp.screens._1home._1_1ExperimentScreenEvents.DONGLE
import com.example.finalapp.screens._1home._1_1ExperimentScreenEvents.SANS
import com.example.finalapp.screens._1home._1_1ExperimentScreenEvents.images
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PrivateEvent(event: EventResponse, navController: NavHostController) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    Log.d("EventData", "PrivateEvent:${event} ")

//    SideEffect {
//        window.statusBarColor = Color.White.toArgb()
//        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
//    }
        Surface(modifier = Modifier
            .fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()){
                    // IMAGE
                    GlideImage(model = event.image.ifEmpty { R.drawable.girl }, contentDescription = "",
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f), contentScale = ContentScale.Crop)
                    //--------------------active button and expiration time----------------------
                    Box(modifier = Modifier.align(Alignment.TopStart)){
                        ActiveButtonAndExpirationTime()
                    }
                    //---------------------------------------------------------------------------
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter)) {
                        //------------------ username and user image----------------------------------------------------
                        UsernameAndUserProfileImage(event.user?.username)

                        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)

                        //----------------- Event comments and join button bar------------------------------------------
                        Row(modifier= Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                                shape= RoundedCornerShape(0.dp)
                                , colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f))) {
                                EventCommentsAndJoinButtonBar()
                            }
                        }
                        //--------------event category and offer -----------------------------------------------------------

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(), shape = RoundedCornerShape(0.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .height(60.dp)
                                            .width(60.dp)
                                    ) {
                                        EventCategory(event.category)
                                    }
                                    Card(
                                        modifier = Modifier
                                            .width(350.dp)
                                            .height(80.dp), shape = RoundedCornerShape(0.dp)
                                    ) {
                                        Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top) {
                                            event.offer?.let { EventOffer(it) }
                                        }
                                    }

                                }
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(24.dp), shape = RoundedCornerShape(0.dp)
                                ) {
                                    RunningText4(event.location)
                                }
                            }
                        }
                        //------hot this weekend-----------------------------------------------------------------

                        Row(modifier= Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp), shape = RoundedCornerShape(0.dp), colors = CardDefaults.cardColors(containerColor = Color(
                                0xFFF3EA97
                            )
                            )) {
                                HotThisWeekendBanner4()
                            }
                        }
                        //----------------------------------------------------------------------------------------------
                        Divider(modifier= Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp), thickness = 0.5.dp, color = Color.LightGray)

                    }
                }
            }
        }
}

@Composable
fun RunningText4(location:String) {
    val textWidth = remember { mutableStateOf(0f) }
    val containerWidth = remember { mutableStateOf(0f) }

    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(textWidth.value, containerWidth.value) {
        if (textWidth.value > 0 && containerWidth.value > 0) {
            while (true) {
                offsetX.snapTo(containerWidth.value)
                offsetX.animateTo(
                    targetValue = -textWidth.value,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 8000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }
    }
//0xFF41074B  0xFF8D5CE5
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RectangleShape)
            .background(Color(0xFF077CDA))
            .onGloballyPositioned { coordinates ->
                containerWidth.value = coordinates.size.width.toFloat()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row (modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(offsetX.value.toInt(), 0) }
            .onGloballyPositioned { coordinates ->
                textWidth.value = coordinates.size.width.toFloat()
            }, verticalAlignment = Alignment.Top){
            Image(painter = painterResource(R.drawable.location_new), contentDescription = "", modifier = Modifier.size(20.dp))
            Text(
                text = location,
                color = Color.White, //0xFF03A9F4
//                fontFamily = SANS,
                modifier = Modifier,
                fontSize = 12.sp,
            )
        }
    }
}


@Composable
fun ActiveButtonAndExpirationTime() {
    Column(modifier = Modifier
        .wrapContentSize()
        .padding(8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            ActiveButton()
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Card(modifier=Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color(
                0xFF1976D2
            ).copy(alpha = 0.9f)),
                shape = RoundedCornerShape(7.dp), border = BorderStroke(width = 1.dp,color= Color(
                    0xFF71C4F1))) {
                Text("08 hrs.", modifier = Modifier.padding(4.dp), color = Color.White, fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp)}
        }

//        Column(modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()) {
//            Image(painter = painterResource(R.drawable.send), contentDescription = "", modifier = Modifier.size(30.dp))
//            Text("100k")
//        }
    }
}

@Composable
fun EventCategory(category:String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 4.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.dating),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(modifier=Modifier.fillMaxWidth(), color = Color.White)
        Spacer(modifier = Modifier.height(2.dp))
        Text(category, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun ApproveOrJoin4(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.join_blue),
            contentDescription = "",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(modifier=Modifier.fillMaxWidth(), color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text("JOIN", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun UsernameAndUserProfileImage(username:String?) {
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 8.dp, end = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0f)), shape = CircleShape
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Card(modifier = Modifier.size(40.dp), shape = CircleShape) {
                    Image(
                        painter = painterResource(R.drawable.girl),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(username ?: "testing",
                    fontFamily = DONGLE,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = Color.Black.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(R.drawable.verified),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(bottom = 9.dp)
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EventOffer(offer:String) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFFFFFFFF).copy(alpha = 0.6f))
        .padding(top = 6.dp)) {
        Text(offer,  fontFamily = SANS,maxLines = 4, fontSize = 12.sp, lineHeight = 16.sp, overflow = TextOverflow.Ellipsis, color = Color(0xFF2196F3),fontWeight = FontWeight.SemiBold,modifier = Modifier.padding(start = 4.dp,end=4.dp))
//        Text("see more...", fontFamily = DONGLE,modifier = Modifier
//            .padding(end = 4.dp)
//            .align(Alignment.BottomEnd), color = Color(0xFF2196F3))
    }

}

@Composable
fun EventCommentsAndJoinButtonBar() {
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(start = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.people),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text("Joined", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.comment),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text("Comment", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.info),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text("Details", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.share),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text("Share", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Card(modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.join_blue),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(horizontal = 4.dp)
                )
                // Spacer(modifier = Modifier.weight(0.1f))
                Text(
                    "JOIN",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = SANS
                )
            }
        }
    }
}
@Composable
fun HotThisWeekendBanner4(modifier: Modifier = Modifier) {
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(start = 4.dp)) {
        Text("Hot this Weekend :", fontFamily = DONGLE, color = Color(0xFF010F1A))
        LazyRow(modifier=Modifier.fillMaxSize(), verticalAlignment = Alignment.Top) {
            items(images){ image->
                HotThisWeekendBannerItem4(image)
            }
        }
    }
}

@Composable
fun HotThisWeekendBannerItem4(image: String) {
    Card (modifier= Modifier
        .size(56.dp)
        .padding(end = 2.dp), shape = RoundedCornerShape(12.dp)){
        AsyncImage(model = image, contentDescription = "",modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
    }
}