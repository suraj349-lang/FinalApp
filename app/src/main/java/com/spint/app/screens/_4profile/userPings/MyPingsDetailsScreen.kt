package com.spint.app.screens._4profile.userPings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.spint.app.R
import com.spint.app.model.flashPost.FlashPostDetailsResponse
import com.spint.app.model.flashPost.PingsOnPost
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._1home._1FlashPosts.presentation.view.AddPingOnFlashPost
import com.spint.app.screens._1home._1FlashPosts.presentation.view.CommentRoundUI
import com.spint.app.screens._1home._1FlashPosts.presentation.view.CountdownTimer
import com.spint.app.screens._1home._1FlashPosts.presentation.view.ShareRoundUI
import com.spint.app.screens._1home._1FlashPosts.presentation.view.ViewRoundUI
import com.spint.app.screens._1home.commonUI.sharePingDeepLink
import com.spint.app.screens._1home._1FlashPosts.presentation.util.FlashPostCommentScreen
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.RequestState
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.HomeViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyFlashPostDetailsScreen(navController: NavController,id:String,homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    var showFullImage by remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Color.DarkGray,
            darkIcons = false
        )
    }
    LaunchedEffect(Unit) {
        homeViewModel.getUserFlashPostDetails(id)
    }
    val flashPostResponse by homeViewModel.userFlashPostDetailsResponse.collectAsState()
    when ( val response=flashPostResponse) {
        is RequestState.Success -> {
            MyFlashPostDetailsScreenUI(
                flashPostResponse = response.data,
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        is RequestState.Error -> {
            Toast.makeText(context, "Error getting Details", Toast.LENGTH_LONG).show()
        }

        is RequestState.Loading -> {
            CircularProgressIndicator()
        }

        else -> {}
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyFlashPostDetailsScreenUI(flashPostResponse: FlashPostDetailsResponse?,navController: NavController,homeViewModel: HomeViewModel) {
    val context=LocalContext.current
    if (flashPostResponse != null) {
        Box(modifier = Modifier
            .background(color = Color.DarkGray)
            .fillMaxSize()
            .statusBarsPadding()) {
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    GlideImage(
                        model = imagePrefix + flashPostResponse.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { /*showFullImage = true */ }
                    )

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment=Alignment.Bottom,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        // .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(color = Color.Transparent)
                ) {

                    ViewRoundUI(flashPostResponse.totalViews)
                    CommentRoundUI(flashPostResponse.commentsCount,{})
                    CountdownTimer(flashPostResponse.expirationTime)
                    AddPingOnFlashPost(flashPostResponse.peopleJoined,flashPostResponse.pingCount,{ /*onPingOfFlashPostClicked(flashPostResponse._id,"I am interested")*/})
                    ShareRoundUI(flashPostResponse.totalShared){
                        val deeplink="http://${Constants.APP_NAME}.com/ping/${flashPostResponse._id}"
                        sharePingDeepLink(context,deeplink)
                    }


                }
                Row(modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(R.drawable.location_new), contentDescription = "",modifier= Modifier
                        .padding(end = 6.dp)
                        .size(8.dp))
                    Text(
                        text = flashPostResponse.location,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 8.sp,
                        lineHeight = 12.sp,
                    )
                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.DarkGray)) {
                    Text(
                        text = flashPostResponse.title ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, top = 4.dp),
                        color = Color(0xFFE7E7E7),
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = flashPostResponse.description ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, top = 4.dp, bottom = 4.dp),
                        color = Color(0xFFCAD8E8),
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                UserFlashPostDetailsTabsScreen(flashPostResponse.pings ?: listOf(), homeViewModel ){
                    navController.navigate(SCREENS.USER_PUBLIC_PROFILE.createPath(it))}
            }
//            if(showFullImage){
//                FullScreenImageViewDialogBox(image =flashPostResponse.image ,onCloseClicked={showFullImage=false })
//            }

        }
    }
}


@Composable
fun UserFlashPostDetailsTabsScreen(ping: List<PingsOnPost>,homeViewModel: HomeViewModel,onProfileClicked: (String) -> Unit) {

    val tabs = listOf("Comments", "Responses")
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Gray,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        tabPositions[pagerState.currentPage]
                    ),
                    height = 3.dp,
                    color = floatingActionBtnColor
                )
            }
        ) {

            tabs.forEachIndexed { index, title ->

                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            color = if(pagerState.currentPage ==index) Color.White else Color.Black,
                            fontFamily = Constants.FONT_MEDIUM
                        )
                    }
                )
            }
        }

        // 🔥 Swipeable Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize()
        ) { page ->

            when (page) {
                0 -> FlashPostCommentScreen("",homeViewModel)
                1 -> {RepliesScreen(ping){onProfileClicked(it)}}
            }
        }
    }
}

@Composable
fun RepliesScreen(ping: List<PingsOnPost>,onProfileClicked:(String)-> Unit) {
    Column(modifier=Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        ping.forEach {it->
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = imagePrefix + it.userId.profileImage,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable { onProfileClicked(it.userId.user) }
                            .padding(vertical = 6.dp)
                            .size(50.dp)
                            .clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.wrapContentHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                        Text(it.userId.name, color = Color.DarkGray, fontSize = 10.sp, fontFamily = Constants.FONT_LIGHT)
                        Text(it.message, color = Color.Black, fontSize = 14.sp, fontFamily = Constants.FONT_MEDIUM)
                    }

                }
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(R.drawable.cameranew),contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Duel", fontSize = 12.sp, fontFamily = Constants.FONT_LIGHT, color = Color.Black)
                }

//                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.25.dp, color = Color.LightGray)

            }
        }

    }
}