package com.spint.app.screens._1home._1_1ExperimentScreenEvents


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spint.app.R
import com.spint.app.model.User

data class EventDroppedProfileModel(
    val date:String="No date",
    val repost:Long=1234L,
    val type:EventsDroppedProfileEnum=EventsDroppedProfileEnum.COMEDY,
    val eventName:String="India's got Talent",
    val user: User= User(),
)

enum class EventsDroppedProfileEnum {
    COMEDY,
    CONCERT,
    GENERIC
}

@Preview(showBackground = true)
@Composable
fun EventDroppedProfile(eventDroppedProfileModel: EventDroppedProfileModel= EventDroppedProfileModel()) {
    Box(modifier = Modifier
        .background(color = Color.Black)
        .wrapContentSize()
        .padding(16.dp)){
        Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(2.dp)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "24 May", color = Color.White )
                    Text(text = eventDroppedProfileModel.eventName,color = Color.White)

                    Text(text ="34k",color = Color.White)
                }

            }
            Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="", modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),contentScale = ContentScale.Fit )


        }

    }

}
