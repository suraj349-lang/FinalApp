package com.example.finalapp.screens._1home.EventAndPingDesigns.pings


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants
import kotlin.random.Random


@Composable
fun PingItem3(item: PingResponse) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .border(
                width = 0.25.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = Brush.verticalGradient(colors = listOf(Color(0xFF142902), Color(0xFF050303)))
            )
//            .background(
//                shape = RoundedCornerShape(16.dp),
//                color = Color(0xFFAFB42B) //0xFFAFB42B
//            ),
    ) {

        Column(modifier = Modifier.padding(1.dp)) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
               // .shadow(elevation = 10.dp, spotColor = Color.White, ambientColor = Color.White)
                ,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) {
                AsyncImage(
                    model = imagePrefix+item.image,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }


            Box {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .zIndex(6f)
                            .shadow(elevation = 40.dp)
                    ) {
                        AsyncImage(
                            model = imagePrefix+item.user?.profileImage,
                            contentDescription = "",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(shape = CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            item.user?.userName?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier
                                        .zIndex(4f)
                                        .shadow(
                                            elevation = 60.dp,
                                            ambientColor = Color.White,
                                            spotColor = Color.White
                                        ),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    fontFamily = Constants.USER_NAME_FONT,
                                    color = Color.White
                                )
                            }


                            Text(
                                item.location,
                                fontSize = 10.sp,
                                color = Color(0xFF047CF3),//0xFFD69D0E //0xFF047CF3 //0xFF067DF1
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = Constants.FONT_MEDIUM
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .wrapContentSize()
                            .zIndex(6f)
                            .shadow(elevation = 60.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        item.title?.let {
                            Text(
                                text = it,
                                maxLines = 3,
                                modifier = Modifier.padding(start = 4.dp),
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = Constants.USER_NAME_FONT,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .zIndex(6f)
                            .shadow(elevation = 60.dp)
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                            .wrapContentHeight()
                    ) {

                        item.description?.let {
                            Text(
                                text = it,
                                fontFamily = Constants.FONT_LIGHT,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }

                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Card(
                            modifier = Modifier.wrapContentSize(),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Row(
                                modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.people),
                                        contentDescription = "",
                                        modifier = Modifier.size(16.dp),
                                        colorFilter = ColorFilter.tint(
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        "100",
                                        color = Color.White,
                                        fontFamily = Constants.FONT_LIGHT
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.comment),
                                        contentDescription = "",
                                        modifier = Modifier.size(16.dp),
                                        colorFilter = ColorFilter.tint(color = Color.White)
                                    )
                                    Text(
                                        "120",
                                        color = Color.White,
                                        fontFamily = Constants.FONT_LIGHT
                                    )
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.wrapContentSize(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF35220)),
                            border = BorderStroke(width = 1.dp, color = Color(0xFFF35220))
                        )
                        {
                            Row(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.clock_outlined),
                                    contentDescription = "",
                                    modifier = Modifier.size(16.dp),
                                    colorFilter = ColorFilter.tint(
                                        color = Color(0xFFFFFFFF)
                                    )
                                )
                                Text(
                                    text =item.expirationTime + " hrs left",
                                    color = Color.White,
                                    fontFamily = Constants.FONT_LIGHT
                                )
                            }
                        }
                    }

                    // Response Buttons
                    Box(
                        modifier = Modifier
                            .fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(brush = Brush.verticalGradient(colors = listOf(Color(
                                0xFF7B1FA2
                            ),
                                Color(0xFF1976D2)
                            ))),

                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {},
                                colors=ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                elevation=ButtonDefaults.elevation(0.dp),
                                //border = BorderStroke(2.dp, Color(0xFF00D26A)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.5f),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.join_blue),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        "Join",
                                        color = Color.White,
                                        fontSize=16.sp,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Button(
                                onClick = {},
                               // border = BorderStroke(2.dp, Color.White),
                                colors=ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                elevation=ButtonDefaults.elevation(0.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.chat_new),
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp),
                                        colorFilter = ColorFilter.tint(
                                            Color.White
                                        )
                                    )
                                    Text(
                                        "Chat",
                                        color = Color.White,
                                        fontSize=16.sp,
                                        fontFamily = Constants.FONT_MEDIUM
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}