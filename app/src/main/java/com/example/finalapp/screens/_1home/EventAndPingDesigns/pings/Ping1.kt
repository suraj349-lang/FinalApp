package com.example.finalapp.screens._1home.EventAndPingDesigns.pings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants


@Composable
fun PingItem1(item: PingResponse) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black), // Dark background //0xFF1A1A1A
        elevation = CardDefaults.cardElevation(defaultElevation = 200.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="" ,modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop)
                Spacer(Modifier.width(8.dp))
                Column {

                        item.user?.let {
                            Text(
                                it.username,
                                fontWeight = FontWeight.Bold,
                                fontSize=14.sp,
                                fontFamily = Constants.USER_NAME_FONT,
                                color = Color.White
                            )
                        }


                    Text(
                        item.location,
                        fontSize = 10.sp,
                        color = Color(0xFF067DF1), //0xFF047CF3
                        fontWeight= FontWeight.Bold,
                        fontFamily = Constants.FONT_EXTRA_LIGHT
                    )
                }
            }
            Card(
                modifier = Modifier
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                shape = RoundedCornerShape(4.dp)
            ) {


                item.title?.let {
                    Text(
                        text = it,
                        maxLines = 3,
                        modifier= Modifier.padding(start = 4.dp),
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
            AsyncImage(
                model = imagePrefix + item.image,
                contentDescription = "Ping Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.Black)) {
                    Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {

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
                                    color = Color.LightGray
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
                                colorFilter = ColorFilter.tint(color = Color.LightGray)
                            )
                            Text(
                                "120",
                                color = Color.White,
                                fontFamily = Constants.FONT_LIGHT
                            )
                        }
                    }
                }

                Card(modifier = Modifier.wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                    border = BorderStroke(width = 1.dp, color = Color(0xFFF35220) )
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
                            item.expirationTime + " hrs left",
                            color = Color.White,
                            fontFamily = Constants.FONT_LIGHT
                        )
                    }
                }
            }

            // Response Buttons
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {},
                        border = BorderStroke(2.dp, Color(0xFF00D26A)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Image(painter = painterResource(id = R.drawable.join_blue), contentDescription ="", modifier = Modifier.size(18.dp) )
                            Text(
                                "Join",
                                color = Color.White,
                                fontFamily = Constants.FONT_MEDIUM,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {},
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Image(painter = painterResource(id = R.drawable.chat_new), contentDescription ="", modifier = Modifier.size(18.dp), colorFilter = ColorFilter.tint(
                                Color.White) )
                            Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
                        }

                    }
                }
            }
        }
    }
}
