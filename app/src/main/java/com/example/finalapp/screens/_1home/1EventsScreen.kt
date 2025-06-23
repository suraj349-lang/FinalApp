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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.model.EventRequestDTO
import com.example.finalapp.model.EventResponse
import com.example.finalapp.screens._1home.publicEvent.PublicEventNewUI
import com.example.finalapp.screens.common.NoDataFound
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.testingDataAndScreen.imageUrls
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.EventsViewModel

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel,
    modifier: Modifier = Modifier,
    initialPage: Int? = 0,
    navController: NavHostController,
    onRetryCalled:()->Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage ?: 0,
        pageCount = { (eventsViewModel.eventsListResponse.value as? RequestState.Success<List<EventRequestDTO>>)?.data?.size ?: 0 }
    )
//    val fling = PagerDefaults.flingBehavior(
//        state = pagerState,
//        lowVelocityAnimationSpec = tween(easing = LinearEasing, durationMillis = 300)
//    )
    val index by remember { mutableStateOf(0) }
    val height by remember { mutableStateOf(false) }
    val eventsState by eventsViewModel.eventsListResponse.collectAsState()

    when (eventsState) {
        is RequestState.Loading -> {
            DialogLoading()
        }
        is RequestState.Error -> {
            Column(modifier=Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Log.e("Error in getting events", "EventsScreen:${(eventsState as RequestState.Error).error} ", )
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
                      //  flingBehavior = fling,
                        beyondBoundsPageCount = 1,
                        modifier = modifier.weight(1f)
                    ) { page ->
                        val event = eventList[page]
                        PublicEventNewUI(
                            event,
                            navController
                        )
                    }
                }
            }else{
                Column() {
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