package com.spint.app.screens._5settings

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.spint.app.screens.common.BackImage
import com.spint.app.utils.constants.Constants

@Composable
fun CommonTopBar(title:String,backGroundColor:Color= Color.White,textColor:Color =Color(0xFF121212),onBackClicked:()->Unit) {
    TopAppBar(
        title={ Text(text = title.capitalize(), color = textColor, fontFamily = Constants.FONT_MEDIUM, fontSize = 20.sp)},
        navigationIcon = {
            BackImage { onBackClicked() }
        },
        backgroundColor = backGroundColor,
        modifier = Modifier.statusBarsPadding()
    )
    
}