package com.spint.app.screens._3createEventOrPing.createEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants

@Composable
fun AddCaptionCreateEvent(caption:String, onCaptionChange:(String)->Unit, onNextClicked:()->Unit){
    Column(modifier = Modifier
        .padding(top = 20.dp)
        .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Add Caption", fontFamily = Constants.FONT_MEDIUM, fontSize = 20.sp)
        TextField(
            value =caption ,
            onValueChange =onCaptionChange,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = { Text(text = "....")}
        )
        Button(onClick =  onNextClicked, enabled = caption.isNotEmpty(),colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor) ) {
            Text(text = "Next ->" , fontFamily = Constants.FONT_MEDIUM, color = if (caption.isNotEmpty()) Color.Black else Color.White)
        }
    }
}
