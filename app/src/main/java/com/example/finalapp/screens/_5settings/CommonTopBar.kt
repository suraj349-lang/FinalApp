package com.example.finalapp.screens._5settings

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.finalapp.screens.common.BackImage
import com.example.finalapp.ui.theme.LIGHT_GREEN
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL

@Composable
fun CommonTopBar(title:String,onBackClicked:()->Unit) {
    TopAppBar(
        title={ Text(text = title.uppercase(), color = floatingActionBtnColor, fontFamily = Constants.FONT_MEDIUM, fontSize = 20.sp)},
        navigationIcon = {
            BackImage { onBackClicked() }
        },
        backgroundColor = Color.White,
        modifier = Modifier.statusBarsPadding()
    )
    
}