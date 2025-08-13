package com.example.finalapp.screens._1home.eventWarScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalapp.model.EventResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._1home.DirectChatScreen
import com.example.finalapp.screens.dialogBox.DialogError
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.testing.TabItem
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.RequestState
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PublicEventDetailsScreenWrapper(id:String,navController: NavHostController,eventsViewModel: EventsViewModel,authViewModel: AuthViewModel) {

    val eventDetailsResponse by eventsViewModel.eventDetailsResponse.collectAsState()
    Log.i("EventDetailsResponse", "PublicEventDetailsScreenWrapper: $eventDetailsResponse")

    LaunchedEffect(id ){
        eventsViewModel.getEventDetails(id)
    }

    Scaffold(
        content = {
            when(val response=eventDetailsResponse){
                is RequestState.Success ->{
                    Surface(modifier = Modifier
                        .padding(it)
                        .fillMaxSize()) {
                        PublicEventDetailsScreen(response.data, authViewModel ,eventsViewModel, navController,{ navController.navigateUp() }) {
                            navController.navigate(SCREENS.COMMENT.route)
                        }
                    }
                }
                is RequestState.Error ->{
                    Surface(modifier = Modifier
                        .fillMaxSize()
                        .padding(it), color = Color.Black) {
                        DialogError {
                            navController.navigateUp()
                        }
                    }
                }
                is RequestState.Loading ->{
                    Surface(modifier = Modifier
                        .fillMaxSize()
                        .padding(it), color = Color.Black) {
                        DialogLoading() {
                            navController.navigateUp()
                        }
                    }
                }
                else ->{

                }
            }

        }
    )
}


@Composable
fun PublicEventDetailsScreen(
    response: EventResponse,
    authViewModel: AuthViewModel,
    eventsViewModel: EventsViewModel,
    navController: NavHostController,
    onBackClicked: () -> Unit,
    onCommentClicked: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF24056F))) {
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.TopStart)
                .shadow(elevation = 10.dp, spotColor = Color.White)
                .zIndex(10f)
                .size(30.dp)
                .clickable { onBackClicked() },
            colorFilter = ColorFilter.tint(Color.White)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .shadow(elevation = 30.dp, ambientColor = Color.White, spotColor = Color.White)
                    .zIndex(6f)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    model = imagePrefix + response.image,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .padding(bottom = 2.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .shadow(elevation = 10.dp, spotColor = Color.White)
                        .zIndex(10f),
                    contentScale = ContentScale.FillBounds
                )
            }
            PeopleCommentWar(
                response.upvoted,
                response.downvoted,
                response.totalUpVotes,
                response.totalDownVotes,
                response.totalComments,
                response.totalJoined,
                onUpVoteClicked = {},
                onDownVoteClicked = {},
                onShareClicked = {},
                onCommentClicked = { onCommentClicked() }
            )
            EventTitleAndDescriptionWar(
                eventTitle = response.title,
                eventDescription = response.description
            )

            ThreeOptions(events = response.childPosts ?: listOf(), authViewModel, eventsViewModel , navController )

        }
    }
}



