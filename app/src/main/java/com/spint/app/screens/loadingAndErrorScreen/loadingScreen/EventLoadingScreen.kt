package com.spint.app.screens.loadingAndErrorScreen.loadingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.utils.constants.Constants




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