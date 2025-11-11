package com.spint.app.screens._1home.EventAndPingDesigns.events.templates.databased.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R
import com.spint.app.model.EventResponse
import com.spint.app.utils.constants.Constants

@Composable
fun FunctionsAndStatsDataBased(modifier: Modifier, totalUpVotes:Int, totalComments: Int, totalViews: Int, onUpVotesClicked:()->Unit, onShareClicked:()->Unit, onContributeClicked:()->Unit) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        IconWithTextFunctionDataBased(icon = R.drawable.heart, text =totalUpVotes.toString() ){ onUpVotesClicked() }
        IconWithTextFunctionDataBased(icon = R.drawable.ping, text = totalViews.toString())
        IconWithTextFunctionDataBased(icon = R.drawable.drop_profile_filled_2, text =totalComments.toString() )
        IconWithTextFunctionDataBased(icon = R.drawable.direct_chat, text ="34" , tint = Constants.HOME_TOP_BAR_TITLE_COLOR){ onShareClicked() }
    }

}


@Composable
fun IconWithTextFunctionDataBased(icon: Int, text: String, tint: Color = Color.White, textColor: Color = Color.White, onClick:()->Unit={}) {
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
            colorFilter = ColorFilter.tint(tint)
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

@Composable
fun DataBasedTrails(childPosts: List<EventResponse>) {

}
