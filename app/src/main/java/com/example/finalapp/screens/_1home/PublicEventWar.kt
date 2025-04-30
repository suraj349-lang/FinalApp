package com.example.finalapp.screens._1home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD

@Composable
fun PublicEventDetailsAndWarUI() {
    Scaffold(topBar = {}, content = {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(modifier =Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BackGroundImage()
                EventTitle("Farmers protest")

            }

        }
    })

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BackGroundImage() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.4f)) {
        GlideImage(model = imagePrefix+"", contentDescription ="", contentScale = ContentScale.Crop)
    }
}

@Composable
fun EventTitle(title:String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()){
        Text(text = title.uppercase(), fontFamily = DONGLE_BOLD, fontSize = 30.sp)
    }
}

@Composable
fun EventDescription() {
    Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()){

    }

}