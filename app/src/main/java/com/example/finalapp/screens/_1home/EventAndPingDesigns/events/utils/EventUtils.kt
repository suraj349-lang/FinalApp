package com.example.finalapp.screens._1home.EventAndPingDesigns.events.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants

@Composable
fun EventDescriptionVertical(
    text: String="",
    modifier: Modifier,
) {
    if(text.isNotEmpty()) {

        Column(modifier = modifier) {
            Text(
                text = text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontFamily = Constants.FONT_EXTRA_LIGHT,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp,
            )
        }
    }
}
@Composable
fun FollowButtonVertical(modifier: Modifier, onJoinEventClicked:()->Unit) {
      // 0xFF7A0505
        Card(modifier = Modifier
            .clickable { onJoinEventClicked() }
            .height(24.dp)
            .wrapContentWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(width = 1.dp, color = Color.White), shape = RoundedCornerShape(50)
        ) {
            Row(modifier = Modifier.fillMaxHeight().wrapContentWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Follow+", modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .padding(horizontal = 6.dp, vertical = 2.dp), textAlign = TextAlign.Center, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, fontFamily = Constants.FONT_LIGHT, lineHeight = 10.sp, color = Color.White)
        }
    }

}


@Composable
fun FunctionsAndStatsVertical(modifier: Modifier,totalUpVotes:Int,totalComments: Int, totalViews: Int,onUpVotesClicked:()->Unit,onShareClicked:()->Unit,onContributeClicked:()->Unit) {
    Column(modifier = modifier,verticalArrangement = Arrangement.spacedBy(16.dp)) {
        IconWithTextFunctionVertical(icon = R.drawable.create_event, text =totalUpVotes.toString() ){ onUpVotesClicked() }
        IconWithTextFunctionVertical(icon = R.drawable.ping, text = totalViews.toString())
        IconWithTextFunctionVertical(icon = R.drawable.drop_profile_filled_2, text =totalComments.toString() )
        IconWithTextFunctionVertical(icon = R.drawable.direct_chat, text ="34" , tint = Constants.HOME_TOP_BAR_TITLE_COLOR){ onShareClicked() }
    }

}


@Composable
fun IconWithTextFunctionVertical(icon: Int, text: String, tint: Color = Color.White,textColor:Color=Color.White,onClick:()->Unit={}) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .wrapContentHeight()
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
         //   colorFilter = ColorFilter.tint(tint)
        )
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Constants.FONT_LIGHT,
            fontSize = 12.sp,
            color=textColor, lineHeight = 12.sp
        )
    }
}
