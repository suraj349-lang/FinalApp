package com.example.finalapp.screens._1home.eventWarScreen

import androidx.camera.camera2.internal.compat.workaround.ForceCloseCaptureSession.OnConfigured
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.testing.TabItem
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PublicEventDetailsScreenWrapper(navController: NavHostController= NavHostController(LocalContext.current)) {

    val systemUiController = rememberSystemUiController()
    val navBarColor = topColor
    val backgroundColor= Color(0xFF121212)

    SideEffect {
//        systemUiController.setNavigationBarColor(
//            color = navBarColor,
//            darkIcons = false
//        )
        systemUiController.setStatusBarColor(
            color = navBarColor,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(
                color = floatingActionBtnColor,
                darkIcons = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = true
            )
        }
    }
    Scaffold(
        topBar = { PublicEventDetailsTopBar("Farmer's Protest,India"){
            navController.navigateUp()
        } },
        content = {
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(it), color = Color.Black) {
                PublicEventDetailsScreen()
            }
        }
    )
}



@Composable
fun PublicEventDetailsScreen() {
    Column(modifier = Modifier
        // .verticalScroll(rememberScrollState())
        .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        //background image
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth)
        }
        PeopleCommentWar(onCommentClicked={/*navigate to CommentsScreen()*/})
        EventDescriptionWar(eventDescription = "Farmers protest is one of the biggest protest in the world.")
        ThreeOptions()

    }
}


@Composable
fun PeopleCommentWar(
    onPeopleClicked:()->Unit={},
    onCommentClicked:()->Unit={},
    onLiveInteractionClicked:()->Unit={},
    onLiveClicked:()->Unit={},
    onContributeClicked:()->Unit={}
) {

    Box(modifier = Modifier
        .padding(top = 4.dp, start = 8.dp)
        .fillMaxWidth()
        .height(40.dp)
        .background(Color.Black))
    {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onPeopleClicked() }) {
                Image(painter = painterResource(id = R.drawable.people), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.White) )
                Text(text ="100" , fontSize = 10.sp, fontFamily = Constants.FONT_LIGHT, color = Color.White)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onCommentClicked() }) {
                Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.White) )
                Text(text = "100", fontSize = 10.sp, fontFamily = Constants.FONT_LIGHT, color = Color.White)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onLiveInteractionClicked() }) {
                Image(painter = painterResource(id = R.drawable.war_room), contentDescription ="", modifier = Modifier.size(20.dp),colorFilter = ColorFilter.tint(Color.White) )
                Text(text= "100" , fontSize = 10.sp, fontFamily = Constants.FONT_LIGHT, color = Color.White)
            }

//            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onLiveClicked() }) {
//                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(
//                    0xFF2738A2
//                )
//                )) {
//                  //  Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier.size(20.dp),colorFilter = ColorFilter.tint(Color.White) )
//                    Text(text= "+Live" , fontSize = 18.sp, color = Color.White)
//                }
////                Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier.size(20.dp),colorFilter = ColorFilter.tint(Color.White) )
////
//            }
            Button(onClick = { onContributeClicked() }) {
                Text(text= "+Contribute" , fontSize = 18.sp, color = Color.White, fontFamily = Constants.USER_NAME_FONT)
            }
//            Column( horizontalAlignment = Alignment.CenterHorizontally) {
//
//            }
        }
    }
}


@Composable
fun EventDescriptionWar(eventDescription:String?) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(shape = RoundedCornerShape(6.dp))
        .background(color = Color.Black)) {
        Column(modifier= Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())) {
           // Text(text = "Description", color = Color.White, fontSize = 16.sp,fontFamily = Constants.USER_NAME_FONT)
            if (eventDescription != null) {
                Text(
                    text = eventDescription,
                    fontFamily = Constants.FONT_EXTRA_LIGHT,
                    color = Color.White,
                    lineHeight=20.sp,
                    softWrap = true,
                    fontSize = 14.sp
                )
            }
        }}
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ThreeOptions() {
    val pagerState = rememberPagerState(0, pageCount = { 3 })
    val scope= rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { isRefreshing = true }
    )
    LaunchedEffect(isRefreshing) {
        if (isRefreshing && pagerState.currentPage==0) {
            delay(1000L)
          //  eventsViewModel.getAllEvents()
            delay(500L)
            isRefreshing = false
        }
        else if (isRefreshing && pagerState.currentPage==1) {
            delay(1000L)
            //eventsViewModel.loadDirectChatUsers(0.0,0.0)
            delay(1000L)
            isRefreshing = false
        }
        else if (isRefreshing && pagerState.currentPage==2) {
            delay(1000L)
          //  eventsViewModel.getDefaultDropProfiles("")
            delay(500L)
            isRefreshing = false
        }
    }




//    Card(modifier = Modifier
//        .fillMaxWidth()
//        .height(40.dp), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = Color(0xFFDF400E) ,//0xFFEB1809
                    height = 3.dp
                )
            },
            backgroundColor = Color.Black,//Color(0xFFFFFFFF), //0xFFD5623E orange , 0xFFBCE697 green
            modifier = Modifier
                //   .border(width = 0.dp, color = Color.White)
                .padding(bottom = 0.dp)
                .fillMaxWidth()
                .height(35.dp)
        ) {
            THREE.forEachIndexed { index, item ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    text = {
                        Text(
                            text = item.title,
                            color = if (pagerState.currentPage == index) Color(0xFFFFFFFF) /*Color(0xFFDF400E)*/ else Color.LightGray,
                            fontFamily = Constants.FONT_MEDIUM,//FontFamily(Font(R.font.dongle_light)),
                            fontSize = 12.sp,//20.sp,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    Box(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()
        .pullRefresh(pullRefreshState)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) { page ->
            when (page) {
                0 -> VerifiedPeoplePosts()
                1 -> NearByPeoplePosts()
                2 -> GeneralPosts()
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
        
   // }
}


val THREE = listOf(
    TabItem("Verified"),
    TabItem("Nearby"),
    TabItem("General")
)

@Composable
fun VerifiedPeoplePosts() {

}

@Composable
fun NearByPeoplePosts() {

}

@Composable
fun GeneralPosts() {

}