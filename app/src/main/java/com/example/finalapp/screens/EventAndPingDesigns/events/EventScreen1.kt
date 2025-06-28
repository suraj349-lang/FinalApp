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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.finalapp.R
import com.example.finalapp.screens._1home.publicEvent.UserReactions
import com.example.finalapp.testingDataAndScreen.imageUrls
import com.example.finalapp.utils.constants.Constants
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.common.math.Stats

@Preview(showBackground = true)
@Composable
fun EventScreenWrapper1() {
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
    Scaffold(
        topBar = {},
        content = {
            Surface(modifier = Modifier
                .padding(it)
                .fillMaxSize(), color = Color.Black) {
                EventScreen1()


            }

        }
    )
}

@Composable
fun EventScreen1() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="", modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f) )

        CreatorData()
        EventDescription1()
        JoinEventButton()
        Stats()
        UserReactionUI()
        MicroPosts()
    }
}

@Composable
fun CreatorData() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription = "", modifier = Modifier
            .size(40.dp)
            .clip(shape = CircleShape), contentScale = ContentScale.Crop)
        Column(modifier = Modifier.wrapContentSize()) {
            Text(text = "suraj_3494", fontFamily = Constants.USER_NAME_FONT, fontSize = 12.sp, color = Color.White)
            Text(text = "Breaking conflict, IRAN vs ISREAL", fontFamily = Constants.FONT_MEDIUM, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}

@Composable
fun EventDescription1() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = 8.dp)) {
        Text(text = "Event odds pal, the mad thinking of the conflict has arosen and leading to the trouble in the area", fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f), lineHeight = 16.sp)
    }
}

@Composable
fun JoinEventButton() {
    Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(start = 8.dp,top=8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A0505))) {
        Text(text = "JOIN EVENT")
    }
}

@Composable
fun Stats() {
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(80.dp), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp,top=2.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(text = "User reactions", color = Color.White, fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp)
                Text(text = "179 %",color = Color.White, fontFamily = Constants.USER_NAME_FONT, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "10/26m",color = Color.White, fontFamily = Constants.FONT_LIGHT)
                
            }

            
        }
        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .height(80.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 2.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(text = "User reactions", color = Color.Black, fontFamily = Constants.FONT_LIGHT)
                Text(text = "179 %",color = Color.Black, fontFamily = Constants.USER_NAME_FONT, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "10/26m", color = Color.Black, fontFamily = Constants.FONT_LIGHT)

            }


        }
    }
}

@Composable
fun UserReactionUI() {
    Card(modifier = Modifier
        .wrapContentWidth()
        .height(56.dp)
        .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f))) {
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
        Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top) {
            Image(painter = painterResource(id = R.drawable.people), contentDescription = "", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
                Color.Black))
            Text(text = "100", fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)

        }
        Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top) {
            Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription = "", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
                Color.Black))
            Text(text = "145", fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)

        }
        Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top) {
            Image(painter = painterResource(id = R.drawable.share), contentDescription = "", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.Black))
            Text(text = "456", fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)

        }
        Button(onClick = {  }) {
            Text(text = "+ Contribute")
            
        }
        }

    }
}

@Composable
fun MicroPosts() {
    UserReactions1(imageUrls = imageUrls, onImageClicked = {})
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
        items(imageUrls) {imageUrl->
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

