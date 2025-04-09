package com.example.finalapp.screens._1home.events

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.finalapp.screens._1home._1_1Events.PrivateLiveEvent1
import com.example.finalapp.testingp.PrivateLiveEventInUse
import com.example.finalapp.testingp.PublicActiveEvent
import com.example.finalapp.testingp.imageUrls
import com.example.finalapp.viewmodels.EventsViewModel

@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ActiveEvents(
    eventsViewModel: EventsViewModel,
    modifier: Modifier = Modifier,
    videos: List<String>,
    initialPage: Int? = 0,
    navController: NavHostController

) {
    val pagerState = rememberPagerState(initialPage = initialPage ?: 0, pageCount = { videos.size })
    val fling = PagerDefaults.flingBehavior(
        state = pagerState, lowVelocityAnimationSpec = tween(
            easing = LinearEasing, durationMillis = 300
        )
    )
    val index by remember {
        mutableStateOf(0)
    }
    val height by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        VerticalPager(
            pageSize = PageSize.Fill,
            state = pagerState,
            flingBehavior = fling,
            beyondBoundsPageCount = 1,//todo set to 1 to save memory
            modifier = modifier.weight(1f)
        ) {
            if(it%2==0) {
                //PrivateLiveEventInUse(navController )
                PrivateLiveEvent1()
            }else {
                PublicActiveEvent(index, height, imageUrls)
            }
        }
    }
}