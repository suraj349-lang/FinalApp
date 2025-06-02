package com.example.finalapp.screens._2pings

import BottomBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.screens._3createEvent.CreateEventOrPingBottomSheet
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD

@Composable
fun PingsScreenUI(navController:NavHostController) {
    val buttonsVisible = remember { mutableStateOf(true) }
    var searchBox by remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    var isScrollingUp by remember { mutableStateOf(true) }
    var previousIndex by remember { mutableStateOf(0) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    var showSheet by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val currentIndex = listState.firstVisibleItemIndex
        val currentOffset = listState.firstVisibleItemScrollOffset

        isScrollingUp = when {
            currentIndex < previousIndex -> true
            currentIndex > previousIndex -> false
            currentOffset < previousScrollOffset -> true
            currentOffset > previousScrollOffset -> false
            else -> isScrollingUp
        }

        previousIndex = currentIndex
        previousScrollOffset = currentOffset
    }

    Scaffold(
        topBar = {
           PingsTopBar(showIcon = searchBox) { searchBox = !searchBox }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            ){
                showSheet=true
            }
        }
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = isScrollingUp) {
                    Column {
                        SearchBar()
                        PingCategories(categories)
                    }
                }

                PingsScreen(listState)
            }
        }
        CreateEventOrPingBottomSheet(showSheet = showSheet, onDismiss = { showSheet=!showSheet }, navHostController =navController )
    }
}
@Composable
fun PingCategories(categories: List<Category>) {
    var selectedCategory by remember { mutableStateOf<Category?>(categories[0]) }
    val lazyListState = rememberLazyListState()
    LaunchedEffect(selectedCategory) {
        selectedCategory?.let { category ->
            val index = categories.indexOf(category)
            if (index != -1) {
                lazyListState.scrollToItem(index)
            }
        }
    }

    LazyRow(
        state=lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Add space between items
    ) {
        items(categories) { category ->
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    //.padding(vertical = 4.dp) // Add vertical padding for better spacing
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        color = if (category == selectedCategory) Color(0xFF074C91) else Color.LightGray
                    )
                    .clickable {
                        selectedCategory = if (category == selectedCategory) null else category
                    }
            ) {
                Text(
                    text = category.name,
                    fontFamily = DONGLE_BOLD,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp), // Add padding inside the Box
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PingsScreen(listState: LazyListState) {
    LazyColumn(state = listState){
        items(30) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxSize()
            ) {
                PingsItemUI()
            }
        }
    }
}

