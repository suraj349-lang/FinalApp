package com.example.finalapp.screens._2pings.old

import BottomBar
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.screens._1home.EventAndPingDesigns.flashPosts.PingItem3
import com.example.finalapp.screens._2pings.Category
import com.example.finalapp.screens._2pings.PingsTopBar
import com.example.finalapp.screens._2pings.SearchBar
import com.example.finalapp.screens._2pings.categories
import com.example.finalapp.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.example.finalapp.screens.common.NoPingsFoundScreen
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.UserLocationObject
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.viewmodels.EventsViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PingsScreenUI(navController:NavHostController,eventsViewModel: EventsViewModel) {
    val buttonsVisible = remember { mutableStateOf(true) }
    var searchBox by remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var isScrollingUp by remember { mutableStateOf(true) }
    var previousIndex by remember { mutableStateOf(0) }
    var previousScrollOffset by remember { mutableStateOf(0) }
    val userLocation by UserLocationObject.userLocation.collectAsState()
    var showSheet by remember {
        mutableStateOf(false)
    }
    val allPings by eventsViewModel.allPingsFlow.collectAsState()
    val allPingsState = allPings?.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit){
        eventsViewModel.getAllPings(userLocation.address.toString())
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset to listState.firstVisibleItemIndex }
            .collect { (offset, index) ->
                isScrollingUp = when {
                    index < previousIndex -> true
                    index > previousIndex -> false
                    offset < previousScrollOffset -> true
                    offset > previousScrollOffset -> false
                    else -> isScrollingUp
                }

                previousIndex = index
                previousScrollOffset = offset
            }
    }

    val systemUiController = rememberSystemUiController()
    val navBarColor = Color(0xFF121212)
    val backgroundColor= Color(0xFF121212)

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
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(
                color = floatingActionBtnColor,
                darkIcons = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = true
            )
        }
    }


    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = isScrollingUp,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
            ) {
                PingsTopBar(navBarColor, showIcon = searchBox) { searchBox = !searchBox }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isScrollingUp,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
            ) {
                BottomBar(
                    navController = navController,
                    state = buttonsVisible,
                    modifier = Modifier.height(45.dp),
                    containerColor = navBarColor,
                    highlightedTextColor = Color.White
                ) {
                    showSheet = true
                }
            }
        }
    ) { it ->
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize(), color = backgroundColor) {
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = isScrollingUp,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        if (showLoader && allPingsState?.itemCount == 0) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = floatingActionBtnColor
                            )
                        }
                        if (searchBox) SearchBar()
                        PingCategories(categories)
                    }
                }



                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(top = 0.dp, bottom = 16.dp))
                {
                   // val number=Random.nextInt()
                        allPingsState?.itemCount?.let {
                            Log.i("POSTCOUNT", "PingsScreenUI: $it")
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
                                    
//                                    if(number/2==0) {
//                                        PingItem2(item)
//                                    }else{
//                                        PingItem1()
//                                    }
                                    Divider(
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
                                        NoPingsFoundScreen(error = "Error getting pings.") {
                                             eventsViewModel.getAllPings("")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
        CreateEventOrPingBottomSheet(showSheet = showSheet, onDismiss = { showSheet=!showSheet }, navHostController =navController )
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
            .padding(start = 4.dp, top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Add space between items
    ) {
        items(categories) { category ->
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    //.padding(vertical = 4.dp) // Add vertical padding for better spacing
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        color = if (category == selectedCategory) floatingActionBtnColor else Color.LightGray //category.color
                    )
                    .clickable {
                        selectedCategory = if (category == selectedCategory) null else category
                    }
//                    .border(
//                        width = 3.dp,
//                        color = if (category == selectedCategory) Color.White else Color.LightGray,
//                        shape = RoundedCornerShape(12.dp)
//                    )
            ) {
                Text(
                    text = category.name,
                    fontFamily = DONGLE_BOLD,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp), // Add padding inside the Box
                    color = Color.White,
                    fontSize =18.sp
                )
            }
        }
    }
}

@Composable
fun PingsScreen(listState: LazyListState, item: DropProfileResponse) {

}

