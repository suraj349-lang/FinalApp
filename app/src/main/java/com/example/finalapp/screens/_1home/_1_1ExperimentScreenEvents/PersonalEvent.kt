package com.example.finalapp.screens._1home._1_1ExperimentScreenEvents

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.EventRequestDTO
import com.example.finalapp.utils.constants.Constants.FONT_EXTRA_LIGHT
import com.example.finalapp.utils.constants.Constants.FONT_LIGHT


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonalEvent(offer: EventRequestDTO) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(6.dp))
                .border(width = 1.dp, color = Color.LightGray)
                .fillMaxWidth()
                .height(600.dp)
                .padding(bottom = 20.dp, start = 2.dp, end = 2.dp)
        ) {
            // Background Image
            GlideImage(
                model = if(offer.image!=" ") offer.image else R.drawable.profile_image_3,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomCenter)
            ) {
                // profile image
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(CircleShape) // Makes the image circular
                        .border(1.dp, Color.LightGray, CircleShape) // Optional white border
                        .align(Alignment.Start) // Align it horizontally to the center

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_image_1), // Replace with your profile image
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(50.dp),
                    )

                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(width = 1.dp, color = Color.LightGray),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(0.1.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            shape = RoundedCornerShape(0f),
                            colors = CardDefaults.cardColors(
//                                containerColor = Color(0xFF0F3A4E).copy(alpha = 0.8f)
                                containerColor = Color.White.copy(alpha = 0.9f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize().padding(start=8.dp,end=16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Suraj kumar",
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    modifier = Modifier,
                                    fontFamily = FONT_LIGHT
                                )
//                                Image(
//                                    painter = painterResource(id = R.drawable.personal),
//                                    contentDescription = "",
//                                    modifier = Modifier.size(30.dp)
//                                )
                                Card(
                                    modifier = Modifier.wrapContentHeight(),
                                    colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Follow",
                                        modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                                        fontSize = 12.sp,
                                        color = Color.Black,
                                        fontFamily = FONT_LIGHT,fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                        Card(
                            modifier = Modifier.padding(0.dp)
                                .fillMaxWidth()
                                .height(40.dp),
                            shape= RoundedCornerShape(2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // 0xFFD81B60
                                Card(
                                    modifier = Modifier.wrapContentSize(),
                                    colors = CardDefaults.cardColors(
                                      //  containerColor = Color(0xFFD81B60).copy(alpha = 0.7f)
                                    containerColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text =  "Trekking ",
                                        color = Color(0xFF4B045E),
                                        fontFamily = FONT_EXTRA_LIGHT,
                                        fontSize = 14.sp, fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                    )
                                }

                                Card(
                                    modifier = Modifier.wrapContentSize(),
                                    colors = CardDefaults.cardColors(
                                       // containerColor = Color(0xFF043C6D).copy(alpha = 0.8f)
                                    containerColor = Color.LightGray
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text =  "Himachal Pradesh ,In",
                                        modifier = Modifier.padding(2.dp),
                                        fontFamily = FONT_EXTRA_LIGHT,fontWeight = FontWeight.Bold,
                                        color = Color(0xFF00080F),
                                        fontSize=14.sp
                                    )
                                }
                                Card(
                                    modifier = Modifier.wrapContentSize(),
                                    colors = CardDefaults.cardColors(
                                      //  containerColor = Color(0xFF3F7903).copy(alpha = 0.9f)
                                    containerColor = Color.LightGray
                                    ),
                                    shape = RoundedCornerShape(3.dp)
                                ) {
                                    Text(
                                        text = "12 people max",
                                        fontFamily = FONT_EXTRA_LIGHT,fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(2.dp),
                                        color = Color(0xFF060C00),
                                        fontSize = 12.sp
                                    )
                                }
                            }

                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = Color.White.copy(alpha = 0.9f))
                        ) {
                            Text(
                                text =  "Looking for someone to join me for trekking at HP, India. Nature,Adventure,FUN ,memories.",
                                fontFamily = FONT_EXTRA_LIGHT,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp, color = Color.Black,
                                modifier = Modifier
                                    .padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(0.dp))
                                .fillMaxSize()
                        ) {
                            Card(
                                modifier = Modifier.wrapContentSize(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF4F8F4)
                                ), shape = RoundedCornerShape(0.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 8.dp, end = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(0.5f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = "event active till :",
                                            fontFamily = FONT_EXTRA_LIGHT,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.DarkGray,

                                            )
                                        Text(
                                            text =  "12 hrs.",
                                            fontFamily = FONT_EXTRA_LIGHT,
                                            fontSize = 20.sp,
//                                        fontWeight = FontWeight.SemiBold,
                                            color = Color.Black,

                                            )
                                    }
                                    val context = LocalContext.current
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .fillMaxHeight(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Button(
                                            onClick = {
                                                Toast.makeText(
                                                    context,
                                                    "Joined the Event",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(text = "JOIN", fontFamily = FONT_LIGHT)

                                        }

                                    }

                                }

                            }

                        }
                    }
                }
            }
        }
    }

}