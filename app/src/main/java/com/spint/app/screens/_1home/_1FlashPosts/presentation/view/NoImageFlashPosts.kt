package com.spint.app.screens._1home._1FlashPosts.presentation.view

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.formatDateTime



@Composable
fun NoImageFlashPosts(flashPostResponse: FlashPostResponse,onFlashPostClicked:()-> Unit) {
    val context= LocalContext.current
    Box(modifier = Modifier.clickable{onFlashPostClicked()}
        .padding(vertical = 4.dp, horizontal = 2.dp)
        .background(color = Color.Black)
        .fillMaxWidth()
        .wrapContentHeight()){
        Image(painter = painterResource(id = R.drawable.menu), contentDescription ="", modifier = Modifier
            .padding(10.dp)
            .align(
                Alignment.TopEnd
            )
            .rotate(90f)
            .size(15.dp), colorFilter = ColorFilter.tint(
            Color.White) )
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()) {
         Row(
             Modifier
                 .fillMaxWidth()
                 .height(100.dp)) {
             Card(Modifier.size(100.dp), shape = CircleShape) {
                 AsyncImage(model = imagePrefix+flashPostResponse.user?.profileImage, contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
             }
             Column(
                 Modifier
                     .padding(start = 10.dp)
                     .fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                 Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement =Arrangement.spacedBy(5.dp)) {
                     flashPostResponse.user?.name?.let { Text(text = it, fontSize = 14.sp, fontFamily = Constants.USER_NAME_FONT, color = Color.LightGray, fontWeight = FontWeight.Bold) }
                     Image(painter = painterResource(id = R.drawable.verified_new_white), contentDescription = "", modifier = Modifier.size(16.dp))
                 }
                 Type(flashPostResponse.category)
                 dynamicText(text = "Expiring at: ${formatDateTime(flashPostResponse.expirationTime)}", fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 10, color = Color.LightGray.copy(alpha = 0.8f))
             }


         }
            flashPostResponse.title?.let { dynamicText(text = it, fontSize = 18, fontFamily = Constants.FONT_MEDIUM) }
            Text(text = flashPostResponse.description ?: "", fontSize = 12.sp, fontFamily = Constants.FONT_LIGHT, lineHeight = 12.sp)
            Row(modifier = Modifier.padding(top=16.dp).fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.location_new), contentDescription = "", modifier = Modifier.size(16.dp))
                Text(text = flashPostResponse.location, maxLines = 1, fontFamily = Constants.FONT_LIGHT, fontSize = 10.sp, color = Color.LightGray, lineHeight = 12.sp)
            }
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                verticalAlignment=Alignment.Bottom,
//                modifier = Modifier.padding(vertical = 12.dp)
//                    .fillMaxWidth()
//                    .background(color = Color.Transparent)
//            ) {
//
//                ViewRoundUI(flashPostResponse.viewsCount)
//                CommentRoundUI(flashPostResponse.commentsCount)
//                CountdownTimer(flashPostResponse.expirationTime)
//                AddPingOnFlashPost(flashPostResponse.peopleJoined, flashPostResponse.pingCount)
//                ShareRoundUI(flashPostResponse.totalShared) {
////                    val deeplink="http://socail.com/ping/${item._id}"
////                    sharePingDeepLink(context,deeplink)
//                }
//
//
//            }
            HorizontalDivider(modifier= Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 10.dp)
                .fillMaxWidth(), thickness = 0.5.dp,color=Color.DarkGray)
            BottomUserActionsUI(
                onRespondClicked = {},
                onCommentButtonClicked = {},
                onShareClicked = {
                    val deeplink = "http://${Constants.APP_NAME}.com/flashPost/${flashPostResponse._id}"
                    sharePingDeepLink(context, deeplink)
                }
            )

        }

    }
}

@Composable
fun Type(type:String) {
    Card(modifier = Modifier
        .wrapContentWidth().height(28.dp)
        .padding(top = 4.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color(
        0xFF1B3603
    )
    )) {
        Text(text = ":: $type/public", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontFamily = Constants.FONT_MEDIUM, modifier = Modifier.padding(horizontal = 4.dp))
    }
}