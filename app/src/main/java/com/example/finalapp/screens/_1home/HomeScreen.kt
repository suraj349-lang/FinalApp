package com.example.finalapp.screens._1home


import BottomBar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.screens._1home.commonUI.HomeFloatingActionButton
import com.example.finalapp.screens._1home.commonUI.HomeTopBar
import com.example.finalapp.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.screens.dialogBox.ShowQRDialog
import com.example.finalapp.screens.dialogBox.ShowDialog
import com.example.finalapp.ui.TAB_ITEMS
import com.example.finalapp.utils.UserLocationObject
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.ImageUploadViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreenUI(navController: NavHostController, eventsViewModel: EventsViewModel, imageUploadViewModel: ImageUploadViewModel, authViewModel: AuthViewModel) {
    val buttonsVisible = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var showQR: ShowDialog by remember { mutableStateOf(ShowDialog.CLOSE) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val userLocation by UserLocationObject.userLocation.collectAsState()


    if (showQR == ShowDialog.OPEN) {
        ShowQRDialog(
            navController = navController,
            onDismiss = { showQR = ShowDialog.CLOSE }
        )
    }

    val pagerState = rememberPagerState(0, pageCount = { 3 })
    var showSheet by remember {
        mutableStateOf(false)
    }
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing && pagerState.currentPage==0) {
            delay(1000L)
            eventsViewModel.getAllEvents()
            delay(500L)
            isRefreshing = false
        }
        else if (isRefreshing && pagerState.currentPage==1) {
            delay(1000L)
            eventsViewModel.loadDirectChatUsers(userLocation.latitude ?: 0.0,userLocation.longitude ?: 0.0)
            delay(500L)
            isRefreshing = false
        }
        else if (isRefreshing && pagerState.currentPage==2) {
            delay(1000L)
            eventsViewModel.getDefaultDropProfiles("")
            delay(500L)
            isRefreshing = false
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { isRefreshing = true }
    )


    Scaffold(
        topBar = {
            HomeTopBar(
                backgroundColor = Constants.HOME_TOP_BAR_COLOR,
                iconAndTextColor= Constants.HOME_TOP_BAR_ICON_COLOR,
                scrollBehavior = scrollBehavior,
                title = Constants.APP_NAME,
                titleColor=Constants.HOME_TOP_BAR_TITLE_COLOR,
                navController = navController,
                navIcon = true,
                actionIcon = true,
                icon = R.drawable.chat_new
            ) { showQR = ShowDialog.OPEN }
        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
        bottomBar = {
            AnimatedVisibility(
                visible = scrollBehavior.state.overlappedFraction == 0f, // Hide on scroll
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BottomBar(
                    navController = navController,
                    containerColor=Constants.HOME_BOTTOM_BAR_COLOR,
                    highlightedTextColor=Constants.BOTTOM_BAR_ACTIVE_TEXT_COLOR,
                    inactiveIconColor = Constants.BOTTOM_BAR_INACTIVE_ICON_COLOR,
                    inactiveTextColor = Constants.BOTTOM_BAR_INACTIVE_TEXT_COLOR,
                    state = buttonsVisible,
                    modifier = Modifier
                        .height(30.dp)
                        .navigationBarsPadding()){
                           showSheet=true
                        }
            }
        },

        floatingActionButton = {
            HomeFloatingActionButton(authViewModel, eventsViewModel, imageUploadViewModel , navController)
        }
    ) { padding ->
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = Constants.TAB_ROW_INDICATOR_COLOR,
                                height = 2.dp
                            )
                        },
                        backgroundColor = Constants.HOME_TOP_BAR_COLOR,
                        modifier = Modifier
                            //   .border(width = 0.dp, color = Color.White)
                            .padding(bottom = 0.dp)
                            .fillMaxWidth()
                            .height(35.dp)
                    ) {
                        TAB_ITEMS.forEachIndexed { index, item ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = {
                                    Text(
                                        text = item.title,
                                        color = if (pagerState.currentPage == index) Constants.TAB_ROW_ACTIVE_TEXT_COLOR /*Color(0xFFDF400E)*/ else Constants.TAB_ROW_INACTIVE_COLOR,
                                        fontFamily = Constants.FONT_MEDIUM,//FontFamily(Font(R.font.dongle_light)),
                                        fontSize = 12.sp,//20.sp,
                                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .imePadding()
                        ) { page ->
                            when (page) {
                                0 -> EventScreenWrapper(eventsViewModel = eventsViewModel, navController = navController, onRetryCalled = {eventsViewModel.getAllEvents()})
                                1 -> DirectChatScreen(scrollBehavior, authViewModel, eventsViewModel, navController)
                                2 -> DroppedProfilesUI(pagerState,scrollBehavior, navController, eventsViewModel)
                            }
                        }

                        PullRefreshIndicator(
                            refreshing = isRefreshing,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }

                }
            }
    }
    CreateEventOrPingBottomSheet(
        showSheet = showSheet,
        onDismiss = { showSheet = false },
        navHostController = navController
    )
}

