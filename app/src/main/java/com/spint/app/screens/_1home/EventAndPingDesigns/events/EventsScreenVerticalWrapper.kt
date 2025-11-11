package com.spint.app.screens._1home.EventAndPingDesigns.events


import BottomBar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.model.Event
import com.spint.app.model.EventResponse
import com.spint.app.screens._1home.EventAndPingDesigns.events.templates.xhmaslive.PingsDetailsScreen
import com.spint.app.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.spint.app.screens.common.NoDataFound
import com.spint.app.screens.common.CommonErrorScreen
import com.spint.app.screens.loadingAndErrorScreen.loadingScreen.EventLoadingScreen
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.RequestState
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.EventsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun EventAndChildPostWrapper(
    eventsViewModel: EventsViewModel,
    modifier: Modifier = Modifier,
    initialPage: Int? = 0,
    navController: NavHostController,
    onRetryCalled:()->Unit
) {
    LaunchedEffect(key1 = true){
        eventsViewModel.getAllEvents()
    }
    var showSheet by remember {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage ?: 0,
        pageCount = { (eventsViewModel.eventsListResponse.value as? RequestState.Success<List<Event>>)?.data?.size ?: 0 }
    )
    val eventsState by eventsViewModel.eventsListResponse.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController = navController, containerColor = Constants.HOME_BOTTOM_BAR_COLOR, highlightedTextColor = Constants.BOTTOM_BAR_ACTIVE_TEXT_COLOR, inactiveTextColor = Constants.BOTTOM_BAR_INACTIVE_TEXT_COLOR, inactiveIconColor = Constants.BOTTOM_BAR_INACTIVE_ICON_COLOR, onCreateEventClick = {showSheet=true}) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            when (eventsState) {
                is RequestState.Loading -> {
                    EventLoadingScreen { navController.navigateUp() }
                    // DialogLoading(){navController.navigateUp()}
                }

                is RequestState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Log.e(
                            "Events",
                            "EventsScreen:${(eventsState as RequestState.Error).error} ",
                        )
                        CommonErrorScreen(error = "Unable to fetch events.", true) {
                            eventsViewModel.getAllEvents()
                        }
                    }
                }

                is RequestState.Success -> {
                    val eventList = (eventsState as RequestState.Success<List<EventResponse>>).data
                    if (eventList.isNotEmpty()) {
                        VerticalPager(
                            pageSize = PageSize.Fill,
                            state = pagerState,
                            beyondBoundsPageCount = 1,
                            modifier = modifier.weight(1f)
                        ) { page ->
                            val event = eventList[page]
//                            EventsAndChildPostsParent(
//                                event = event,
//                                navController = navController,
//                                onUpVotesClicked = { eventsViewModel.upvoteEvent(it) }
//                            )
                            PingsDetailsScreen(eventResponse = event)
                        }

                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NoDataFound(
                                "No Events Found!",
                                R.drawable.search,
                                content = { RetryButton(onRetryCalled = onRetryCalled) })
                        }
                    }
                }

                else -> {}
            }
            CreateEventOrPingBottomSheet(
                showSheet = showSheet,
                onDismiss = { showSheet = false },
                navHostController = navController
            )
        }
    }
}

@Composable
fun RetryButton(onRetryCalled: () -> Unit) {
    Button(onClick = { onRetryCalled()}, colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)) {
        Text(text = "Retry", fontFamily = Constants.FONT_MEDIUM, color = Color.White)
    }
}