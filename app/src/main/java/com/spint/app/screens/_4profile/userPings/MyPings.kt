package com.spint.app.screens._4profile.userPings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants

@Composable
fun MyPings(
    items: List<FlashPostResponse> = emptyList(),
    onFlashPostClicked: (String) -> Unit,
    onCreatePingClicked: () -> Unit = {}
) {
    val pingsList = remember { items }
    Column(modifier = Modifier.fillMaxSize()) {
        if (items.isEmpty()) {
            Card(
                modifier = Modifier
                    .clickable { onCreatePingClicked() }
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(180.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color(0xFFF3F0E9).copy(alpha = 1f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ping),
                            contentDescription = "",
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "✨ No Pings Yet!",
                                fontFamily = Constants.FONT_MEDIUM,
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            )
                            Text(
                                text = "Start your first Ping",
                                fontFamily = Constants.FONT_MEDIUM,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF121212)
                            )
                            Text(
                                text = "Share a quick offer or request that lives for just a few hours. Go public with your name, or stay private so no one knows it’s you.",
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                color = Color.DarkGray,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(50),
                        backgroundColor = floatingActionBtnColor
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "➕ Create Ping",
                                fontSize = 16.sp,
                                fontFamily = Constants.FONT_LIGHT,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                verticalItemSpacing = 2.dp,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(pingsList) { item ->
                    MyPingItem(item){
                        onFlashPostClicked(item._id)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPingItem(item: FlashPostResponse,onFlashPostClicked:()-> Unit) {
    Box(modifier = Modifier.clickable{onFlashPostClicked()}
        .aspectRatio(9f/13f) //120 earlier
        .clip(shape = RoundedCornerShape(6.dp))) {
        GlideImage(model=  imagePrefix +item.image/*R.drawable.profile_image_1*/ , contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop) //todo add imagePrefix when upload is happening
        if(item.pingCount !=0){
        Box(modifier= Modifier.padding(10.dp).background(color = Color.LightGray.copy(alpha = 0.2f)).align(Alignment.BottomStart).wrapContentSize()) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Image(painter = painterResource(R.drawable.ping), contentDescription = "",modifier=Modifier.size(18.dp))
                Text(item.pingCount.toString(), modifier = Modifier, color = Color.Black, fontSize = 14.sp, fontFamily = Constants.FONT_MEDIUM)
            }
        }
    }
    }
}

