package com.example.finalapp.screens._1home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.onboarding.screen.FinishButton
import com.example.finalapp.screens.onboarding.util.OnBoardingPage
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.WelcomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import com.example.finalapp.ui.theme.floatingActionBtnColor
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
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalPagerIndicator(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            pagerState = pagerState,
            activeColor = floatingActionBtnColor,
            inactiveColor = Color.LightGray
        )
    }
}


@Composable
fun PagerScreenDirectChat(directChatHorizontalPager: DirectChatPagerPages) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = directChatHorizontalPager.image),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = directChatHorizontalPager.title,
            fontFamily = Constants.FONT_LIGHT,
            color = Color(0xFFEEEBF0),
            fontSize = 24.sp
        )
        Text(
            text = directChatHorizontalPager.description,
            style = TextStyle(textDecoration = TextDecoration.Underline),
            fontFamily = Constants.FONT_MEDIUM,
            color = Color(0xFFB7AEBB),
            fontSize = 14.sp
        )
    }
}
