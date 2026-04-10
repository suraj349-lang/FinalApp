package com.spint.app.screens.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants

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
fun NoPingsFoundScreen(
    backgroundColor: Color = Constants.HOME_TOP_BAR_COLOR,
    error: String = "Something went wrong!",
    showButton: Boolean = false,
    onRetryClicked: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.no_internet),
//                contentDescription = "",
//                modifier = Modifier.size(100.dp)
//            )
            Text(
                text = error,
                fontFamily = Constants.FONT_LIGHT,
                fontSize = 18.sp,
                color = Color.LightGray
            )
            Button(
                onClick = { onRetryClicked() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.DarkGray
                )
            ) {
                Text(
                    text = "Retry",
                    fontFamily = Constants.FONT_MEDIUM,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun NoProfilesFoundScreen(
    backgroundColor: Color = Constants.HOME_TOP_BAR_COLOR,
    error: String = "Error getting profiles",
    showButton: Boolean = true,
    onRetryClicked: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = error,
                fontFamily = Constants.FONT_LIGHT,
                fontSize = 18.sp,
                color = Color.White
            )
            if(showButton) {
                Button(onClick = { onRetryClicked() }, modifier = Modifier.padding(top=20.dp), colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)) {
                    Text(
                        text = "Retry",
                        fontFamily = Constants.FONT_MEDIUM,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

        }
    }
}


@Composable
fun DroppedProfileLoadingScreen(modifier: Modifier = Modifier) {

    val transition = rememberInfiniteTransition(label = "")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // smoother than restart
        ),
        label = ""
    )

    val baseColor = Color(0xFF2A2A2A)
    val highlight = Color(0xFF323232)

    val brush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlight.copy(alpha = 0.7f),
            baseColor
        ),
        start = Offset.Zero,
        end = Offset(600f * progress, 600f * progress) // very wide → no line
    )

    Box(
        modifier = modifier
            .size(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush)
    )
}