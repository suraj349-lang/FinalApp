package com.example.finalapp.screens._8notification

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.fcm.screen.ChatScreenUIFCM
import com.example.finalapp.screens._1home.commonUI.HomeTopBar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationsScreenUI(navController:NavHostController){
    val buttonVisible=remember { mutableStateOf(false)};

    Scaffold(
        topBar = { HomeTopBar(
            backgroundColor= Color.DarkGray,
            scrollBehavior=TopAppBarDefaults.enterAlwaysScrollBehavior(),
            title = "Notifications",
            navController,
            false,
            false,
            R.drawable.settings
        ) },
        bottomBar = { BottomBar(navController =navController , state = buttonVisible,modifier = Modifier.height(45.dp)){} }
    ) {
        Surface(Modifier.fillMaxSize()) {
            ChatScreenUIFCM()

        }

    }

}