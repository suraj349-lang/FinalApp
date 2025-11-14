package com.spint.app.screens.EventAndPingDesigns.events.templates.databased.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.spint.app.model.EventResponse
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.constants.Constants


@Composable
fun ChildMicroPostsDataBasedNew(modifier: Modifier, events: List<EventResponse>, onAddChildPostClicked: () -> Unit, onChildPostClicked:(String)->Unit) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
//        item {
//            Column(modifier = Modifier
//                .width(80.dp)
//                .fillMaxHeight(), verticalArrangement = Arrangement.Top) {
//                // Text(text = "Trails ->", fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp, color = Color.White, lineHeight = 8.sp)
//                Box(
//                    modifier = Modifier
//                        .clip(shape = RoundedCornerShape(12.dp))
//                        //.clip(RoundedCornerShape(8.dp))
//                        .background(color = Color.Black)
//                        //.fillMaxWidth()
//                        .fillMaxSize()
//
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .wrapContentSize()
//                            .align(Alignment.Center),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.add),
//                            contentDescription = "",
//                            contentScale = ContentScale.Crop,
//                            colorFilter = ColorFilter.tint(Color.White),
//                            modifier = Modifier
//                                .clickable { onAddChildPostClicked() }
//                                .size(40.dp)
//                        )
//                        Text(
//                            text = "Add a trail",
//                            fontFamily = Constants.FONT_LIGHT,
//                            fontSize = 12.sp, color = Color.White
//                        )
//                    }
//                }
//
//            }
//        }

        items(events) { event ->
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(140.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = imagePrefix +event.image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clickable { onChildPostClicked(event._id) }
                        .fillMaxSize(),
                    filterQuality = FilterQuality.High
                )
                Text(text = "@"+event.user.userName,fontFamily= Constants.USER_NAME_FONT, fontWeight = FontWeight.ExtraBold,fontSize=12.sp, modifier = Modifier
                    .padding(start = 4.dp)
                    .align(Alignment.BottomStart)
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color.Black,
                        ambientColor = Color.Black
                    ), color = Color.White)
//                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
//                        Text(text = event.title, color = Color.White, fontFamily = Constants.FONT_LIGHT, lineHeight = 18.sp, fontSize = 9.sp)
//                        Text(text = event.description ?: "", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, lineHeight = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 9.sp)
//                    }
            }
        }
    }

}

