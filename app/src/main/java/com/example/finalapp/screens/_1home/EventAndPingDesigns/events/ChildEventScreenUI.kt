package com.example.finalapp.screens._1home.EventAndPingDesigns.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.model.EventResponse
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants

@Composable
fun ChildEventScreenUI(event:EventResponse) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier =Modifier.fillMaxWidth().height(500.dp).shadow(elevation = 20.dp, ambientColor = Color.White, spotColor = Color.White) ) {
                AsyncImage(
                    model = imagePrefix + event.image, contentDescription = "", modifier = Modifier.fillMaxSize()
                        , contentScale = ContentScale.Crop
                )
            }
            UserReactionUIChildPost(
                totalUpVotes = event.totalUpVotes,
                totalComments = event.totalComments,
                totalViews =event.totalViews ,
                onUpVotesClicked = { /*TODO*/ },
                onShareClicked = { /*TODO*/ }) {

            }

        }
    }
}


@Composable
fun UserReactionUIChildPost(totalUpVotes:Int,totalComments: Int, totalViews: Int,onUpVotesClicked:()->Unit,onShareClicked:()->Unit,onContributeClicked:()->Unit) {
//    Card(modifier = Modifier
//        .fillMaxWidth()
//        .height(40.dp)
//        .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
        Row(modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth().height(40.dp), horizontalArrangement = Arrangement.spacedBy(30.dp), verticalAlignment = Alignment.CenterVertically) {

            IconWithText(icon = R.drawable.upvote, text =totalUpVotes.toString() ){
                onUpVotesClicked()
            }
            IconWithText(icon = R.drawable.people, text = totalViews.toString())
            IconWithText(icon = R.drawable.comment_filled, text =totalComments.toString() )
            IconWithText(icon = R.drawable.share_event, text ="Share" , tint = Constants.HOME_TOP_BAR_TITLE_COLOR){
                onShareClicked()
            }
        }
  //  }
}