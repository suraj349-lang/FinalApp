package com.example.finalapp.screens._1home.EventAndPingDesigns.pings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.getFormattedTimeAndFlag
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
@Composable
fun PingsTinderScreen(item:PingResponse) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Constants.HOME_TOP_BAR_COLOR,
                        Color.Black
                    )
                )
            )
            .fillMaxWidth()
    ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
                Box(modifier = Modifier
                    .wrapContentSize()
                    .wrapContentHeight()
                    .background(Color.Black)){
                    Row(modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .shadow(
                                elevation = 20.dp,
                                spotColor = Color.White,
                                ambientColor = Color.White
                            )) {
                            AsyncImage(model = imagePrefix+item.user?.profileImage, contentDescription ="", modifier = Modifier
                                .fillMaxSize(), contentScale = ContentScale.Crop , filterQuality = FilterQuality.High)
                        }

                        Column(modifier = Modifier.wrapContentSize()) {
                            item.user?.name?.let { Text(text = it, fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM,fontSize = 16.sp, color = Color(0xFFFFFFFF).copy(alpha = 0.9f), lineHeight = 12.sp) }
                            item.user?.userName?.let { Text(text = it, fontWeight = FontWeight.Normal, fontSize = 12.sp, fontFamily = Constants.FONT_LIGHT, color = Color.LightGray, lineHeight = 12.sp) }
                        }
//                        Box(modifier = Modifier
//                            .wrapContentWidth()
//                            .wrapContentHeight()
//                            .clip(shape = RoundedCornerShape(50))
//                            .background(color = Color(0xFFAF340D))) {
//                            Text("${item.category}", color = Color(0xFFFFFEFC),fontFamily = Constants.FONT_LIGHT, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
//                        }


                    }

                    Box(modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .background(color = Color(0x00000000))) {  //
                        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                            Text(item.category, color = Color(0xFFFAB815),fontFamily = Constants.USER_NAME_FONT, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
                            Image(painter = painterResource(id = R.drawable.menu), contentDescription ="", modifier = Modifier
                                .rotate(90f)
                                .size(15.dp), colorFilter = ColorFilter.tint(
                                Color.White) )
                        }
                    }
//                    Box(modifier = Modifier
//                        .wrapContentWidth().align(Alignment.CenterEnd)
//                        .wrapContentHeight()
//                        .clip(shape = RoundedCornerShape(50))
//                        .background(color = Color(0xFF1976D2))) {
//                        Text("Follow", color = Color(0xFFFFFEFC),fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
//                    }

                }
                Box(modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black, Color.Black
                            )
                        )
                    )
                    .fillMaxWidth()
                    .wrapContentHeight()
                    ){
                    AsyncImage(
                        model =  imagePrefix+ item.image,
                        contentDescription = "Ping Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(0.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // controls how tall the fade is
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.Black.copy(alpha = 1f)
                                    )
                                )
                            )
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment=Alignment.Bottom,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(color = Color.Transparent)
                    ) {

                        LikeRoundUI()
                        CommentRoundUI()
                        CountdownTimer(item.expirationTime)
                        JoinRoundUI()
                        ChatRoundUI()


                    }
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 8.dp)){
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {
                        Text(text = item.title.toString(), fontWeight = FontWeight.Normal, fontFamily = Constants.USER_NAME_FONT,fontSize = 18.sp, color = Color(0xFFE9E9E9).copy(alpha = 01f))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = item.description.toString().capitalize(), fontWeight = FontWeight.Normal, fontSize = 13.sp, fontFamily = Constants.FONT_LIGHT, color = Color.LightGray.copy(alpha=0.9f))
                    }

                }


            }


    }
    }

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CountdownTimer(expirationIso: String) {
    var timeLeft by remember { mutableStateOf("") }
    var isLessThanHour by remember { mutableStateOf(false) }

    LaunchedEffect(expirationIso) {
        while (true) {
            val (formatted, lessThanHour) = getFormattedTimeAndFlag(expirationIso)
            timeLeft = formatted
            isLessThanHour = lessThanHour
            delay(1000) // tick every second
        }
    }

    RoundUI(time = timeLeft, isLessThanHour = isLessThanHour)
}


@Composable
fun RoundUI(time: String, isLessThanHour: Boolean) {
    val containerColor = when {
        time == "Expired" -> Color.Gray
        isLessThanHour -> Color(0xFF940707)
        else -> Color(0xFF888D0B)///0xFF545707
    }

    Card(
        modifier = Modifier.size(60.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = time,
                fontWeight = FontWeight.Bold,
                fontFamily = Constants.FONT_MEDIUM,
                fontSize = 13.sp,
                maxLines = 2, color = Color.White
            )
        }
    }
}


@Composable
fun ChatRoundUI() {
    Card(
        onClick = {},
        modifier=Modifier.size(50.dp),
        shape = CircleShape, colors = CardDefaults.cardColors(containerColor =  Constants.HOME_TOP_BAR_COLOR)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.chat_new), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                Color.White.copy(alpha = 0.954f)) )
            //  Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
        }

    }
}
@Composable
fun JoinRoundUI() {
    Card(
        onClick = {},
        modifier=Modifier.size(50.dp),
        shape = CircleShape, colors = CardDefaults.cardColors(containerColor =  Constants.HOME_TOP_BAR_COLOR)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.join_blue), contentDescription ="", modifier = Modifier.size(30.dp) )
        }

    }
}
@Composable
fun CommentRoundUI() {
    Card(
        onClick = {},
        modifier=Modifier.size(50.dp),
        shape = CircleShape, colors = CardDefaults.cardColors(containerColor =  Constants.HOME_TOP_BAR_COLOR)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.comment), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                Color.White.copy(alpha = 0.954f)) )
            //  Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
        }

    }
}
@Composable
fun LikeRoundUI() {
    Card(
        onClick = {},
        modifier=Modifier.size(50.dp),
        shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Constants.HOME_TOP_BAR_COLOR)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.heart), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                Color.White.copy(alpha = 0.954f)) )
            //  Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
        }

    }
}