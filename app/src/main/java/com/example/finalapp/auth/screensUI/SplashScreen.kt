package com.example.finalapp.auth.screensUI

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreenUI(navController: NavController){
    val auth=FirebaseAuth.getInstance().currentUser;

    val scale= remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true, block ={
        scale.animateTo(targetValue = 0.9f, animationSpec = tween(durationMillis = 800, easing = {
            OvershootInterpolator(8f).getInterpolation(it)
        }))
        delay(2000L)
        if(auth!=null) navController.navigate(SCREENS.HOME.route){popUpTo(0) };
        else navController.navigate(SCREENS.LOGIN.route){ popUpTo(0); }

    } )
    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White)
    {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.frisbee ),
                contentDescription ="App Icon" +
                        "",
                contentScale= ContentScale.Fit,
                modifier = Modifier.size(95.dp)
            )
            Text(text="FRISBEE", fontSize = 45.sp, modifier = Modifier.padding(top=8.dp, bottom = 0.dp), color = statusAndTopAppBarColor, style = MaterialTheme.typography.titleMedium)


        }

    }

}