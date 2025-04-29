package com.example.finalapp.screens._1home


import BottomBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.screens._1home.commonUI.HomeFloatingActionButton
import com.example.finalapp.screens._1home.commonUI.HomeTopBar
import com.example.finalapp.screens._3createEvent.CreateEventBottomSheet
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.screens.dialogBox.ShowQRDialog
import com.example.finalapp.screens.dialogBox.showDialog
import com.example.finalapp.testing.items
import com.example.finalapp.ui.TAB_ITEMS
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.ImageUploadViewModel
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenUI(navController: NavHostController, eventsViewModel: EventsViewModel, imageUploadViewModel: ImageUploadViewModel, authViewModel: AuthViewModel) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val buttonsVisible = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var showQR: showDialog by remember { mutableStateOf(showDialog.CLOSE) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    if (showQR == showDialog.OPEN) {
        ShowQRDialog(
            image = R.drawable.bigqr,
            navController = navController,
            onDismiss = { showQR = showDialog.CLOSE }
        )
    }

    val pagerState = rememberPagerState(0, pageCount = { 3 })
    var showSheet by remember {
        mutableStateOf(false)
    }


    Scaffold(
        topBar = {
            HomeTopBar(
                scrollBehavior,
                Constants.APP_NAME,
                navController,
                true,
                true,
                R.drawable.chat_new
            ) { showQR = showDialog.OPEN }
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
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    drawerContainerColor = Color.Transparent,
                    drawerContentColor = Color.Black
                ) {
                    items.forEachIndexed { index, item ->
                        Spacer(modifier = Modifier.height(10.dp))
                        NavigationDrawerItem(
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFF035697),
                                unselectedContainerColor = Color(0xFFFFFFFF).copy(alpha = 0.8f)
                            ),
                            label = { Text(text = item.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                selectedItemIndex = index
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Image(
                                    painterResource(id = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp)
                                )
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                                .wrapContentSize(),
                            shape = RoundedCornerShape(6.dp)
                        )
                    }
                }
            },
            drawerState = drawerState,
            gesturesEnabled = true
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
//
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = Color(0xFF0A010E) //0xFFEB1809
                            )
                        },
                        backgroundColor = Color(0xFFDF400E), //0xFFD5623E orange , 0xFFBCE697 green
                        modifier = Modifier
                            .border(width = 0.dp, color = Color.White)
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
                                        color = if (pagerState.currentPage == index) Color.White else Color.Black,
                                        fontFamily = Constants.FONT_MEDIUM,//FontFamily(Font(R.font.dongle_light)),
                                        fontSize = 12.sp,//20.sp,
                                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .imePadding()
                    ) { page ->
                        when (page) {
                            0 -> EventsScreen(eventsViewModel = eventsViewModel, navController = navController)//PublicLivePost(videos = listOf("1","2","3","4","5","6") )//PrivateLivePost(videos = listOf("1","2","3","4","5","6") )//LivePosts( scrollBehavior,eventsViewModel, offersList, padding)
                            1 -> DirectChatScreen(scrollBehavior,authViewModel,eventsViewModel,navController)
                            2 -> DroppedProfilesUI(scrollBehavior,navController ,eventsViewModel)
                                //DroppedProfiles(navController = navController, eventsViewModel = eventsViewModel)
                        }
                    }
                }
            }
        }
    }
    CreateEventBottomSheet(
        showSheet = showSheet,
        onDismiss = { showSheet = false },
        navHostController = navController
    )
}

