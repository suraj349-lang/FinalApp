package com.example.finalapp.screens._2search

import BottomBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD

@Composable
fun TrendingScreen(navController:NavHostController) {
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
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar()
                TrendingCategories(categories)
                StaggeredList(items = items)
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
        modifier = Modifier.fillMaxWidth().padding(start=10.dp),
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
fun SearchTopBar() {
    TopAppBar(
        title = { Text(text = "Trending", fontFamily = DONGLE_BOLD, fontSize = 24.sp, color = Color.White )},
        navigationIcon = {},
        actions = {},
        backgroundColor = Color(0xFF43075C)
    )

}

@Composable
fun SearchBar() {
    var searchText by remember {
        mutableStateOf("")
    }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFE))
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray,
                    cursorColor = Color.Red
                ),
                //textStyle = TextStyle(fontSize = 14.sp),
                placeholder = { Text(text = "Search", fontFamily = DONGLE_BOLD) },
            )
        }
    }

@Composable
fun StaggeredList(items: List<String>) {
    val items=remember{items}
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

data class Category(
    val name: String,
    var isSelected: Boolean = false
)
val categories = listOf(
    Category("All"),
    Category("Sports"),
    Category("Cinema"),
    Category("Politics"),
    Category("Entertainment"),
    Category("News"),
    Category("Comedy"),
    Category("Crime"),
    Category("Elections"),
    Category("Protest")
)