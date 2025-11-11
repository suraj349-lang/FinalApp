package com.spint.app.screens._1home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.utils.constants.Constants
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import com.spint.app.ui.theme.floatingActionBtnColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DirectChatHorizontalPager() {
    val pages = listOf(
        DirectChatPagerPages.First,
        DirectChatPagerPages.Second,
        DirectChatPagerPages.Third,
        DirectChatPagerPages.Fourth
    )
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(modifier = Modifier.wrapContentSize()) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreenDirectChat(directChatHorizontalPager = pages[position])
        }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalPagerIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            pagerState = pagerState,
            activeColor = floatingActionBtnColor,
            inactiveColor = Color.LightGray,
            indicatorHeight = 6.dp,
            indicatorWidth = 6.dp
        )
    }
}


@Composable
fun PagerScreenDirectChat(directChatHorizontalPager: DirectChatPagerPages) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp).fillMaxWidth()
            .wrapContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Image(
                painter = painterResource(id = directChatHorizontalPager.image),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )
            Column(modifier = Modifier.wrapContentSize()) {
                Text(
                    text = directChatHorizontalPager.title,
                    fontFamily = Constants.FONT_LIGHT,
                    color = Color(0xFFEEEBF0),
                    fontSize = 18.sp
                )
                Text(
                    text = directChatHorizontalPager.description,
                   // style = TextStyle(textDecoration = TextDecoration.Underline),
                    fontFamily = Constants.ROBOTO_CONDENSED,
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }

        }

    }
}