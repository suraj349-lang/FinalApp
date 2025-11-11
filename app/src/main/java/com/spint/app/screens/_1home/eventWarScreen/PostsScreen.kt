package com.spint.app.screens._1home.eventWarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.spint.app.R

@Composable
fun PostsScreen() {
    LazyColumn{
        items(12){
            PostsScreenItem()
        }
    }
}

@Composable
fun PostsScreenItem() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp)) {
        Image(
            painter = painterResource(id = R.drawable.profile_image_1),
            contentDescription ="", modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

    }
}