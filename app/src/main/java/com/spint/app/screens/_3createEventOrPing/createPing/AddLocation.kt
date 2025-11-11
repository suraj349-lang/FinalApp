package com.spint.app.screens._3createEventOrPing.createPing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants

@Composable
fun AddLocationCreatePing(city:String,country:String,onNextClicked: (String) -> Unit) {
    val cityCountry= "$city, $country"
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Your Location", fontFamily = Constants.FONT_MEDIUM, fontSize = 24.sp)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(painterResource(id = R.drawable.location_new),contentDescription = null, modifier = Modifier.size(30.dp))
            Text(text = cityCountry, modifier = Modifier , fontFamily = Constants.FONT_MEDIUM, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = { onNextClicked(cityCountry)} ,
            colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor),
            shape = RoundedCornerShape(6.dp)
            ) {
            Text(text = "Next", fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp, color = Color.White)
        }
    }
}