@Composable
fun PeopleCommentWar(
    upvoted:Boolean,
    downvoted:Boolean,
    totalUpVotes:Int,
    totalDownVotes:Int,
    totalComments:Int,
    totalPeopleJoined:Int,
    onUpVoteClicked:()->Unit,
    onDownVoteClicked:()->Unit,
    onShareClicked:()->Unit,
    onPeopleClicked:()->Unit={},
    onCommentClicked:()->Unit={},
    onLiveInteractionClicked:()->Unit={},
    onLiveClicked:()->Unit={},
    onContributeClicked:()->Unit={}
) {
    var upVotedIcon by remember {
        mutableStateOf(upvoted)
    }
    var downVotedIcon by remember {
        mutableStateOf(downvoted)
    }


    Box(modifier = Modifier
        .shadow(elevation = 1.dp, spotColor = Color.White)
        .zIndex(4f)
        .fillMaxWidth()
        .height(50.dp)
        .clip(shape = RoundedCornerShape(2.dp))
        .background(Color(0xFF03061F)))
    {
        Row(
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onUpVoteClicked()
                    upVotedIcon = !upVotedIcon
                    if(downvoted) {
                        downVotedIcon=!downVotedIcon
                    }
                }) {
                Image(
                    painter = painterResource(id = if (upVotedIcon) R.drawable.upvote else R.drawable.upvote_empty),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = totalUpVotes.toString(),
                    fontSize = 10.sp,
                    fontFamily = Constants.FONT_LIGHT,
                    color = Color.White
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onDownVoteClicked();
                    if (upvoted) {
                        upVotedIcon = !upVotedIcon
                    }
                    downVotedIcon = !downVotedIcon
                }
            ) {
                Image(
                    painter = painterResource(id = if (downVotedIcon) R.drawable.downvote_empty else R.drawable.arrow_down),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = totalDownVotes.toString(),
                    fontSize = 10.sp,
                    fontFamily = Constants.FONT_LIGHT,
                    color = Color.White
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    onCommentClicked()
                }) {
                Image(
                    painter = painterResource(id = R.drawable.comment_filled),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = totalComments.toString(),
                    fontSize = 10.sp,
                    fontFamily = Constants.FONT_LIGHT,
                    color = Color.White
                )
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onLiveInteractionClicked() }) {
                Image(painter = painterResource(id = R.drawable.war_room), contentDescription ="", modifier = Modifier.size(16.dp),colorFilter = ColorFilter.tint(Color.White) )
                Text(text= totalPeopleJoined.toString() , fontSize = 10.sp, fontFamily = Constants.FONT_LIGHT, color = Color.White)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onShareClicked() }) {
                Image(painter = painterResource(id = R.drawable.share_event), contentDescription ="", modifier = Modifier.size(16.dp),colorFilter = ColorFilter.tint(Color.White) )
                Text(text= "Share" , fontSize = 10.sp, fontFamily = Constants.FONT_LIGHT, color = Color.White)
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
            Button(onClick = { onContributeClicked() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF062442))) {
                Text(text= "+Contribute" , fontSize = 14.sp, color = Color.White, fontFamily = Constants.FONT_MEDIUM)
            }
//            Column( horizontalAlignment = Alignment.CenterHorizontally) {
//
//            }
        }
    }
}


@Composable
fun EventTitleAndDescriptionWar(eventTitle:String,eventDescription:String?) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(shape = RoundedCornerShape(6.dp))) {
        Column(modifier= Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .wrapContentHeight()
           // .verticalScroll(rememberScrollState())
        ) {
           // Text(text = "Description", color = Color.White, fontSize = 16.sp,fontFamily = Constants.USER_NAME_FONT)
            Text(
                text = eventTitle,
                fontFamily = Constants.FONT_MEDIUM,
                color = Color.White,
                lineHeight=20.sp,
                softWrap = true,
                fontSize = 18.sp
            )
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun ThreeOptions(events:List<EventResponse>,authViewModel:AuthViewModel,eventsViewModel:EventsViewModel,navController:NavHostController) {
    val pagerState = rememberPagerState(0, pageCount = { 3 })
    val scope= rememberCoroutineScope()
    val scrollBehavior =TopAppBarDefaults.enterAlwaysScrollBehavior()
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
        .fillMaxWidth()
        .height(500.dp)
        .pullRefresh(pullRefreshState)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
        ) { page ->
            when (page) {
                0 -> ChildPosts(events =events )
                1 -> DirectChatScreen(scrollBehavior, authViewModel, eventsViewModel, navController)
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
    TabItem("Child posts"),
    TabItem("Nearby"),
    TabItem("General")
)

@Composable
fun ChildPosts(events:List<EventResponse>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .heightIn(100.dp, 800.dp)
        .verticalScroll(rememberScrollState())
    ){
        events.forEach {event->
            Box(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()){
                AsyncImage(
                    model = imagePrefix+event.image,
                    contentDescription ="",
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(800.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE2D0A4),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun NearByPeoplePosts() {

}

@Composable
fun GeneralPosts() {

}