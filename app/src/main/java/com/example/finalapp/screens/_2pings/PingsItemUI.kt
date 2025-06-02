package com.example.finalapp.screens._2pings


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
import androidx.compose.material.Divider
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
import com.example.finalapp.screens._2pings.old.EventImage
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import kotlinx.coroutines.delay


@Preview
@Composable
fun PingsItemUI() {

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(width = 1.dp, color = floatingActionBtnColor.copy(alpha = 0.5f))
    ) {
        Column {
            UserImageNameTime2()
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TypeLocationDate2()
              //  StatusBadge(status = "Active", urgency = "Expiring soon")
                EventImage()
                CaptionHeader()
                Caption()
                CountdownTimer(eventTimeMillis = System.currentTimeMillis() + 3600000L) // 1 hour from now
                Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
                ShareLikeRespond(onRespondClicked = { /* handle response */ })
            }
        }
    }
}

@Composable
fun UserImageNameTime2() {
    Row(
        modifier = Modifier
            .background(color = floatingActionBtnColor)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier.size(40.dp), shape = CircleShape) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image_3),
                    contentDescription = null,
                    modifier=Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text("suraj__3494", fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Text("8 hrs ago", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp, color = Color.White)
    }
}



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
        InfoChip(text = "Dating", backgroundColor = floatingActionBtnColor)
        InfoChip(iconRes = R.drawable.location_new, text = "Delhi", backgroundColor = Color(0xFF3949AB))
        InfoChip(
            iconRes = R.drawable.clock_filled,
            text = "In ${hours}h ${minutes}m",
            backgroundColor = Color(0xFFFB8C00)
        )
    }
}

@Composable
fun InfoChip(iconRes: Int? = null, text: String, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(iconRes !=null) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .padding(end = 4.dp)
            )
        }
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White,
            fontFamily = Constants.FONT_MEDIUM
        )
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
            color = if (status == "Active") Color.Green else Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun CaptionHeader() {
    Card(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(), colors = CardDefaults.cardColors(containerColor = Color.LightGray), shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            "Pick Your Vibe Partner",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Constants.FONT_EXTRA_LIGHT,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }

}

@Composable
fun Caption() {
    Text(
        "Post your interest. Wait for others to respond. Choose who you want to go out with!",
        fontSize = 14.sp,
        color = Color.DarkGray,
        fontFamily = Constants.FONT_EXTRA_LIGHT,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
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




@Composable
fun ShareLikeRespond(onRespondClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
//        ResponderAvatars(
//            responderImageIds = listOf(
//                R.drawable.profile_image_1,
//                R.drawable.profile_image_2,
//                R.drawable.profile_image_3
//            )
//        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(top=8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconWithLabel(R.drawable.people, "20")
            IconWithLabel(R.drawable.share, "Share")
        }
        RespondButton {}
       // AnimatedRespondButton(onClick = onRespondClicked)
    }
}


@Composable
fun IconWithLabel(iconRes: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.Black)
        )
        Text(text = label, fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL, color = Color.Black)
    }
}



@Composable
fun RespondButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 8.dp)
            .height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)
    ) {
        Text(
            text = "RESPOND",
            fontFamily = Constants.FONT_MEDIUM,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}
