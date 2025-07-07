package com.example.finalapp.screens._4profile.privateUsername

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.screens._1home.commonUI.HomeTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateUserNameScreenWrapper(navController: NavHostController) {
    Scaffold(
        topBar = {
            HomeTopBar(
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = "___s____3494",
                navController = navController,
                navIcon = true,
                actionIcon = false
            )
        }
    , content = {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            PrivateUserNameScreen()

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