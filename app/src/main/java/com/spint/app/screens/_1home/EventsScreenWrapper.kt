package com.spint.app.screens._1home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.model.Event
import com.spint.app.model.EventResponse
import com.spint.app.screens.common.NoDataFound
import com.spint.app.screens.common.CommonErrorScreen
import com.spint.app.screens.loadingAndErrorScreen.loadingScreen.EventLoadingScreen
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.RequestState
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun EventScreenWrapper(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    initialPage: Int? = 0,
    navController: NavHostController,
    onRetryCalled:()->Unit
) {
    LaunchedEffect(key1 = true){
        homeViewModel.getAllEvents()
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage ?: 0,
        pageCount = { (homeViewModel.eventsListResponse.value as? RequestState.Success<List<Event>>)?.data?.size ?: 0 }
    )
    val eventsState by homeViewModel.eventsListResponse.collectAsState()

    when (eventsState) {
        is RequestState.Loading -> {
            EventLoadingScreen{navController.navigateUp()}
           // DialogLoading(){navController.navigateUp()}
        }
        is RequestState.Error -> {
            Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Log.e("Events", "EventsScreen:${(eventsState as RequestState.Error).error} ", )
                CommonErrorScreen(error = "Unable to fetch events.",true){
                    homeViewModel.getAllEvents()
                }
            }
        }
        is RequestState.Success -> {
            val eventList = (eventsState as RequestState.Success<List<EventResponse>>).data
            if(eventList.isNotEmpty()) {
//                Column(modifier = Modifier.fillMaxSize()) {
//                    VerticalPager(
//                        pageSize = PageSize.Fill,
//                        state = pagerState,
//                        beyondBoundsPageCount = 1,
//                        modifier = modifier.weight(1f)
//                    ) { page ->
//                        val event = eventList[page]
//                        EventScreen(
//                            event=event,
//                            navController=navController,
//                            onUpVotesClicked ={eventsViewModel.upvoteEvent(it)}
//                        )
//                    }
//                }
                Surface(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
                    LazyColumn(modifier = Modifier.fillMaxSize().background(color = Color.White)){
                        items(eventList){event->
//                            EventScreen(
//                                event =event,
//                                navController = navController,
//                                onUpVotesClicked = { eventsViewModel.upvoteEvent(it) }
//                            )
//                            ClassicEventUI(
//                                event =event,
//                                navController = navController,
//                                onUpVotesClicked = { eventsViewModel.upvoteEvent(it) }
//                            )
                        }
                    }
                }
            }else{
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    NoDataFound("No Events Found!", R.drawable.search, content ={ RetryButton(onRetryCalled=onRetryCalled)})
                }
            }
        }
        else -> {}
    }
}

@Composable
fun RetryButton(onRetryCalled: () -> Unit) {
    Button(onClick = { onRetryCalled()}, colors = ButtonDefaults.buttonColors(backgroundColor = floatingActionBtnColor)) {
        Text(text = "Retry", fontFamily = Constants.FONT_MEDIUM, color = Color.White)
    }
}