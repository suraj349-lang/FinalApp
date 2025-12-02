package com.spint.app.screens._2pings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.spint.app.R
import com.spint.app.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.spint.app.screens.common.NoPingsFoundScreen
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserLocationObject
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.EventsViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.spint.app.navigation.SCREENS
import com.spint.app.screens.EventAndPingDesigns.flashPosts.FlashPostWithImageScreen
import com.spint.app.screens.EventAndPingDesigns.flashPosts.NoImageFlashPosts
import com.spint.app.screens.EventAndPingDesigns.flashPosts.PrivateFlashPostScreen


@Composable
fun FlashPostsScreen(
    navController: NavHostController,
    eventsViewModel: EventsViewModel,
) {
    val selectedCategory = remember { mutableStateOf("All") }
   // val categories = listOf("All", "Dating", "Personal", "Sports", "Politics", "Adventure")
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
    val userLocation by UserLocationObject.userLocation.collectAsState()
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
        eventsViewModel.getAllPings(userLocation.address.toString())
    }

    val systemUiController = rememberSystemUiController()
    val navBarColor = Constants.HOME_NAV_BAR_COLOR

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = navBarColor,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }
    Scaffold(
        content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                LazyColumn(
                    //state = listState,
                    modifier= Modifier
                        .background(color = Constants.HOME_TOP_BAR_COLOR),
                    )
                {

//                Column(
//                    modifier = Modifier
//                        .padding(it)
//                        .verticalScroll(rememberScrollState())
//                        .fillMaxSize()
//                        .background(Color.Black)
//                ) {
//                    item {
//                        LazyRow {
//                            items(categories.size) { index ->
//                                val category = categories[index]
//                                val isSelected = category == selectedCategory.value
//                                Box(
//                                    modifier = Modifier
//                                        .padding(horizontal = 4.dp, vertical = 8.dp)
//                                        .clip(RoundedCornerShape(15.dp))
//                                        .wrapContentSize()
//                                        .background(if (isSelected) Color(0xFF065F0A) else Color.Gray)
//                                        .clickable { selectedCategory.value = category }
//                                ) {
//                                    Text(
//                                        category,
//                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//                                        color = Color.White,
//                                        fontFamily = Constants.FONT_MEDIUM,
//                                        fontSize = 12.sp
//                                    )
//                                }
//                            }
//                        }
//                    }

                    item {
                        if (searchOn) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .background(Color.DarkGray, shape = MaterialTheme.shapes.medium)
                                    .padding(16.dp)
                            ) {
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = {query-> searchQuery = query },
                                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                                    modifier = Modifier.fillMaxWidth(),
                                    decorationBox = { innerTextField ->
                                        if (searchQuery.isEmpty()) {
                                            Text(
                                                "Search",
                                                color = Color.White,
                                                fontFamily = Constants.FONT_EXTRA_LIGHT
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
                    /*
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
                    */

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
                    allPingsState?.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item {
                                    Surface(Modifier.fillMaxSize()) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.9f)
                                                .padding(top = 2.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = floatingActionBtnColor, strokeCap = StrokeCap.Round, trackColor = Color.Yellow)
                                        }
                                        showLoader = true
                                    }
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item {
                                    Surface(Modifier.fillMaxSize()) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.9f)
                                                .padding(top = 2.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = floatingActionBtnColor, strokeCap = StrokeCap.Round, trackColor = Color.Yellow)
                                        }
                                        showLoader = true
                                    }
                                }
                            }

                            loadState.refresh is LoadState.Error -> {
                                showLoader = false
                                val error = (loadState.refresh as LoadState.Error).error
                                item {
                                    Log.e("Error in getting pings", "PingsScreenUI: $error ")
                                    Column(
                                        Modifier
                                            .padding(top = 200.dp)
                                            .fillMaxWidth()
                                            .fillMaxHeight(0.9f), verticalArrangement = Arrangement.Center) {
                                        NoPingsFoundScreen(error = "Error getting pings.") {
                                            eventsViewModel.getAllPings("")
                                        }
                                    }

                                }
                            }
                        }
                    }
                        allPingsState?.itemCount?.let {count->
                            items(count) { index ->
                                val item = allPingsState[index]
                                Log.i("item", "FlashPostsScreen:$item ")
                                if (item != null) {
//                                    PingItemCard(
//                                        item,
//                                        onShareClicked = {},
//                                        onRespondClicked = {}
//                                    )
                                    //   PingItem1(item)
                                    // PingItem2(item = item)


                                  //  PingItem3(item)
                                  //  PingItem1()
                                   
                                    if(item.image.isNotEmpty() && !item.isPrivate){
                                        FlashPostWithImageScreen(
                                            item,
                                            onFlashPostClicked = {navController.navigate(SCREENS.PING_DETAILS.createRoute(item))},
                                            onCommentButtonClicked = {
                                                navController.navigate(SCREENS.COMMENT.route)
                                            }
                                        )
                                    }else if (item.image.isEmpty() && !item.isPrivate ){
                                       NoImageFlashPosts(flashPostResponse = item)
                                    }else{
                                        PrivateFlashPostScreen(item)
                                    }
                                  

                                    Spacer(modifier = Modifier.height(6.dp))
                                 //  Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.6f))
                                }
                            }
                        }
                    }
                }
        })
    CreateEventOrPingBottomSheet(showSheet = showSheet, onDismiss = { showSheet=!showSheet }, navHostController =navController )
}



//
//@Composable
//fun StatsCard(pingStats: Map<String, Int>) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F)),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(
//                text = "Pings This Week in Your Area",
//                style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
//                modifier = Modifier.padding(bottom = 12.dp)
//            )
//
//            pingStats.forEach { (type, count) ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 6.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = when (type.lowercase()) {
//                            "sports" -> Icons.Default.Info
//                            "politics" -> Icons.Default.Info
//                            "club" -> Icons.Default.Build
//                            "study" -> Icons.Default.AccountCircle
//                            else -> Icons.Default.Info
//                        },
//                        contentDescription = type,
//                        tint = Color.Cyan,
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Text(
//                        text = type.replaceFirstChar { it.uppercaseChar() },
//                        color = Color.White,
//                        modifier = Modifier.weight(1f)
//                    )
//                    Text(
//                        text = "$count",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    }
//}

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
//
//@Composable
//fun StatsChipsRow(pingStats: Map<String, Int>) {
//    LazyRow(
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(pingStats.toList()) { (type, count) ->
//            Surface(
//                color = Color(0xFF2C2C2C),
//                shape = RoundedCornerShape(50),
//                tonalElevation = 2.dp
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(horizontal = 16.dp, vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CheckCircle,
//                        contentDescription = type,
//                        tint = Color.Green,
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(Modifier.width(8.dp))
//                    Text(
//                        text = "$type: $count",
//                        color = Color.White,
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                }
//            }
//        }
//    }
//}