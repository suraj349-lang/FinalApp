package com.example.finalapp.screens._1home


import BottomBar
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.User
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._1home.utils.HomeFloatingActionButton
import com.example.finalapp.screens._1home.utils.HomeTopBar
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.screens.dialogBox.ShowQRDialog
import com.example.finalapp.screens.dialogBox.showDialog
import com.example.finalapp.testing.items
import com.example.finalapp.ui.TAB_ITEMS
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.homeTopBarIconsColor
import com.example.finalapp.ui.theme.statusBarColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.TAG
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants.FONT_MEDIUM
import com.example.finalapp.viewmodels.ImageUploadViewModel
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenUI(navController: NavHostController, eventsViewModel: EventsViewModel, imageUploadViewModel: ImageUploadViewModel, authViewModel: AuthViewModel) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val buttonsVisible = remember { mutableStateOf(true) }
    val eventsViewModel = hiltViewModel<EventsViewModel>()
    val offersList = eventsViewModel.offersList.value
    val scope = rememberCoroutineScope()
    val heightInDp = LocalConfiguration.current.screenHeightDp.dp * 0.78f
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
        bottomBar = {
            AnimatedVisibility(
                visible = scrollBehavior.state.overlappedFraction == 0f, // Hide on scroll
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                BottomBar(
                    navController = navController,
                    state = buttonsVisible,
                    modifier = Modifier.height(30.dp)
                )
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
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFFDCD6DD)
                    )
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = Color(0xFFEB1809)
                            )
                        },
                        backgroundColor = Color(0xFFFFFFFE),
                        modifier = Modifier
                            .padding(bottom = 0.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        TAB_ITEMS.forEachIndexed { index, item ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = {
                                    Text(
                                        text = item.title,
                                        color = if (pagerState.currentPage == index) Color(0xFF000000) else Color(0xFF636368),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 18.sp,
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
                    ) { page ->
                        when (page) {
                            0 -> LivePosts( scrollBehavior,eventsViewModel, offersList, padding)
                            1 -> DirectChatScreen(scrollBehavior,authViewModel,eventsViewModel,navController)
                            2 -> DroppedProfilesNew(scrollBehavior,navController ,eventsViewModel)
                                //DroppedProfiles(navController = navController, eventsViewModel = eventsViewModel)
                        }
                    }
                }
            }
        }
    }
}

