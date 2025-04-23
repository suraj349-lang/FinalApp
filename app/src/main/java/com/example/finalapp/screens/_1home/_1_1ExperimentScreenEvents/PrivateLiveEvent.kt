package com.example.finalapp.screens._1home._1_1ExperimentScreenEvents



import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.screens._1home.commentBottomSheet.CommentBottomSheet

@Composable
fun PrivateLiveEvent() {
    var showCommentBottomSheet by remember {
        mutableStateOf(false)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Image(painter = painterResource(R.drawable.girl), contentDescription = "",
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.align(Alignment.TopStart)) {
                    ActiveButtonUi()
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)) {
                    UserImageAndUsername()
                    EventTypeCaptionAndJoin()
                    CommentAndJoinedInfoShare(){showCommentBottomSheet=!showCommentBottomSheet}
                    EventHotThisWeekend()
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp), thickness = 0.5.dp, color = Color.LightGray)
                }
            }
        }
    }
    CommentBottomSheet(showSheet = showCommentBottomSheet) {
        showCommentBottomSheet=false
    }
}

@Composable
fun UserImageAndUsername() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(44.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)), shape = RoundedCornerShape(0.dp)
    )
         {
        UserImageAndUsernameUi()
    }
}

@Composable
fun UserImageAndUsernameUi() {
    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Card (modifier = Modifier.size(40.dp), shape = CircleShape){
            Image(painter = painterResource(R.drawable.girl), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text("@jiyaShankar24", fontFamily = DONGLE, fontWeight = FontWeight.SemiBold,fontSize = 24.sp, color = Color.Black.copy(alpha = 0.9f))
        Spacer(modifier = Modifier.width(12.dp))
        Image(painter = painterResource(R.drawable.verified), contentDescription = "", modifier = Modifier.size(20.dp))
    }
}

@Composable
fun EventTypeCaptionAndJoin() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp), horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Card(modifier = Modifier
            .height(100.dp)
            .width(90.dp)) {
            EventTypeV2()
        }
        Column {
            Card(modifier = Modifier
                .width(195.dp)
                .height(80.dp)) {
                Column(modifier = Modifier.wrapContentSize()) {
                    EventCaptionV2()
                }
            }
            Card(
                modifier = Modifier
                    .width(195.dp)
                    .height(24.dp)
                    .padding(top = 2.dp)
            ) {
                RunningTextV2()
            }
        }
        Card(modifier = Modifier.size(100.dp)) {
            ApproveOrJoinV2()
        }
    }

}

@Composable
fun CommentAndJoinedInfoShare(onCommentClicked:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 4.dp, end = 4.dp, bottom = 2.dp)
        ) {
            ItemsBarV2(onCommentClicked)
        }
    }

}

@Composable
fun EventHotThisWeekend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(start = 4.dp, end = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(
                    0xFFF3EA97
                )
            )
        ) {
            HotThisWeekendBanner()
        }
    }

}
@Composable
fun RunningTextV2(modifier: Modifier = Modifier) {
    val text = "Cannaught place,Delhi"
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
        modifier = modifier
            .fillMaxSize()
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
                text = text,
                color = Color.White, //0xFF03A9F4
//                fontFamily = SANS,
                modifier = Modifier,
                fontSize = 12.sp,
            )
        }
    }
}
val DONGLE=FontFamily(Font( R.font.dongle_bold))
val SANS= FontFamily(Font(R.font.work_sans_regular))

@Composable
fun ActiveButtonUi() {
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
fun EventTypeV2(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.dating),
            contentDescription = "",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(modifier=Modifier.fillMaxWidth(0.9f), color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Dating", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
    }
}

@Composable
fun ApproveOrJoinV2(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.join_blue),
            contentDescription = "",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(modifier=Modifier.fillMaxWidth(0.9f), color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text("JOIN", fontWeight = FontWeight.Bold, fontSize = 14.sp,color = Color.Black)
    }
}

@Composable
fun EventLocationV2() {
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
///0xFF9465E8  ,0xFFCDDC39  in use 0xFF2196F3
@Composable
fun EventCaptionV2(modifier: Modifier = Modifier) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFFFFFFFF).copy(alpha = 0.6f))
        .padding(top = 6.dp)) {
        Text("Hosting a social night out — chill vibes, great music, and even better company 🎧🍸 Ladies, who’s in?",  fontFamily = SANS,maxLines = 4, fontSize = 12.sp, lineHeight = 16.sp, overflow = TextOverflow.Ellipsis, color = Color(0xFF2196F3),fontWeight = FontWeight.SemiBold,modifier = Modifier.padding(start = 4.dp,end=4.dp))
//        Text("see more...", fontFamily = DONGLE,modifier = Modifier
//            .padding(end = 4.dp)
//            .align(Alignment.BottomEnd), color = Color(0xFF2196F3))
    }

}


@Composable
fun ItemsBarV2(onCommentClicked: () -> Unit) {
    Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.people), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Joined", fontSize = 8.sp)
        }
        Column(modifier = Modifier.clickable { onCommentClicked() }
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.comment), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Comment", fontSize = 8.sp)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.info), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Details", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.share), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Share", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ItemsBarV3() {
    Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.people), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Joined", fontSize = 8.sp)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.comment), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Comment", fontSize = 8.sp)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.info), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Details", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
        Column(modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.share), contentDescription = "", modifier = Modifier.size(24.dp))
            Text("Share", fontSize = 8.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
@Composable
fun HotThisWeekendBanner(modifier: Modifier = Modifier) {
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(start = 2.dp, end = 2.dp)) {
        Text("\uD83D\uDD25 Hot this weekend :", fontFamily = DONGLE, color = Color(0xFF010F1A))
        LazyRow(modifier=Modifier.fillMaxSize(), verticalAlignment = Alignment.Top) {
            items(images){image->
                HotThisWeekendBannerItem(image)
            }
        }
    }
}

@Composable
fun HotThisWeekendBannerItem(image: String) {
    Card (modifier= Modifier
        .size(56.dp)
        .padding(end = 2.dp), shape = RoundedCornerShape(12.dp)){
        AsyncImage(model = image, contentDescription = "",modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
    }
}
val images = listOf(
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg",  // Woman with curly hair smiling
    "https://images.pexels.com/photos/2379005/pexels-photo-2379005.jpeg", // Man with beard and glasses
    "https://images.pexels.com/photos/247932/pexels-photo-247932.jpeg",   // Woman with short hair laughing
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg",  // Man with afro hairstyle
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg",   // Woman with long hair outdoors
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg", // Man with a hat and beard
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg",   // Woman with glasses smiling
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg",   // Man with short hair and blue eyes
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg", // Woman with blonde hair and red lipstick
    "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg"    // Man with dark hair and a serious expression
)