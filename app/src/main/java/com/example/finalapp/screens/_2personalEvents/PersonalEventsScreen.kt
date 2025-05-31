package com.example.finalapp.screens._2personalEvents

import BottomBar
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD

@Composable
fun PersonalEventsScreen(navController:NavHostController) {
    val buttonsVisible = remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
           PersonalEventTopBar()
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            ){}
        }
    ) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar()
               // TrendingCategories(categories)
                TrendingPosts()
            }
        }
    }
}
@Composable
fun TrendingCategories(categories: List<Category>) {
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
                    .clip(RoundedCornerShape(20.dp))
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
fun TrendingPosts() {
    LazyColumn{
        items(30) { index ->
            Box(
                modifier = Modifier.padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.Gray)
            ) {
                Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="",modifier=Modifier.fillMaxSize(), contentScale = ContentScale.Crop )
            }
        }
    }
}

