package com.example.finalapp.screens._2personalEvents

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.finalapp.utils.constants.Constants

@Composable
fun PersonalEventTopBar() {
    TopAppBar(
        title = { Text(text = "Events", fontFamily = Constants.FONT_MEDIUM, fontSize = 24.sp, color = Color.White ) },
        navigationIcon = {},
        actions = {},
        backgroundColor = Color(0xFF43075C)
    )

}
