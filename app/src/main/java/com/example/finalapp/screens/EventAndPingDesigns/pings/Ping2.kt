package com.example.finalapp.screens.EventAndPingDesigns.pings


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants


@Composable
fun PingItem2(item: PingResponse) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth().clip(shape = RoundedCornerShape(16.dp)),
    ) {
        AsyncImage(
            model = imagePrefix + item.image,
            contentDescription = "",
            modifier = Modifier.heightIn(min = 300.dp, max = 600.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Slightly taller for smoother blend
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent, Color.DarkGray, Color.Black
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .zIndex(6f)
                    .shadow(elevation = 40.dp)
            ) {
                AsyncImage(
                    model = imagePrefix + item.user?.profileImage,
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(8.dp))
                Column {

                    item.user?.username?.let {
                        Text(
                            text = it,
                            modifier = Modifier.zIndex(4f).shadow(
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
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
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

                Card(
                    modifier = Modifier.wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
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
                            text = item.expirationTime + " hrs left",
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
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)//0xFF1E1E1E
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
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.join_blue),
                                contentDescription = "",
                                modifier = Modifier.size(18.dp)
                            )
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
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.chat_new),
                                contentDescription = "",
                                modifier = Modifier.size(18.dp),
                                colorFilter = ColorFilter.tint(
                                    Color.White
                                )
                            )
                            Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
                        }

                    }
                }
            }
        }
    }}
