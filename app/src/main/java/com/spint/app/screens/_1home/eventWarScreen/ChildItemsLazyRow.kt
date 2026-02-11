package com.spint.app.screens._1home.eventWarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.spint.app.R
import com.spint.app.model.DropProfileResponse
import com.spint.app.model.EventResponse
import com.spint.app.model.User
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.constants.Constants


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChildMicroPostsInDetailsScreen(events: List<EventResponse>, onAddChildPostClicked: () -> Unit, onChildPostClicked:(String)->Unit) {
    LazyRow(
        modifier= Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(start = 4.dp, bottom = 8.dp, top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (events.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = Color.Gray)
                        //.fillMaxWidth()
                       // .aspectRatio(9f / 16f)

                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.Center), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            colorFilter= ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clickable { onAddChildPostClicked() }
                                .size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text ="Add child post", fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp )
                    }
                }

            }
        } else {
            items(events) { event ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = imagePrefix +event.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable { onChildPostClicked(event._id) }
                            .aspectRatio(1f),
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
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChildMicroPingsInDetailsScreen(events: List<FlashPostResponse>, onAddChildPostClicked: () -> Unit, onChildPostClicked:(String)->Unit) {
    LazyRow(
        modifier= Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(start = 4.dp, bottom = 8.dp, top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (events.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        //.clip(RoundedCornerShape(8.dp))
                        .background(color = Color.Gray)
                        //.fillMaxWidth()
                        .aspectRatio(1f)

                ) {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            colorFilter= ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .clickable { onAddChildPostClicked() }
                                .size(40.dp)
                        )
                        Text(text ="Create ping", fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp )
                    }
                }

            }
        } else {
            items(events) { event ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = imagePrefix +event.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable { onChildPostClicked(event._id) }
                            .aspectRatio(1f),
                        filterQuality = FilterQuality.High
                    )
                    Text(text = "@"+event.user?.userName,fontFamily= Constants.USER_NAME_FONT, fontWeight = FontWeight.ExtraBold,fontSize=12.sp, modifier = Modifier
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
}


@Composable
fun ChildMicroNearByUsersInDetailsScreen(
    users: List<User>,
    onAddChildPostClicked: () -> Unit,
    onProfileClicked: (String) -> Unit
) {
    val users= listOf(R.drawable.profile_image_1,R.drawable.profile_image_3,R.drawable.profile_image_2,R.drawable.girl)
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(start = 4.dp, bottom = 8.dp, top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(users) { user ->
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    shape = CircleShape
                ) {
                    AsyncImage(
                        model = user,//imagePrefix + user.profileImage,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(60.dp)//.clickable { onProfileClicked(user._id) }
                        ,
                        filterQuality = FilterQuality.High
                    )
                }
                    Text(
                            text = "@suraj" ,//+ user.userName,
                            fontFamily = Constants.USER_NAME_FONT,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            modifier = Modifier,
                            color = Color.Black
                        )
//                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
//                        Text(text = event.title, color = Color.White, fontFamily = Constants.FONT_LIGHT, lineHeight = 18.sp, fontSize = 9.sp)
//                        Text(text = event.description ?: "", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, lineHeight = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 9.sp)
//                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .height(40.dp)
                .clip(shape = RoundedCornerShape(40))
                //.clip(RoundedCornerShape(8.dp))
                .background(color = Color(0xFFBE044D))
            //.fillMaxWidth()

        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxHeight()
                    .wrapContentWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "3400", lineHeight = 16.sp, fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp, color = Color.White)
                    Text(text = ("joined"), lineHeight = 16.sp, fontFamily = Constants.FONT_MEDIUM, fontSize = 8.sp, color = Color.White)
                    
                }
                Divider(modifier = Modifier.width(1.dp), color = Color.Black, thickness = 60.dp)
                Image(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .clickable { onAddChildPostClicked() }
                        .size(24.dp)
                )
                Text(
                    text = "Participate in direct chat",
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun ChildMicroDropProfilesInDetailsScreen(dropProfiles: List<DropProfileResponse>, onAddChildPostClicked: () -> Unit, onChildPostClicked:(String)->Unit) {
    val users= listOf(R.drawable.profile_image_1,R.drawable.profile_image_3,R.drawable.profile_image_2,R.drawable.girl)
    LazyRow(
        modifier= Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(start = 4.dp, bottom = 8.dp, top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (users.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        //.clip(RoundedCornerShape(8.dp))
                        .background(color = Color.Gray)
                        //.fillMaxWidth()
                        .aspectRatio(9f / 16f)

                ) {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            colorFilter= ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .clickable { onAddChildPostClicked() }
                                .size(40.dp)
                        )
                        Text(text ="Drop your profile", fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp )
                    }
                }

            }
        } else {
            items(users) { user ->
                Box(
                    modifier = Modifier
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = user,//imagePrefix +profile.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            /// .clickable { onChildPostClicked(user) }
                            .aspectRatio(9f / 16f),
                        filterQuality = FilterQuality.High
                    )
                    Text(text = "@suraj" /*"@"+profile.createdBy,fontFamily= Constants.USER_NAME_FONT*/, fontWeight = FontWeight.ExtraBold,fontSize=12.sp, modifier = Modifier
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
}
