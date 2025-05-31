package com.example.finalapp.screens._2personalEvents


import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.finalapp.R
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import kotlinx.coroutines.delay


@Preview(showBackground = true)
@Composable
fun PersonalEvent2() {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF065309), Color(0xFF868B05)),
        startY = offsetY,
        endY = offsetY + 300f
    )

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(width = 4.dp, brush = animatedBrush)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserImageNameTime2()
            TypeLocationDate2()
            StatusBadge(status  = "Active", urgency = "")
            CaptionHeader()
            Caption()
            CountdownTimer(eventTimeMillis = System.currentTimeMillis() + 3600000L) // 1 hour from now
//
//            ResponderAvatars(
//                responderImageIds = listOf(
//                    R.drawable.profile_image_1,
//                    R.drawable.profile_image_2,
//                    R.drawable.profile_image_3
//                )
//            )

            EventImage()
         //   EventStats(countdownTime = "1h 23m", interestedUsers = 14)
            ShareLikeRespond(onRespondClicked = { /* handle response */ })
        }
    }
}

@Composable
fun StatusBadge(status: String?, urgency: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        status?.let {
            Box(
                modifier = Modifier
                    .background(Color.Yellow, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Text(
            text = urgency,
            color = if (status == "OPEN") Color.Green else Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun CountdownTimer(eventTimeMillis: Long) {
    var timeLeft by remember { mutableStateOf(eventTimeMillis - System.currentTimeMillis()) }

    LaunchedEffect(eventTimeMillis) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft = eventTimeMillis - System.currentTimeMillis()
        }
    }

    val hours = (timeLeft / (1000 * 60 * 60)).toInt()
    val minutes = ((timeLeft / (1000 * 60)) % 60).toInt()
    val seconds = ((timeLeft / 1000) % 60).toInt()

    if (timeLeft > 0) {
        Text(
            text = String.format("\u23F3 %02dh %02dm %02ds left", hours, minutes, seconds),
            fontSize = 14.sp,
            color = Color.Red,
            fontFamily = Constants.FONT_MEDIUM
        )
    } else {
        Text(text = "\uD83C\uDF89 Event Started!", fontSize = 14.sp, color = Color.Green)
    }
}
//
//@Composable
//fun ResponderAvatars(responderImageIds: List<Int>) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .horizontalScroll(rememberScrollState()),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        responderImageIds.forEach { imageId ->
//            Image(
//                painter = painterResource(id = imageId),
//                contentDescription = "Responder Avatar",
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
//        }
//    }
//}
@Composable
fun ResponderAvatars(responderImageIds: List<Int>) {
    Box(modifier = Modifier.height(40.dp)) {
        responderImageIds.forEachIndexed { index, imageId ->
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .offset(x = (index * 24).dp) // 24dp instead of 40dp to create overlap
                    .clip(CircleShape)
                    .zIndex(index.toFloat()), // Ensure correct layering
                contentScale = ContentScale.Crop
            )
        }
    }
}


//
//@Composable
//fun StatusBadge(status: String, urgency: String) {
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(10.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = status,
//            color = if (status == "OPEN") Color.Green else Color.Red,
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Bold
//        )
//        Box(
//            modifier = Modifier
//                .background(Color.Yellow, shape = RoundedCornerShape(8.dp))
//                .padding(horizontal = 8.dp, vertical = 4.dp)
//        ) {
//            Text(
//                text = urgency,
//                fontSize = 12.sp,
//                color = Color.Black,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//    }
//}

@Composable
fun EventStats(countdownTime: String, interestedUsers: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "⏰ $countdownTime left to respond",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Red
        )
        Text(
            text = "$interestedUsers interested",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF42810B)
        )
    }
}

@Composable
fun ShareLikeRespond(onRespondClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ResponderAvatars(
            responderImageIds = listOf(
                R.drawable.profile_image_1,
                R.drawable.profile_image_2,
                R.drawable.profile_image_3
            )
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconWithLabel(R.drawable.people, "20")
            IconWithLabel(R.drawable.share, "Share")
        }
        AnimatedRespondButton(onClick = onRespondClicked)
    }
}

@Composable
fun IconWithLabel(iconRes: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.DarkGray)
        )
        Text(text = label, fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL, color = Color.DarkGray)
    }
}

