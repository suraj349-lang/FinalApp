package com.example.finalapp.screens

import BottomBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.utils.Constants.Constants

@Composable
fun SearchScreenUI(navController:NavHostController) {
    val buttonsVisible = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
           SearchTopBar()
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            )
        }
    ) { it ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "search")

            }

        }
    }
}

@Composable
fun SearchTopBar() {
    var searchText by remember {
        mutableStateOf("")
    }
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFE))) {
        OutlinedTextField(
            modifier= Modifier
                .fillMaxSize()
                .padding(4.dp),
            value =searchText ,
            onValueChange ={searchText=it},
            placeholder = { Text(text = "Search")},
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription ="" )},

        )

    }
}
