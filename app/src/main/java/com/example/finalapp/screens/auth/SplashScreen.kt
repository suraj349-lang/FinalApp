package com.example.finalapp.screens.auth

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.APP_NAME_FONT
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreenUI(navController: NavController, screen: String){
    val auth=FirebaseAuth.getInstance().currentUser;

    val scale= remember { Animatable(0f) }
    LaunchedEffect(key1 = true, block ={
//        scale.animateTo(targetValue = 0.9f, animationSpec = tween(durationMillis = 800, easing = {
//            OvershootInterpolator(8f).getInterpolation(it)
//        }))
        delay(2000L)
        if(auth!=null) navController.navigate(SCREENS.HOME.route){popUpTo(0) };
        else navController.navigate(screen){ popUpTo(0); }

    } )
    val systemUiController = rememberSystemUiController()
    val navBarColor = Color.Gray

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = navBarColor,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(
                color = floatingActionBtnColor,
                darkIcons = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = true
            )
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Gray
    )
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp)) {
                Image(
                    painterResource(id = R.drawable.app_iconn ),
                    contentDescription ="App Icon",
                    contentScale= ContentScale.Fit,
                    modifier = Modifier
                        .size(95.dp).clip(shape = RoundedCornerShape(20.dp))
                )
            }
        }

    }

}