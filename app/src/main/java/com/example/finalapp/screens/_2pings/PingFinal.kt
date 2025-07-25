package com.example.finalapp.screens._2pings

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.finalapp.R
import com.example.finalapp.screens._1home.EventAndPingDesigns.pings.PingItem3
import com.example.finalapp.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.UserLocation
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.EventsViewModel


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PingScreenFinal(
    navController: NavHostController,
    eventsViewModel: EventsViewModel,
) {
    val selectedCategory = remember { mutableStateOf("All") }
    val categories = listOf("All", "Sports", "Politics", "Adventure", "Dating", "Personal")
    val gridItems = listOf("Alpha-1", "Pari Chowk")
    val shorts = listOf("Ending in hours", "Today's pings", "Ending this week")
    val staggeredItems = listOf("Music", "Travel", "Education", "Gaming", "Art", "Food")
    var searchOn by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val allPings by eventsViewModel.allPingsFlow.collectAsState()
    val allPingsState = allPings?.collectAsLazyPagingItems()
    var showLoader by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val cities = listOf(R.drawable.img_3, R.drawable.img_4)
    val girls = listOf(R.drawable.img, R.drawable.img_1, R.drawable.img_2)
    val cardColors = listOf(
        Color(0xFFEF476F), // Red
        Color(0xFF03CC98), // Green
        Color(0xFF118AB2), // Blue
        Color(0xFFEEA807), // Yellow
        Color(0xFF8338EC), // Purple
        Color(0xFFFB5607)  // Orange
    )

    val examplePingStats = mapOf(
        "Club" to 12,
        "Sports" to 8,
        "Politics" to 5,
        "Study" to 10f
    )
    LaunchedEffect(key1 = Unit){
        eventsViewModel.getAllPings(UserLocation.address.toString())
    }
    Scaffold(
        topBar = {
            PingsTopBar(backgroundColor = Color.DarkGray, false,{navController.navigateUp()}) {
                searchOn = !searchOn
            }
        },
        content = {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    //state = listState,
                    modifier=Modifier.padding(it).background(color = Color.Black),
                    contentPadding = PaddingValues(top = 0.dp, bottom = 16.dp))
                {

//                Column(
//                    modifier = Modifier
//                        .padding(it)
//                        .verticalScroll(rememberScrollState())
//                        .fillMaxSize()
//                        .background(Color.Black)
//                ) {
                    item {
                        LazyRow(modifier = Modifier.padding(8.dp)) {
                            items(categories.size) { index ->
                                val category = categories[index]
                                val isSelected = category == selectedCategory.value
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .wrapContentSize()
                                        .background(if (isSelected) Color(0xFF065F0A) else Color.Gray)
                                        .clickable { selectedCategory.value = category }
                                ) {
                                    Text(
                                        category,
                                        modifier = Modifier.padding(6.dp),
                                        color = Color.White,
                                        fontFamily = Constants.FONT_LIGHT
                                    )
                                }
                            }
                        }
                    }

                    item {
                        if (searchOn) {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .background(Color.DarkGray, shape = MaterialTheme.shapes.medium)
                                    .padding(12.dp)
                            ) {
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                                    modifier = Modifier.fillMaxWidth(),
                                    decorationBox = { innerTextField ->
                                        if (searchQuery.isEmpty()) {
                                            Text(
                                                "Search",
                                                color = Color.Gray,
                                                fontFamily = Constants.FONT_MEDIUM
                                            )
                                        }
                                        innerTextField()
                                    }
                                )
                            }
                        }
                    }
                    //        StatsCard(examplePingStats)
//        StatsGrid(examplePingStats)
                    //VerticalBarStats(examplePingStats)