@Composable
fun AnimatedRespondButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val backgroundColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF275004),
        targetValue = Color(0xFFC2185B),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Button(
        onClick = onClick,
        modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(
            text = "RESPOND",
            fontFamily = Constants.FONT_MEDIUM,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}


@Composable
fun UserImageNameTime2() {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier.size(50.dp), shape = CircleShape) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image_3),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text("suraj__3494", fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Text("8 hrs ago", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp)
    }
}

//@Composable
//fun TypeLocationDate2() {
//    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF42810B))) {
//            Row(
//                modifier = Modifier.padding(6.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Image(painter = painterResource(id = R.drawable.personal), contentDescription = null, modifier = Modifier.size(20.dp))
//                Text("Dating", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold, fontFamily = Constants.FONT_MEDIUM)
//            }
//        }
//        Text("Delhi", fontSize = 18.sp, fontFamily = Constants.FONT_MEDIUM)
//        Text("Tonight", fontSize = 18.sp, fontFamily = Constants.FONT_MEDIUM)
//    }
//}

@Composable
fun TypeLocationDate2(eventTimeMillis: Long = System.currentTimeMillis() + 3600000L) {
    val timeRemaining = remember(eventTimeMillis) {
        eventTimeMillis - System.currentTimeMillis()
    }
    val hours = (timeRemaining / (1000 * 60 * 60)).toInt()
    val minutes = ((timeRemaining / (1000 * 60)) % 60).toInt()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoChip(iconRes = R.drawable.personal, text = "Dating", backgroundColor = Color(0xFF42810B))
        InfoChip(iconRes = R.drawable.location_new, text = "Delhi", backgroundColor = Color(0xFF3949AB))
        InfoChip(
            iconRes = R.drawable.clock_filled,
            text = "In ${hours}h ${minutes}m",
            backgroundColor = Color(0xFFFB8C00)
        )
    }
}

@Composable
fun InfoChip(iconRes: Int, text: String, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .padding(end = 4.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            fontFamily = Constants.FONT_MEDIUM
        )
    }
}


@Composable
fun CaptionHeader() {
    Text(
        "Pick Your Vibe Partner",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = Constants.FONT_EXTRA_LIGHT,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Caption() {
    Text(
        "Post your interest. Wait for others to respond. Choose who you want to go out with!",
        fontSize = 14.sp,
        color = Color.Gray,
        fontFamily = Constants.FONT_EXTRA_LIGHT,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun EventImage2() {
    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_image_1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun InterestStats() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatColumn(icon = R.drawable.people, label = "Interested", count = 27)
        StatColumn(icon = R.drawable.chat, label = "Chats", count = 12)
        StatColumn(icon = R.drawable.personal, label = "Likes", count = 35)
    }
}

@Composable
fun StatColumn(icon: Int, label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = icon), contentDescription = null, modifier = Modifier.size(24.dp))
        Text("$count", fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL)
        Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun ActionButtons(onJoinClicked: () -> Unit = {}) {
    val infiniteTransition = rememberInfiniteTransition(label = "colorTransition")
    val backgroundColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFC2185B),
        targetValue = Color(0xFF275004),
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse),
        label = "buttonBackground"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = onJoinClicked,
            modifier = Modifier.height(40.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
        ) {
            Text("+ CONNECT", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp, color = Color.White)
        }
    }
}
//@Composable
//fun CountdownTimer(eventTimeMillis: Long) {
//    var timeLeft by remember { mutableStateOf(eventTimeMillis - System.currentTimeMillis()) }
//
//    LaunchedEffect(eventTimeMillis) {
//        while (timeLeft > 0) {
//            delay(1000)
//            timeLeft = eventTimeMillis - System.currentTimeMillis()
//        }
//    }
//
//    val hours = (timeLeft / (1000 * 60 * 60)).toInt()
//    val minutes = ((timeLeft / (1000 * 60)) % 60).toInt()
//    val seconds = ((timeLeft / 1000) % 60).toInt()
//
//    if (timeLeft > 0) {
//        Text(
//            text = String.format("⏳ %02dh %02dm %02ds left", hours, minutes, seconds),
//            fontSize = 14.sp,
//            color = Color.Red,
//            fontFamily = Constants.FONT_MEDIUM
//        )
//    } else {
//        Text(text = "🎉 Event Started!", fontSize = 14.sp, color = Color.Green)
//    }
//}
//@Composable
//fun ResponderAvatars(responderImageIds: List<Int>) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .horizontalScroll(rememberScrollState()),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        responderImageIds.forEach { imageId ->
//            Image(
//                painter = painterResource(id = imageId),
//                contentDescription = "",
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
//        }
//    }
//}
