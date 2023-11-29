package com.example.finalapp.screens

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreenUI(navController: NavHostController){
    val buttonVisible=remember { mutableStateOf(false)};
    Scaffold(
        topBar = { HomeTopBar(title = "Chat", navController )},
        bottomBar = {BottomBar(navController = navController, state = buttonVisible, modifier = Modifier.height(45.dp))})
    {
        Surface() {

        }

    }
}