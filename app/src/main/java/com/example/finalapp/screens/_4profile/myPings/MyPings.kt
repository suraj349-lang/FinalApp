package com.example.finalapp.screens._4profile.myPings

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.EventResponse
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants


@Composable
fun MyPings(items: List<PingResponse>, onCreatePingClicked:()->Unit) {
    val pingsList= remember{ items}
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "My Pings", color = Color.White,fontWeight = FontWeight.Bold,
                fontSize=18.sp,
                fontFamily = Constants.FONT_MEDIUM)
        }
        LazyRow{
            items(pingsList){ item->
                MyPingItem(item)
            }
        }
        Card(modifier = Modifier
            .clickable { onCreatePingClicked() }
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(40.dp),
            backgroundColor = Color(0xFF121212),
            border = BorderStroke(width = 1.dp, brush = Brush.linearGradient(colors = listOf(Color(0xFFF7B206), Color(0xFF540575)))))
        {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painterResource(id = R.drawable.add),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color(0xFF033666)),
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = "Create new Ping",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.White
                )

            }

        }
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPingItem(item: PingResponse) {
    Log.i("CALLAPI", "LiveEventItem:$item ")
    Box(modifier = Modifier
        .size(180.dp) //120 earlier
        .padding(end = 8.dp, top = 8.dp)
        .clip(shape = RoundedCornerShape(6.dp))) {
        GlideImage(model=  imagePrefix +item.image/*R.drawable.profile_image_1*/ , contentDescription = "", contentScale = ContentScale.Crop) //todo add imagePrefix when upload is happening
//        Row(modifier = Modifier
//            .align(Alignment.BottomStart)
//            .padding(4.dp)
//            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            item.title?.let { Text(text = it, color = Color.White, fontWeight = FontWeight.SemiBold) }
//            Card(shape = CircleShape,backgroundColor = Color.Black.copy(alpha = 0.4f)) {
//                Text(text=item.expirationTime, color = Color.White, modifier = Modifier.padding(2.dp))
//            }
//
//
//        }



    }
}

