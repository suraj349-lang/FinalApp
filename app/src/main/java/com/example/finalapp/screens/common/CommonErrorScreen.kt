package com.example.finalapp.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants
import com.google.firebase.annotations.concurrent.Background

@Composable
fun CommonErrorScreen(error:String,showButton:Boolean=false,onRetryClicked:()->Unit ={}) {
    Surface(modifier = Modifier.fillMaxSize(), color = Constants.HOME_TOP_BAR_COLOR) {
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
@Composable
fun NoEventsFoundScreen(backgroundColor:Color,error:String,showButton:Boolean=false,onRetryClicked:()->Unit ={}) {
    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Card(
            Modifier
                .padding(bottom = 16.dp)
                .height(50.dp)
                .width(40.dp), backgroundColor = Color.DarkGray) {
            Column(modifier = Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Drop your profile", fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)
                Button(onClick = { onRetryClicked() }) {
                    Text(text = "Retry", fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
        }
    }
}


@Composable
fun NoPingsFoundScreen(backgroundColor:Color=Constants.HOME_TOP_BAR_COLOR,error:String="Error getting pings",showButton:Boolean=false,onRetryClicked:()->Unit ={}) {
    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


                    Text(text = "Error getting pings!", fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp, color = Color.LightGray)
                    Button(onClick = { onRetryClicked() }, colors = ButtonDefaults.buttonColors(backgroundColor= Color.White, contentColor = Color.DarkGray)) {
                        Text(text = "Retry", fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }
                }


    }
}

@Composable
fun NoProfilesFoundScreen(backgroundColor:Color=Constants.HOME_TOP_BAR_COLOR,error:String="Error getting profiles",showButton:Boolean=false,onRetryClicked:()->Unit ={}) {
    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "Error getting profiles",
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 16.sp
                    )
                    Button(onClick = { onRetryClicked() }) {
                        Text(text = "Retry", fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }

        }
    }
}
