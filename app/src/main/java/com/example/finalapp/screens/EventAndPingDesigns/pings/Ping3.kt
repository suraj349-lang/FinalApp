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
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.finalapp.R
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants
import kotlin.random.Random

val VibrantBackgroundColors = listOf(
    Color(0xFFFF6F61), // Coral Red
    Color(0xFF00D26A), // Mint Green
    Color(0xFF3DDC84), // Android Green
    Color(0xFFFFC107), // Amber Yellow
    Color(0xFF6200EA), // Deep Purple
    Color(0xFF009688), // Teal
    Color(0xFF4CAF50), // Vibrant Green
    Color(0xFFFF4081), // Pink Accent
    Color(0xFF3F51B5), // Indigo
    Color(0xFF00BCD4), // Cyan
    Color(0xFFFF5722), // Deep Orange
    Color(0xFF8BC34A), // Light Green
    Color(0xFF2962FF), // Bright Blue
    Color(0xFFE91E63), // Magenta
    Color(0xFFFFEB3B), // Vibrant Yellow
    Color(0xFFAA00FF), // Electric Purple
)
val SoftVibrantColors = listOf(
    Color(0xFFFF8A80), // Soft Red
    Color(0xFF80CBC4), // Soft Teal
    Color(0xFFFFF176), // Soft Yellow
    Color(0xFF81D4FA), // Soft Blue
    Color(0xFFE1BEE7), // Lavender
    Color(0xFFFFCCBC), // Light Peach
    Color(0xFFCE93D8), // Light Purple
    Color(0xFFA5D6A7), // Light Green
    Color(0xFFB3E5FC), // Light Cyan
    Color(0xFFFFF59D), // Banana Yellow
    Color(0xFFFFAB91), // Coral
    Color(0xFFB39DDB), // Soft Indigo
    Color(0xFF90CAF9), // Sky Blue
    Color(0xFFFFE082), // Sun Glow
    Color(0xFFF48FB1), // Rose Pink
)

val DarkVibrantColors = listOf(
    Color(0xFF1E1E2F), // Charcoal Blue
    Color(0xFF2C2C54), // Midnight Indigo
    Color(0xFF3A3F5C), // Slate Navy
    Color(0xFF283149), // Twilight Blue
    Color(0xFF1F1B24), // True Black-Purple
    Color(0xFF1E272E), // Dark Steel
    Color(0xFF2E3A59), // Deep Sky Blue Navy
    Color(0xFF3E4C59), // Soft Iron Blue
    Color(0xFF1B1B2F), // Deep Black Navy
    Color(0xFF2D2A32), // Dim Purple Gray
    Color(0xFF252525), // Jet Gray
    Color(0xFF2F3E46), // Storm Grey Blue
    Color(0xFF373B44), // Smoky Steel
    Color(0xFF2C3E50), // Dark Ocean
    Color(0xFF3F3F74), // Muted Royal Indigo
)


@Composable
fun PingItem3(item: PingResponse) {
    val random = remember { Random.nextInt(DarkVibrantColors.size) }
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(
                shape = RoundedCornerShape(1.dp),
                color = DarkVibrantColors
                    .get(random)
                    .copy(alpha = 0.8f)
            ),
    ) {

        Column(modifier = Modifier.padding(1.dp)) {
            AsyncImage(
                model = imagePrefix+item.image,
                contentDescription = "",
                modifier = Modifier.wrapContentHeight(),
                contentScale = ContentScale.Crop
            )

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
                            item.user?.username?.let {
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
                            Button(
                                onClick = {},
                                colors=ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                //border = BorderStroke(2.dp, Color(0xFF00D26A)),
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
                                        color = Color.Black,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Button(
                                onClick = {},
                               // border = BorderStroke(2.dp, Color.White),
                                colors=ButtonDefaults.buttonColors(backgroundColor = Color.White),
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
                                            Color.Black
                                        )
                                    )
                                    Text(
                                        "Chat",
                                        color = Color.Black,
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