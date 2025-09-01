package com.example.finalapp.screens.loadingAndErrorScreen.loadingScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.finalapp.model.EventResponse
import com.example.finalapp.screens.VideoPlayerComposable
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.EventDescription
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.EventTags
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.JoinEventButton
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.MicroPosts
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.Stats
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.UserReactionVerticalUI
import com.example.finalapp.screens._1home.commonUI.shareDeepLink
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants




@Composable
fun EventLoadingScreen(onBackClicked:()->Unit) {
    Box(modifier = Modifier
        .wrapContentSize()
        .background(color = Color.White)) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(horizontal = 2.dp)
                .padding(bottom = 6.dp, top = 2.dp)
                .border(width = 0.5.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(start = 8.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {


                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Text(
                            text = "------",
                            fontFamily = Constants.USER_NAME_FONT,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = "-----",
                            fontFamily = Constants.FONT_LIGHT,
                            fontSize = 9.sp,
                            color = Color.Black,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            // EventTags()
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
                    .background(color = Color(0xFF00020C))
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

            }


            Text(
                text = "----",
                modifier= Modifier.padding(start = 4.dp),
                fontFamily = Constants.FONT_MEDIUM,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                fontSize = 16.sp
            )

        }
    }
}