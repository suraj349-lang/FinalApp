package com.example.finalapp.screens._2pings.old

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants

@Preview(showBackground = true)
@Composable
fun PersonalEvent() {
    val infiniteTransition = rememberInfiniteTransition(label = "gradientTransition")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // you can tweak this
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetY"
    )

    val animatedBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF065309), Color(0xFF868B05)),
        startY = offsetY,
        endY = offsetY + 300f // controls gradient length
    )
    Card(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White ),
        border = BorderStroke(width = 4.dp, brush = animatedBrush)){
        Column(modifier = Modifier
            .wrapContentSize()
            .padding(16.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(8.dp)) {

            UserImageNameTime()

            TypeLocationDate()

            com.example.finalapp.screens._2pings.CaptionHeader("")

            com.example.finalapp.screens._2pings.Caption("")

            EventImage() //optional

            ShareLikeJoin()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserImageNameTime() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier.size(50.dp), shape = CircleShape) {
                Image(painter = painterResource(id = R.drawable.profile_image_3), contentDescription ="", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "suraj__3494", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp)
        }

        Text(text = "8 hrs.", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp)
    }
}


@Composable
fun TypeLocationDate() {
    Row(modifier = Modifier
        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Card(modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color(
            0xFF42810B
        )
        )) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.personal),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Dating", fontSize = 18.sp, color = Color.White ,fontWeight = FontWeight.Bold,fontFamily = Constants.FONT_MEDIUM)
            }
        }

        Text(text = "Delhi", fontSize = 18.sp, fontFamily = Constants.FONT_MEDIUM)
        Text(text = "Tonight", fontSize = 18.sp, fontFamily = Constants.FONT_MEDIUM)
    }
}

@Composable
fun CaptionHeader(title:String="") {
    val title="Skip the Small Talk"
    Text(
        text =title.replaceFirstChar { it.uppercase() },
        fontSize = 20.sp,fontWeight = FontWeight.SemiBold,
        fontFamily = Constants.FONT_EXTRA_LIGHT,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )

}

@Composable
fun Caption(subtitle:String="") {
    val subtitle="We’re all here for the same reason — something meaningful."
    Text(
        text = subtitle,
        fontSize = 14.sp,fontWeight = FontWeight.SemiBold,
        fontFamily = Constants.FONT_EXTRA_LIGHT,
        maxLines = 4,
        color= Color.Gray,
        overflow = TextOverflow.Ellipsis
    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EventImage() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp), colors = CardDefaults.cardColors(containerColor = Color.Black)) {
        Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="", modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f) )
    }
}


@Composable
fun ShareLikeJoin(onJoinClicked:()->Unit={},onCommentClicked:()->Unit={}) {
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
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            Column( horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                Image(painter = painterResource(id = R.drawable.people), contentDescription ="", modifier = Modifier.size(24.dp), colorFilter = ColorFilter.tint(
                    Color.DarkGray) )
                Text(text = "20" , fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL, color = Color.DarkGray)
            }
//            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCommentClicked() }) {
//                Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(
//                    Color.DarkGray) )
//                Text(text = "144k" , fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL, color = Color.DarkGray)
//            }
//            Column( horizontalAlignment = Alignment.CenterHorizontally) {
//                Image(painter = painterResource(id = R.drawable.war_room), contentDescription ="", modifier = Modifier.size(30.dp),colorFilter = ColorFilter.tint(
//                    Color.DarkGray) )
//                Text(text = "100" , fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL, color = Color.DarkGray)
//            }
              Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCommentClicked() }) {
                Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier.size(24.dp), colorFilter = ColorFilter.tint(
                    Color.DarkGray) )
                Text(text = "144" , fontSize = 12.sp, fontFamily = Constants.DONGLE_NORMAL, color = Color.DarkGray)
            }
//            Button(onClick = {onJoinClicked() }, modifier = Modifier.height(40.dp),shape= RoundedCornerShape(8.dp),colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)) {
//                Text(text = "+JOIN", fontFamily = Constants.DONGLE_BOLD, fontSize = 20.sp,color= Color.White)
//            }
            AnimatedJoinButton({})
        }
    }
}

@Composable
fun AnimatedJoinButton(onJoinClicked: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "colorTransition")

    val backgroundColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFC2185B), // start dark gray
        targetValue = Color(0xFF275004),   // animated gold
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonBackground"
    )

    Button(
        onClick = { onJoinClicked() },
        modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(
            text = "+ CONNECT",
            fontFamily = Constants.FONT_MEDIUM,
            fontSize = 12.sp,
            color = Color.White
        )
    }
}

