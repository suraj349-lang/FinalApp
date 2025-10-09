package com.example.finalapp.screens._4profile.privateUsername

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.screens._1home.commonUI.HomeTopBar
import com.example.finalapp.utils.constants.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateUserNameScreenWrapper(navController: NavHostController) {
    Scaffold(
        //topBar = {PrivateProfileTopBar(){navController.navigateUp()} },
        content = {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp).background(Constants.HOME_TOP_BAR_COLOR)) {
                    Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
                            .size(24.dp)
                            .clickable { navController.navigateUp() },
                        colorFilter = ColorFilter.tint(Color.White),
                    )


                }
                PrivateUserNameScreen()
            }
        }
    }
    )
}

@Composable
fun PrivateUserNameScreen() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Private username")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateProfileTopBar(onBackClicked:()->Unit) {
    TopAppBar(title = {
        Text(
            text = "Private username",
            fontFamily = Constants.ROBOTO_CONDENSED,
            fontSize = 16.sp,
            color = Color.White
        )
    },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Constants.HOME_TOP_BAR_COLOR),
        navigationIcon = {
            Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClicked() },
                colorFilter = ColorFilter.tint(Color.White),
            )
        })
}