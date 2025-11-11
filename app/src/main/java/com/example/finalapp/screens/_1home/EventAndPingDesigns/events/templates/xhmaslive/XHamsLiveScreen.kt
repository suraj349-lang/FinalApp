package com.example.finalapp.screens._1home.EventAndPingDesigns.events.templates.xhmaslive


import BottomBar
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.model.Event
import com.example.finalapp.model.EventResponse
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.RetryButton
import com.example.finalapp.screens._1home.EventAndPingDesigns.events.utils.FollowButtonVertical
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.screens.common.NoDataFound
import com.example.finalapp.screens.loadingAndErrorScreen.loadingScreen.EventLoadingScreen
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.EventsViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun XHamsLiveScreenWrapper(
    eventsViewModel: EventsViewModel,
    modifier: Modifier = Modifier,
    initialPage: Int? = 0,
    navController: NavHostController,
    onRetryCalled:()->Unit
) {
    LaunchedEffect(key1 = true){
        eventsViewModel.getAllEvents()
    }
    val pagerState = rememberPagerState(
        initialPage = initialPage ?: 0,
        pageCount = { (eventsViewModel.eventsListResponse.value as? RequestState.Success<List<Event>>)?.data?.size ?: 0 }
    )
    val eventsState by eventsViewModel.eventsListResponse.collectAsState()

    when (eventsState) {
        is RequestState.Loading -> {
            EventLoadingScreen{navController.navigateUp()}
            // DialogLoading(){navController.navigateUp()}
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
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomBar(navController = navController, containerColor = Constants.HOME_BOTTOM_BAR_COLOR, highlightedTextColor = Constants.BOTTOM_BAR_ACTIVE_TEXT_COLOR, inactiveTextColor = Constants.BOTTOM_BAR_INACTIVE_TEXT_COLOR, inactiveIconColor = Constants.BOTTOM_BAR_INACTIVE_ICON_COLOR) }) {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(it)) {
                        VerticalPager(
                            pageSize = PageSize.Fill,
                            state = pagerState,
                            beyondBoundsPageCount = 1,
                            modifier = modifier.weight(1f)
                        ) { page ->
                            val event = eventList[page]
                            PingsDetailsScreen(eventResponse = event)
                        }
                    }
                }
            }else{
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    NoDataFound("No Events Found!", R.drawable.search, content ={ RetryButton(onRetryCalled=onRetryCalled) })
                }
            }
        }
        else -> {}
    }
}
@Composable
fun PingsDetailsScreen(eventResponse: EventResponse) {
    val context = LocalContext.current
    val window = (context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Constants.HOME_STATUS_BAR_COLOR.toArgb()
    window.navigationBarColor = Constants.HOME_NAV_BAR_COLOR.toArgb()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f)
                    .background(color = Constants.HOME_TOP_BAR_COLOR)
            ) {
                AsyncImage(
                    model = imagePrefix + eventResponse.image,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.TopStart)
                        .padding( 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(
                            Color.White
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 20.dp, end = 16.dp)
                        .align(Alignment.BottomEnd),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.view),
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            text = "300",
                            fontFamily = Constants.ROBOTO_CONDENSED,
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.like),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            text = "200k",
                            fontFamily = Constants.ROBOTO_CONDENSED,
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.comment_outlined),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            text = "300k",
                            fontFamily = Constants.ROBOTO_CONDENSED,
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Text(
                            text = "50k",
                            fontFamily = Constants.ROBOTO_CONDENSED,
                            fontSize = 10.sp,
                            color = Color.LightGray
                        )
                    }

                }

                Column(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.BottomStart),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = imagePrefix + eventResponse.user.profileImage,
                            contentDescription = "",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(shape = CircleShape), contentScale = ContentScale.Crop,
                            filterQuality = FilterQuality.High
                        )

                        Column(
                            modifier = Modifier
                                .shadow(elevation = 10.dp, spotColor = Color.White)
                                .wrapContentSize()
                        ) {
                            Text(
                                text = eventResponse.userName,
                                fontFamily = Constants.USER_NAME_FONT,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                lineHeight = 16.sp
                            )
                            androidx.compose.material3.Text(
                                text = "expiring at : 12 pm.",
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 9.sp,
                                color = Color.LightGray,
                                lineHeight = 12.sp
                            )
                        }
                        FollowButtonVertical(
                            modifier = Modifier

                        ) {

                        }
                    }
                }

            }

            //--------------------------------------------------------------------------------------------//
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                Box(
                    modifier = Modifier
                        .background(Constants.HOME_TOP_BAR_COLOR)
                        .fillMaxHeight()
                        .width(50.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, top = 46.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.create_event),
                                contentDescription = "",
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                "Trails",
                                fontFamily = Constants.ROBOTO_CONDENSED,
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ping),
                                contentDescription = "",
                                modifier = Modifier.size(22.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            Text(
                                "Pings",
                                fontFamily = Constants.ROBOTO_CONDENSED,
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.drop_profile_filled_2),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            Text(
                                "Profiles",
                                fontFamily = Constants.ROBOTO_CONDENSED,
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.direct_chat),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            Text(
                                "Nearby",
                                fontFamily = Constants.ROBOTO_CONDENSED,
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.create_event_new),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            Text(
                                "Create",
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 9.sp,
                                color = Color.LightGray,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.settings_new),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            Text(
                                "Settings",
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 8.sp,
                                color = Color.LightGray,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.menu),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                            // Text("Settings", fontFamily = Constants.FONT_LIGHT, fontSize = 9.sp, color = Color.LightGray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
                        }

                    }

                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = Constants.HOME_TOP_BAR_COLOR)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    eventResponse.title,
                                    fontFamily = Constants.FONT_MEDIUM,
                                    fontSize = 16.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    eventResponse.description ?: "",
                                    fontFamily = Constants.FONT_LIGHT,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                        if (eventResponse.childPosts?.isNotEmpty() == true) {
                            LazyColumn {
                                items(eventResponse.childPosts) { post ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(400.dp)
                                            .padding(vertical = 4.dp, horizontal = 4.dp)
                                    ) {
                                        AsyncImage(
                                            model = imagePrefix + post.image,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .fillMaxSize(), contentScale = ContentScale.Crop,
                                            filterQuality = FilterQuality.High
                                        )
                                    }

                                }

                            }
                        }

                    }

                }
            }
            //---------------------------------------------------------------------------------------//


        }
    }
}