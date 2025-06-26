package com.example.finalapp.screens._1home.eventWarScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
val topColor= Color.DarkGray  //Color(0xFFF1AF0A)
@Composable
fun PublicEventDetailsTopBar(eventTitle:String,onBackClicked:()->Unit) {
    TopAppBar(
        modifier = Modifier.shadow(40.dp),
        backgroundColor =  topColor,
        title = { Text(text = eventTitle, fontFamily = Constants.FONT_MEDIUM, fontSize = 20.sp, color = Color.White)},
        navigationIcon = {
            Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription ="",
                modifier = Modifier.size(30.dp).clickable { onBackClicked() },
                colorFilter = ColorFilter.tint(Color.White)
            )},
        actions ={}
    )
}