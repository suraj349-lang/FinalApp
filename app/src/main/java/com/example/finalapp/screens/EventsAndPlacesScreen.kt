package com.example.finalapp.screens

import BottomBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun EventsAndPlacesScreen(navController:NavHostController) {
    val buttonsVisible = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val items:List<String> = listOf("Alpha-1","Beta-1","Gamma-1","Delta-1","Delta-2","Gamma-2","Beta-2","Alpha-2","Cp Mall")
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
    ) { it->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

            StaggeredList(items = items)
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
@Composable
fun StaggeredList(items: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
            ) {
                Text(
                    text = items[index],
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}