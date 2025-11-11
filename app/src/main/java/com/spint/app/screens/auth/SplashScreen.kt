package com.spint.app.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.spint.app.navigation.SCREENS
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.constants.Constants.APP_NAME_FONT
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreenUI(navController: NavController, screen: String){
    val auth=FirebaseAuth.getInstance().currentUser;
    val color=Constants.SPLASH_SCREEN_COLOR

    LaunchedEffect(key1 = true, block ={
       // delay(1000L)
        if(auth!=null) navController.navigate(SCREENS.HOME.route){popUpTo(0) };
        else navController.navigate(screen){ popUpTo(0); }

    } )
    val systemUiController = rememberSystemUiController()
    val navBarColor =color

//    SideEffect {
//        systemUiController.setNavigationBarColor(
//            color = navBarColor,
//            darkIcons = false
//        )
//        systemUiController.setStatusBarColor(
//            color = navBarColor,     // Your desired color
//            darkIcons = false        // true = dark icons (for light backgrounds)
//        )
//    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = color //0xFFDB3B08
    )
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(modifier = Modifier.wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.Transparent), shape = RoundedCornerShape(20.dp)) {
                Text(text = Constants.APP_NAME, fontSize = 30.sp, fontFamily = APP_NAME_FONT, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

    }

}