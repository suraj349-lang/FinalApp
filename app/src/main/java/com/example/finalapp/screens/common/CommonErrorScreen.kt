package com.example.finalapp.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants

@Composable
fun CommonErrorScreen(error:String,showButton:Boolean=false,onRetryClicked:()->Unit ={}) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.search_new_filled), contentDescription ="", modifier = Modifier.size(30.dp) )
            Text(error, fontSize = 20.sp, modifier = Modifier.padding(vertical = 20.dp), color= Color.DarkGray,fontFamily = Constants.FONT_MEDIUM)
            Button(onClick = { onRetryClicked()}, colors = ButtonDefaults.buttonColors(backgroundColor = Color(
                0xFF460561
            )
            )) {
                Text(text = "Retry", fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp, color = Color.White)
            }

        }

    }
}