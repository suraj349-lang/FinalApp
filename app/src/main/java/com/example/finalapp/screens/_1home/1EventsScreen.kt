package com.example.finalapp.screens._1home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.model.Event
import com.example.finalapp.model.EventResponse
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.EventScreen
import com.example.finalapp.screens.common.NoDataFound
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.EventsViewModel

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun EventScreenWrapper(
    eventsViewModel: EventsViewModel,
    modifier: Modifier = Modifier,
    initialPage: Int? = 0,
    navController: NavHostController,
    onRetryCalled:()->Unit
) {
//    LaunchedEffect(key1 = true){
//        eventsViewModel.getAllEvents()
//    }
    val pagerState = rememberPagerState(
        initialPage = initialPage ?: 0,
        pageCount = { (eventsViewModel.eventsListResponse.value as? RequestState.Success<List<Event>>)?.data?.size ?: 0 }
    )
    val eventsState by eventsViewModel.eventsListResponse.collectAsState()

    when (eventsState) {
        is RequestState.Loading -> {
            DialogLoading(){navController.navigateUp()}
        }
        is RequestState.Error -> {
            Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Log.e("Events", "EventsScreen:${(eventsState as RequestState.Error).error} ", )
                CommonErrorScreen(error = "Unable to fetch events.",true){
                    eventsViewModel.getAllEvents()
                }
            }
        }
        is RequestState.Success -> {
            val eventList = (eventsState as RequestState.Success<List<EventResponse>>).data
            if(eventList.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    VerticalPager(
                        pageSize = PageSize.Fill,
                        state = pagerState,
                        beyondBoundsPageCount = 1,
                        modifier = modifier.weight(1f)
                    ) { page ->
                        val event = eventList[page]
                        EventScreen(
                            event=event,
                            navController=navController,
                            onUpVotesClicked ={eventsViewModel.upvoteEvent(it)}
                        )
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