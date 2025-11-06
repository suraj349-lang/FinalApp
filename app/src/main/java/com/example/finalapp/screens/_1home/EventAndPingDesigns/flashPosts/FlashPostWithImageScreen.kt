package com.example.finalapp.screens._1home.EventAndPingDesigns.flashPosts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.screens._1home.commonUI.sharePingDeepLink
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.getFormattedTimeAndFlag
import kotlinx.coroutines.delay


@OptIn(ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
@Composable
fun FlashPostWithImageScreen(item:PingResponse) {
    val context= LocalContext.current
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
                    .clip(shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .wrapContentSize()
                    .wrapContentHeight()
                    .background(Color.Black)){
                    Row(modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(start = 10.dp)
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
                            Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                item.user?.name?.let { Text(text = it  , fontWeight = FontWeight.SemiBold, fontFamily = Constants.USER_NAME_FONT,fontSize = 16.sp, color = Color.LightGray, lineHeight = 12.sp) }
                                Text(text = "  :: " +item.category +" /public" , fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_LIGHT,fontSize = 16.sp, color = Color(
                                    0xFF689F38
                                ).copy(alpha = 0.9f), lineHeight = 12.sp)
                            }
                            Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                                Text(text = item.location, fontWeight = FontWeight.Normal, fontSize = 9.sp, fontFamily = Constants.FONT_LIGHT, color = Color.Gray, lineHeight = 1.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
//                                Card(modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.Gray)) {
//                                    Text(text = item.category, fontWeight = FontWeight.Normal, fontSize = 16.sp, fontFamily = Constants.FONT_MEDIUM, color = Color(
//                                        0xFF00060C
//                                    ), lineHeight = 1.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
//                                }
//                                Card(modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.Gray)) {
//                                    Text(text = "Public", fontWeight = FontWeight.Normal, fontSize = 16.sp, fontFamily = Constants.FONT_MEDIUM, color = Color(
//                                        0xFF060C00
//                                    ), lineHeight = 1.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
//                                }
                            }
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
                           // Text(item.category, color = Color(0xFFFAB815),fontFamily = Constants.USER_NAME_FONT, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
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
                        model = imagePrefix + item.image,
                        contentDescription = "Ping Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(9f / 12f) // or 16f / 9f, or 1f for square
                            .clip(RoundedCornerShape(12.dp)),
//                        placeholder = painterResource(id = R.drawable.loading),
//                        error = painterResource(id = R.drawable.error),
                        contentScale = ContentScale.Crop
                    )
                  //  Text(item.category, color = Color(0xFFFAB815),fontFamily = Constants.USER_NAME_FONT, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopStart).padding(horizontal = 16.dp, vertical = 4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
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

                        ViewRoundUI()
                        CommentRoundUI()
                        CountdownTimer(item.expirationTime)
                        JoinRoundUI()
                        ChatRoundUI(){
                            val deeplink="http://socail.com/ping/${item._id}"
                            sharePingDeepLink(context,deeplink)
                        }


                    }
                }
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp), thickness = 0.18.dp, color = Color.Gray.copy(alpha = 0.3f))
                Box(modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(shape = RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Gray.copy(alpha = 0.25f), Color.Gray.copy(alpha = 0.25f)
                            )
                        )
                    )){
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(4.dp), verticalArrangement = Arrangement.Center) {
                        Row(
                            modifier = Modifier //.padding(horizontal = 4.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Image(painter = painterResource(id = R.drawable.ping), contentDescription ="",modifier=Modifier.size(24.dp) )
                            Text(text = item.title.toString(), fontWeight = FontWeight.Normal, fontFamily = Constants.FONT_LIGHT,fontSize = 15.sp, color = Color(0xFFE9E9E9).copy(alpha = 01f))

                        }
                      ///  Text(text = item.location.capitalize(), fontWeight = FontWeight.Normal, fontSize = 11.sp, fontFamily = Constants.FONT_LIGHT, color = Color(0xFFC9D106))
                        item.description?.let {  Text(text = item.description.toString().capitalize(), fontWeight = FontWeight.Normal, fontSize = 11.sp, fontFamily = Constants.FONT_EXTRA_LIGHT, color = Color.LightGray.copy(alpha=0.9f), modifier = Modifier.padding(top=4.dp))}
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
        else -> Color(0xFF214601)///0xFF545707 0xFF888D0B 0xFF689F38
    }

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(40.dp),
        shape = RoundedCornerShape(4.dp),
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
fun ChatRoundUI(onChatClicked:()->Unit) {
    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//
        Image(painter = painterResource(id = R.drawable.chat_new), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
            Color.White.copy(alpha = 0.954f)) )
   Text("2.3k", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)
  }
}
@Composable
fun JoinRoundUI() {
    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//    Card(
//        onClick = {},
//        modifier=Modifier.size(40.dp),
//        shape = CircleShape, colors = CardDefaults.cardColors(containerColor =  Constants.HOME_TOP_BAR_COLOR)
//    ) {
//        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//
//            //Text(text = "5/100", fontFamily = Constants.FONT_LIGHT, fontSize = 8.sp, color = Color.LightGray, lineHeight =1.sp)
//        }
//
//    }
        Image(painter = painterResource(id = R.drawable.join_blue), contentDescription ="", modifier = Modifier.size(20.dp) )
    Text("122k", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)}
}
@Composable
fun CommentRoundUI() {//50,30 earlier
    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//    Card(
//        onClick = {},
//        modifier=Modifier.size(40.dp),
//        shape = CircleShape, colors = CardDefaults.cardColors(containerColor =  Constants.HOME_TOP_BAR_COLOR)
//    ) {
//        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//
//            //  Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
//        }
//
//    }
        Image(painter = painterResource(id = R.drawable.comment), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
            Color.White.copy(alpha = 0.954f)) )
        Text("56k", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)}
}
@Composable
fun ViewRoundUI() {
    Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//    Card(
//        onClick = {},
//        modifier=Modifier.size(40.dp),
//        shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Constants.HOME_TOP_BAR_COLOR)
//    ) {
//        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//
//            //  Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
//        }
//
//    }
        Image(painter = painterResource(id = R.drawable.view), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
            Color.White.copy(alpha = 0.954f)) )
    Text("24k", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)}
}