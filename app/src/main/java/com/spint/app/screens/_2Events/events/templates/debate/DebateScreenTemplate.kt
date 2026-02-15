package com.spint.app.screens._2Events.events.templates.debate


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.spint.app.R


@Composable
fun DebateDetailsScreen(modifier: Modifier = Modifier) {
        Box(
            modifier
                .fillMaxSize()
                .background(color = Color(0xFF121212))
        ) {

            Column(
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    AutoPlayVideo(
                        videoRes = R.raw.video1
                    )
                    Column(
                        modifier.align(Alignment.BottomEnd).padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.heart),
                                contentDescription = "",
                                modifier.size(20.dp)
                            )
                            Text("12.8 k", color = Color.White, fontSize = 10.sp)
                        }
                        Column(
                            modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.share),
                                contentDescription = "",
                                modifier.size(20.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Text("12.8 k", color = Color.White, fontSize = 10.sp)
                        }
                        Column(
                            modifier.wrapContentSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.privacy_new),
                                contentDescription = "",
                                modifier.size(20.dp)
                            )
                            Text("12.8 k", color = Color.White, fontSize = 10.sp)
                        }
                    }

                }
                Divider(modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color.Gray)
                Row(
                    modifier.zIndex(2f).padding(top = 10.dp, start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_colored),
                        contentDescription = "",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        "MaisieWilliams",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                }

                Text("NRC and CAA implementation in the bengal debate." ,color = Color.White, fontSize = 12.sp,modifier=Modifier.padding(10.dp))
              //  Text("Comparison chart", fontSize = 10.sp, color = Color.LightGray,modifier=Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                DashBoard()
                Trends()

            }
        }
    }

@Composable
fun Trends() {
    Box(modifier = Modifier.fillMaxWidth().height(50.dp).background(color = Color.DarkGray)) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Trends", color = Color.White)
            Text("Comments", color = Color.White)
            Text("Replies", color = Color.White)
            Text("Tags", color = Color.White)
        }
    }
}

@Composable
fun DashBoard(modifier: Modifier= Modifier) {
    Box(
        modifier.padding(horizontal = 10.dp).padding(bottom = 10.dp)
            .fillMaxWidth()
            .height(90.dp).clip(shape = RoundedCornerShape(2.dp)).border(width = 1.dp, color = Color.DarkGray,shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier.fillMaxWidth(0.5f).fillMaxHeight()) {
                Text(
                    "BJP",
                    modifier.padding(vertical = 3.dp).fillMaxWidth(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Divider(thickness = 0.25.dp, color = Color.DarkGray)
                Row(
                    modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.up_arrow),
                            contentDescription = "",
                            modifier.padding(horizontal = 8.dp, vertical = 2.dp).size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("2.5M", color = Color.White, fontSize = 14.sp)
                    }
                    Column(
                        modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.comment),
                            contentDescription = "",
                            modifier.padding(horizontal = 8.dp, vertical = 2.dp).size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("134.5k", color = Color.White, fontSize = 14.sp)
                    }
                    Column(
                        modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.trending),
                            contentDescription = "",
                            modifier.padding(horizontal = 8.dp, vertical = 2.dp).size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("12", color = Color.White, fontSize = 14.sp, lineHeight = 10.sp)
                    }

                }
            }
            Divider(
                thickness = 0.25.dp,color=Color.DarkGray,
                modifier = Modifier.fillMaxHeight().width(0.2.dp)
            )

            Column(modifier.fillMaxSize()) {
                Text(
                    "Congress",
                    modifier.padding(vertical = 3.dp).fillMaxWidth(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Divider(thickness = 0.25.dp,color=Color.DarkGray)
                Row(
                    modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.up_arrow),
                            contentDescription = "",
                            modifier.padding(horizontal = 8.dp, vertical = 2.dp).size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("2.5M", color = Color.White, fontSize = 14.sp)
                    }
                    Column(
                        modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.comment),
                            contentDescription = "",
                            modifier.padding(horizontal = 8.dp, vertical = 2.dp).size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("134.5k", color = Color.White, fontSize = 14.sp)
                    }
                    Column(
                        modifier.wrapContentWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.trending),
                            contentDescription = "",
                            modifier.padding(horizontal = 8.dp, vertical = 2.dp).size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("12", color = Color.White, fontSize = 14.sp, lineHeight = 10.sp)
                    }

                }
            }
        }


    }
}
@Preview(showBackground = true)
@Composable
fun DashBoard2(modifier: Modifier= Modifier) {
    Box(
        modifier.background(Color.Black)
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Column (
            modifier.padding(10.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(modifier.fillMaxWidth().fillMaxHeight(0.5f), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "BJP",
                    modifier.fillMaxWidth(0.3f),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Divider(modifier=Modifier.padding(bottom = 8.dp).width(1.dp).height(100.dp))
                Row(
                    modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier.fillMaxHeight().wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.up_arrow),
                            contentDescription = "",
                            modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("2.5M", color = Color.White)
                    }
                    Column(
                        modifier.fillMaxHeight().wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.comment),
                            contentDescription = "",
                            modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("134.5k", color = Color.White)
                    }
                    Column(
                        modifier.fillMaxHeight().wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.trending),
                            contentDescription = "",
                            modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("12", color = Color.White)
                    }

                }

            }
            Divider(thickness = 0.25.dp, modifier = Modifier.padding(bottom = 8.dp))
            Row(modifier.fillMaxWidth().fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Congress",
                    modifier.fillMaxWidth(0.3f),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start
                )
                Divider(modifier=Modifier.width(1.dp).height(100.dp))
                Row(
                    modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Column(
                        modifier.padding(top=4.dp).fillMaxHeight().wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.up_arrow),
                            contentDescription = "",
                            modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("2.5M", color = Color.White)
                    }
                    Column(
                        modifier.padding(top=4.dp).fillMaxHeight().wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.comment),
                            contentDescription = "",
                            modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("134.5k", color = Color.White)
                    }
                    Column(
                        modifier.padding(top=4.dp).fillMaxHeight().wrapContentWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.trending),
                            contentDescription = "",
                            modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text("12", color = Color.White)
                    }
                }
            }
        }
    }
}

