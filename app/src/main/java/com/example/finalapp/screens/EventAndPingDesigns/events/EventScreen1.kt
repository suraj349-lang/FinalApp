package com.example.finalapp.screens.EventAndPingDesigns.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.EventResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.testingDataAndScreen.imageUrls
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun EventScreenWrapper1(event: EventResponse, navController: NavHostController) {
    val systemUiController = rememberSystemUiController()
    val navBarColor = Color.DarkGray
    val backgroundColor= Color(0xFF121212)

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = navBarColor,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(
                color = navBarColor,
                darkIcons = true
            )
            systemUiController.setNavigationBarColor(
                color = navBarColor,
                darkIcons = true
            )
        }
    }
    Surface(modifier = Modifier
        .fillMaxSize(), color = Color.Black) {
        EventScreen1(event,onJoinEventClicked={ navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.route)})


    }





}

@Composable
fun EventScreen1(event: EventResponse,onJoinEventClicked:()->Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {

        AsyncImage(model = imagePrefix+event.image, contentDescription ="", modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f), filterQuality = FilterQuality.High
        )

        CreatorData(event.user.profileImage,event.user.username, event.title)
        EventDescription1(event.description)
        JoinEventButton(onJoinEventClicked)
        Stats(event.totalViews,event.totalChildPosts)
        UserReactionUI(event.totalComments,event.totalViews)
        MicroPosts(event.topPostsList ?: listOf())
    }
}

@Composable
fun CreatorData(userImage:String,username:String,eventTitle:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        AsyncImage(model = imagePrefix+userImage, contentDescription = "", modifier = Modifier
            .size(40.dp)
            .clip(shape = CircleShape), contentScale = ContentScale.Crop, filterQuality = FilterQuality.High)
        Column(modifier = Modifier.wrapContentSize()) {
            Text(text = username, fontFamily = Constants.USER_NAME_FONT, fontSize = 12.sp, color = Color.White)
            Text(text = eventTitle, fontFamily = Constants.FONT_MEDIUM, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
fun EventDescription1(description: String?) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = 8.dp)) {
        Text(text = description ?: " ", fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f), lineHeight = 16.sp)
    }
}

@Composable
fun JoinEventButton(onJoinEventClicked:()->Unit) {
    Button(onClick = { onJoinEventClicked()}, modifier = Modifier.padding(start = 8.dp,top=8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A0505))) {
        Text(text = "JOIN EVENT")
    }
}

@Composable
fun Stats(totalViews: Int, totalChildPosts: Int) {
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(80.dp), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 2.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(text = "Total views", color = Color.White, fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp)
                Text(text = totalViews.toString(),color = Color.White, fontFamily = Constants.USER_NAME_FONT, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Last 3 hours",color = Color.White, fontFamily = Constants.FONT_LIGHT)
                
            }

            
        }
        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .height(80.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 2.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(text = "Child Posts", color = Color.Black, fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp)
                Text(text = totalChildPosts.toString(),color = Color.Black, fontFamily = Constants.USER_NAME_FONT, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "10/26m", color = Color.Black, fontFamily = Constants.FONT_LIGHT)

            }


        }
    }
}

@Composable
fun UserReactionUI(totalComments: Int, totalViews: Int) {
    Card(modifier = Modifier
        .wrapContentWidth()
        .height(56.dp)
        .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f))) {
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
        Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.people), contentDescription = "", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
                Color.Black))
            Text(text = totalViews.toString(), fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)

        }
        Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription = "", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
                Color.Black))
            Text(text = totalComments.toString(), fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)

        }
        Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.share_event), contentDescription = "", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.Black))
           // Text(text = "456", fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)

        }
        Button(onClick = {  }, colors = ButtonDefaults.buttonColors(containerColor = Color(
            0xFF08087C
        )
        )) {
            Text(text = "+ Contribute", fontFamily =Constants.FONT_MEDIUM, color = Color.White)
            
        }
        }

    }
}

@Composable
fun MicroPosts(topPostsList: List<String>) {
    UserReactions1(imageUrls = topPostsList, onImageClicked = {})
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserReactions1(imageUrls:List<String>,onImageClicked:(String)->Unit) {
    LazyHorizontalGrid(
        modifier=Modifier.padding(start=6.dp),
        rows = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (imageUrls.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = Color.Gray)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            colorFilter=ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .clickable { }
                                .size(40.dp)
                        )
                        Text(text ="Add child post", fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp )
                    }
                }

            }
        } else {
            items(imageUrls) { imageUrl ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    GlideImage(
                        model = imageUrl,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable { onImageClicked(imageUrl) }
                            .fillMaxSize(), alpha = 0.8f
                    )
                }
            }
        }
    }
}

