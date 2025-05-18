package com.example.finalapp.screens._3createEvent.publicEventDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun PublicEventDetailsScreenWrapper() {
    var warOrPosts by remember {
        mutableStateOf("war")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {PublicEventDetailsTopBar("Testing")},
        content = { it ->
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                PublicEventDetailsScreen(screen = warOrPosts, onScreenChange = {screen-> warOrPosts=screen})
            }
        }
    )
}


@Composable
fun PublicEventDetailsScreen(screen:String,onScreenChange:(String)->Unit) {
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        //background image
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            Image(painter = painterResource(id = R.drawable.back), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
        }

        // details
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)) {


        }
        // war / Posts
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp), verticalAlignment = Alignment.CenterVertically) {
          Card(
              Modifier
                  .fillMaxSize()
                  .padding(horizontal = 16.dp)) {
              Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                  Text(text = "War", fontFamily = Constants.FONT_MEDIUM, fontSize = 24.sp, color = Color.DarkGray, modifier = Modifier.clickable {
                      onScreenChange("war")
                  })
                  Divider(modifier = Modifier.fillMaxHeight(), thickness = 1.dp, color = Color.DarkGray)
                  Text(text = "Posts", fontFamily = Constants.FONT_MEDIUM, fontSize = 24.sp, color = Color.DarkGray, modifier = Modifier.clickable {
                      onScreenChange("posts")
                  })
              }

          }

        }
        if(screen =="war"){
            WarScreen()
        }else{
            PostsScreen()
        }
    }
}

