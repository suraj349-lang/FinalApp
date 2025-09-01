package com.example.finalapp.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.finalapp.R
import com.example.finalapp.ui.theme.floatingActionBtnColor

@Composable
fun BackImage(onBackClicked: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.baseline_arrow_back_24) ,
        contentDescription ="back button", modifier = Modifier.size(24.dp).clickable { onBackClicked() },
        colorFilter = ColorFilter.tint(Color.Black)
    )
}