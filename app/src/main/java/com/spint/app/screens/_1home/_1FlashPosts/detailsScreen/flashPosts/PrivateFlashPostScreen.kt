package com.spint.app.screens._1home._1FlashPosts.detailsScreen.flashPosts

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.spint.app.R
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.screens._1home.commonUI.sharePingDeepLink
import com.spint.app.screens._4profile.privateUsername.dynamicText
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.formatDateTime
import com.spint.app.utils.getFormattedTimeAndFlag
import kotlinx.coroutines.delay



@Composable
fun PrivateFlashPostScreen(flashPostResponse: FlashPostResponse,onFlashPostClicked:()-> Unit) {
    val context=LocalContext.current
    Box(modifier = Modifier.clickable{onFlashPostClicked()}
        .padding(vertical = 4.dp, horizontal = 2.dp)
        .background(color = Color(0xFF190124))
        .fillMaxWidth()
        .wrapContentHeight()){
        Image(painter = painterResource(id = R.drawable.menu), contentDescription ="", modifier = Modifier
            .padding(10.dp)
            .align(Alignment.TopEnd)
            .rotate(90f)
            .size(15.dp), colorFilter = ColorFilter.tint(Color.White) )
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)) {
                Box(
                    Modifier
                        .size(70.dp)
                        .clip(shape = CircleShape)) {
                    AsyncImage(model = R.drawable.alien, contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
//                    Image(painter = painterResource(id = R.drawable.verified_new), contentDescription = "", modifier = Modifier
//                        .align(
//                            Alignment.BottomEnd
//                        ).padding(4.dp)
//                        .size(20.dp))
                }
                Column(
                    Modifier
                        .padding(start = 10.dp)
                        .fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement =Arrangement.spacedBy(5.dp)) {
                        flashPostResponse.user?.name?.let { Text(text = it, fontSize = 14.sp, fontFamily = Constants.USER_NAME_FONT, color = Color.LightGray, fontWeight = FontWeight.Bold) }

                        Image(painter = painterResource(id = R.drawable.verified_new_white), contentDescription = "", modifier = Modifier.size(16.dp))
                    }
                    TypeForPrivateFlashPost(flashPostResponse.category)
                    dynamicText(text = "Expiring at: ${formatDateTime(flashPostResponse.expirationTime)}", fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 10, color = Color.LightGray.copy(alpha = 0.8f))
                }


            }
            flashPostResponse.title?.let { dynamicText(text = it, fontSize = 18, fontFamily = Constants.FONT_MEDIUM) }
            Text(text = flashPostResponse.description ?: "", fontSize = 12.sp, fontFamily = Constants.FONT_LIGHT, lineHeight = 12.sp)
            Row(modifier = Modifier.padding(top=16.dp)
                .fillMaxWidth()
                .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.location_new), contentDescription = "", modifier = Modifier.size(12.dp))
                Text(text = flashPostResponse.location, maxLines = 1, fontFamily = Constants.FONT_LIGHT, fontSize = 10.sp, color = Color.LightGray, lineHeight = 12.sp)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment= Alignment.Bottom,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {

                ViewRoundUI(flashPostResponse.totalViews)
                CommentRoundUI(flashPostResponse.commentsCount)
                CountdownTimerForPrivate(flashPostResponse.expirationTime)
                AddPingOnFlashPost(flashPostResponse.peopleJoined,flashPostResponse.pingCount)
                ShareRoundUI(flashPostResponse.totalShared){
                    val deeplink="http://socail.com/ping/${flashPostResponse._id}"
                    sharePingDeepLink(context,deeplink)
                }


            }

        }

    }
}

@Composable
fun TypeForPrivateFlashPost(type:String) {
    Card(modifier = Modifier
        .wrapContentWidth()
        .height(28.dp)
        .padding(top = 4.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(
        0xFF4D0D68
    )  //0xFFC40C55
    )) {
        Text(text = ":: $type/private", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontFamily = Constants.FONT_MEDIUM, modifier = Modifier.padding(horizontal = 4.dp))
    }
}


@Composable
fun CountdownTimerForPrivate(expirationIso: String) {
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

    RoundUIForPrivate(time = timeLeft, isLessThanHour = isLessThanHour)
}


@Composable
fun RoundUIForPrivate(time: String, isLessThanHour: Boolean) {
    val containerColor = when {
        time == "Expired" -> Color.Gray
        isLessThanHour -> Color(0xFF940707)
        else -> Color(0xFF340647)// 0xFF520C70
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
            androidx.compose.material.Text(
                text = time,
                fontWeight = FontWeight.Bold,
                fontFamily = Constants.FONT_MEDIUM,
                fontSize = 13.sp,
                maxLines = 2, color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