//        StatsChipsRow(examplePingStats)
                   item {
                       Text(
                           "Discover near you.",
                           color = Color.White,
                           modifier = Modifier.padding(start = 8.dp),
                           fontFamily = Constants.FONT_MEDIUM,
                           fontWeight = FontWeight.Bold
                       )
                   }
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                // .padding(horizontal = 8.dp)
                                .height(100.dp)
                        ) {
                            itemsIndexed(gridItems) { index, item ->
                                Card(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                        .height(80.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Image(
                                            painter = painterResource(cities[index]),
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            item,
                                            color = Color.Black,
                                            fontFamily = Constants.FONT_MEDIUM,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Text(
                            "Recent",
                            modifier = Modifier.padding(8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Constants.FONT_MEDIUM
                        )
                    }
                    item {
                        LazyRow(modifier = Modifier.padding(8.dp)) {
                            itemsIndexed(shorts) { index, item ->
                                Card(
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .width(120.dp)
                                        .height(200.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.BottomStart,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Image(
                                            painter = painterResource(girls[index]),
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            item,
                                            color = Color.White,
                                            fontFamily = Constants.FONT_MEDIUM,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

//                    Column(
//                        modifier = Modifier
//                            .padding(8.dp)
//                    ) {
//                        Text(
//                            "Explore",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = Constants.FONT_MEDIUM,
//                            modifier = Modifier.padding(bottom = 8.dp)
//                        )
//                        val groupedItems = staggeredItems.withIndex().chunked(2)
//
//                        Column(modifier = Modifier.fillMaxWidth()) {
//                            groupedItems.forEach { rowItems ->
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                                ) {
//                                    rowItems.forEach { (globalIndex, tag) ->
//                                        val color = cardColors[globalIndex % cardColors.size].copy(alpha = 0.7f)
//                                        Card(
//                                            modifier = Modifier
//                                                .weight(1f)
//                                                .height(40.dp),
//                                            shape = RoundedCornerShape(16.dp),
//                                            colors = CardDefaults.cardColors(containerColor = color)
//                                        ) {
//                                            Box(
//                                                modifier = Modifier.fillMaxSize(),
//                                                contentAlignment = Alignment.Center
//                                            ) {
//                                                Text(
//                                                    text = tag,
//                                                    color = Color.White,
//                                                    fontFamily = Constants.FONT_MEDIUM,
//                                                    fontWeight = FontWeight.Bold,
//                                                    fontSize = 18.sp
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                                Spacer(modifier = Modifier.height(8.dp))
//                            }
//                        }
//
//                    }
                        //PingsScreenUI(navController = navController, eventsViewModel = eventsViewModel)

                        // val number=Random.nextInt()
                        allPingsState?.itemCount?.let {
                            items(it) { index ->
                                val item = allPingsState[index]
                                if (item != null) {
//                                    PingItemCard(
//                                        item,
//                                        onShareClicked = {},
//                                        onRespondClicked = {}
//                                    )
                                    //   PingItem1(item)
                                    // PingItem2(item = item)
                                    PingItem3(item)
                                    androidx.compose.material.Divider(
                                        modifier = Modifier.fillMaxWidth(),
                                        thickness = 0.5.dp,
                                        color = Color.LightGray.copy(alpha = 0.2f)
                                    )
                                }
                            }
                        }
                        allPingsState?.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item {
                                        showLoader = true
                                    }
                                }

                                loadState.append is LoadState.Loading -> {
                                    item {
                                        showLoader = true
                                    }
                                }

                                loadState.refresh is LoadState.Error -> {
                                    showLoader = false
                                    val error = (loadState.refresh as LoadState.Error).error
                                    item {
                                        Log.e("Error in getting pings", "PingsScreenUI: $error ")
                                        CommonErrorScreen(error = "Error getting pings.", true) {
                                            // eventsViewModel.getAllPings("")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        })
    CreateEventOrPingBottomSheet(showSheet = showSheet, onDismiss = { showSheet=!showSheet }, navHostController =navController )
}




@Composable
fun StatsCard(pingStats: Map<String, Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Pings This Week in Your Area",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            pingStats.forEach { (type, count) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (type.lowercase()) {
                            "sports" -> Icons.Default.Info
                            "politics" -> Icons.Default.Info
                            "club" -> Icons.Default.Build
                            "study" -> Icons.Default.AccountCircle
                            else -> Icons.Default.Info
                        },
                        contentDescription = type,
                        tint = Color.Cyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = type.replaceFirstChar { it.uppercaseChar() },
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "$count",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StatsGrid(pingStats: Map<String, Int>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pingStats.toList()) { (type, count) ->
            Card(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(type, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("$count this week", color = Color.LightGray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun VerticalBarStats(pingStats: Map<String, Int>) {
    val maxCount = pingStats.values.maxOrNull() ?: 1

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Pings by Category",
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            pingStats.forEach { (type, count) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .height((100 * count / maxCount).dp.coerceAtLeast(8.dp))
                            .width(24.dp)
                            .background(Color.Cyan, shape = RoundedCornerShape(4.dp))
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(type, color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun StatsChipsRow(pingStats: Map<String, Int>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pingStats.toList()) { (type, count) ->
            Surface(
                color = Color(0xFF2C2C2C),
                shape = RoundedCornerShape(50),
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = type,
                        tint = Color.Green,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "$type: $count",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}