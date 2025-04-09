package com.example.finalapp.screens._1home._1_1Events

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R

@Composable
fun NewPrivateUI2(modifier: Modifier = Modifier) {
        Surface(modifier = Modifier
            .fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()){
                    Image(painter = painterResource(R.drawable.girl), contentDescription = "",Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f), contentScale = ContentScale.FillHeight)
                    Box(modifier = Modifier.align(Alignment.TopStart)){ EventActions()}
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter)) {
                        Row(modifier=Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(start = 8.dp, end = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))) {
                                UserDetails()
                            }
                        }
                        Row(modifier=Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Card(modifier = Modifier
                                .height(100.dp)
                                .width(90.dp)) {
                                EventType()
                            }
                            Column {
                                Card(modifier = Modifier
                                    .width(195.dp)
                                    .height(80.dp)) {
                                    Column(modifier = Modifier.wrapContentSize()) {
                                        EventLocation()
                                        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.White)
                                        EventCaption()
                                    }

                                }
                                Card(modifier = Modifier
                                    .width(195.dp)
                                    .height(20.dp)
                                    .padding(top = 2.dp)) {
                                    RunningText()
                                }
                            }
                            Card(modifier = Modifier.size(100.dp)) {
                                ApproveOrJoin()
                            }
                        }
                        Row(modifier=Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(start = 8.dp, end = 8.dp, bottom = 4.dp)) {
                                ItemsBar()
                            }
                        }
                        Row(modifier=Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(start = 8.dp, end = 8.dp)) {
                            }
                        }
                        Divider(modifier=Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp), thickness = 1.dp, color = Color.DarkGray)

                    }
                }
            }
        }

    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("Social", color = Color.White, fontFamily = SANS, fontWeight = FontWeight.Bold) },
        actions = {
            Row(modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(end = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Image(
                    painter = painterResource(R.drawable.notification),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 4.dp), colorFilter = ColorFilter.tint(Color.White)
                )
                Image(
                    painter = painterResource(R.drawable.qr),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.White)
                )
                Image(
                    painter = painterResource(R.drawable.send_24),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.White)
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2B0533))
    )

}

@Composable
fun ActionsColumn(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.wrapContentSize()){

    }
}
@Composable
fun RunningText(modifier: Modifier = Modifier) {
    val text = "Event live till 8pm."
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

    Box(
        modifier = modifier
            .height(30.dp)
            .fillMaxWidth()
            .clip(RectangleShape)
            .background(Color(0xFF41074B))
            .onGloballyPositioned { coordinates ->
                containerWidth.value = coordinates.size.width.toFloat()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .onGloballyPositioned { coordinates ->
                    textWidth.value = coordinates.size.width.toFloat()
                },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EventActions() {
    Column(modifier = Modifier
        .wrapContentHeight()
        .width(80.dp)
        .padding(8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            ActiveButton()
        }
//        Column(modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()) {
//            Image(painter = painterResource(R.drawable.send), contentDescription = "", modifier = Modifier.size(30.dp))
//            Text("100k")
//        }
//        Column(modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()) {
//            Image(painter = painterResource(R.drawable.send), contentDescription = "", modifier = Modifier.size(30.dp))
//            Text("100k")
//        }
    }
}

@Composable
fun EventType(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.dating),
            contentDescription = "",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Dating", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun ApproveOrJoin(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.join_blue),
            contentDescription = "",
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("JOIN", fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun UserDetails(modifier: Modifier = Modifier) {
    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Card (modifier = Modifier.size(50.dp), shape = CircleShape){
            Image(painter = painterResource(R.drawable.girl), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text("@jiyaShankar24", fontWeight = FontWeight.SemiBold,fontSize = 18.sp, color = Color.Black.copy(alpha = 0.9f))
        Spacer(modifier = Modifier.width(12.dp))
        Image(painter = painterResource(R.drawable.verified), contentDescription = "", modifier = Modifier.size(20.dp))
    }
}

@Composable
fun EventLocation() {
    Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
        Image(
            painter = painterResource(R.drawable.location_new),
            contentDescription = "",
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cannaught place,Delhi", fontSize = 12.sp,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            modifier = Modifier.padding(top=6.dp),fontWeight = FontWeight.Bold, color = Color(
                0xFF0584EA
            )
        )
    }
}
///0xFF9465E8  ,0xFFCDDC39
@Composable
fun EventCaption(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color(0xFF2196F3))) {
        Text("Hosting a social night out — chill vibes, great music, and even better company 🎧🍸 Ladies, who’s in?", maxLines = 3, overflow = TextOverflow.Ellipsis, color = Color.White,fontWeight = FontWeight.SemiBold,modifier = Modifier.padding(start = 4.dp,end=4.dp))
        Text("see more...", modifier = Modifier.padding(end=4.dp, bottom = 4.dp).align(Alignment.BottomEnd), color = Color.DarkGray)
    }

}

@Composable
fun ActiveButton(modifier: Modifier = Modifier) {
    Card(modifier=Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color(0xFF5B0505).copy(alpha = 0.9f)), shape = RoundedCornerShape(7.dp)) {
        Text("ACTIVE", modifier = Modifier.padding(4.dp), color = Color.White, fontFamily = SANS, fontSize = 14.sp)}
}

@Composable
fun ItemsBar() {
    Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceEvenly){
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.people), contentDescription = "", modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Joined", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.comment), contentDescription = "", modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Comment", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.info), contentDescription = "", modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Details", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.share), contentDescription = "", modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Share", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}