package com.spint.app.screens._2Events.events.eventWarScreen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.CardDefaults
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
import com.spint.app.R
import com.spint.app.utils.constants.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.spint.app.model.EventResponse
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._1home._2directChat.DirectChatScreen
import com.spint.app.screens.dialogBox.DialogError
import com.spint.app.screens.dialogBox.DialogLoading
import com.spint.app.testing.TabItem
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.RequestState
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.viewmodels.ChatViewModel
import com.spint.app.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PublicEventDetailsScreenWrapper(id:String, navController: NavHostController, homeViewModel: HomeViewModel, authViewModel: AuthViewModel) {

    val eventDetailsResponse by homeViewModel.eventDetailsResponse.collectAsState()
    Log.i("EventDetailsResponse", "PublicEventDetailsScreenWrapper: $eventDetailsResponse")

    LaunchedEffect(id ){
        homeViewModel.getEventDetails(id)
    }

    Scaffold(
        content = {
            when(val response=eventDetailsResponse){
                is RequestState.Success ->{
                    Surface(modifier = Modifier
                        .padding(it)
                        .fillMaxSize(), color = Color.Black) {
                        PublicEventDetailsScreen(response.data, authViewModel ,homeViewModel, navController,{ navController.navigateUp() }) {
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
fun EventTagsDetailsScreen() {
    Card(modifier = Modifier
        .padding(start = 4.dp)
        .wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
        Text(text = "#SSC protest", fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.SemiBold, color = Color(
            0xFF5466D3
        ), fontSize = 18.sp, modifier = Modifier.padding(4.dp))
    }
}
@Composable
fun PublicEventDetailsScreen(
    response: EventResponse,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    onBackClicked: () -> Unit,
    onCommentClicked: () -> Unit
) {
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF000000))) {
        Column(
            Modifier
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.shadow(elevation = 20.dp, spotColor = Color.White).zIndex(5f).background(Color.Black).fillMaxWidth().padding(vertical = 4.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp))  {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "",
                        modifier = Modifier.padding(start = 8.dp)
                            .shadow(elevation = 2.dp)
                            .zIndex(2f)
                            .size(24.dp)
                            .clickable { onBackClicked() },
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Card(Modifier.size(36.dp).shadow(elevation = 10.dp, spotColor = Color.White), shape = CircleShape, elevation = CardDefaults.cardElevation(20.dp)) {
                        AsyncImage(model = imagePrefix+response.user.profileImage, contentDescription ="", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop )
                    }
                    Text(text = response.user.name, color = Color.White, modifier = Modifier.shadow(elevation = 10.dp, spotColor = Color.White))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    AsyncImage(
                        model = imagePrefix + response.image,
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .heightIn(200.dp, 600.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.align(Alignment.BottomEnd)) {
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
                    }
                }


//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .verticalScroll(rememberScrollState())
//            ) {
            EventTagsDetailsScreen()
            EventTitleAndDescriptionWar(
                    eventTitle = response.title,
                    eventDescription = response.description
            )
            //----------------------------------------------------------------------------------------------------------//
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.8f))

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Nearby users:", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 18.sp, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
                Text(text = "see all", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 13.sp, style = TextStyle(textDecoration = TextDecoration.Underline),fontFamily = Constants.FONT_MEDIUM, color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
            ChildMicroNearByUsersInDetailsScreen(
                users = response.childNearByUsers ?: emptyList(),
                onAddChildPostClicked = { /*TODO*/ },
                onProfileClicked = {//navController.navigate(SCREENS.DROP_PROFILE_USER_PROFILE.createRoute())
                }
            )
            //----------------------------------------------------------------------------------------------------------//
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.8f))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Dropped profiles:", modifier = Modifier, fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
                Text(text = "see all", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 13.sp, style = TextStyle(textDecoration = TextDecoration.Underline),fontFamily = Constants.FONT_MEDIUM, color = Color.White)     }
            ChildMicroDropProfilesInDetailsScreen(
                    dropProfiles = response.childDropProfiles ?: emptyList(),
                    onAddChildPostClicked = { /*TODO*/ },
                    onChildPostClicked ={}
            )

            //----------------------------------------------------------------------------------------------------------//
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.8f))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Child posts:", modifier = Modifier, fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
                Text(text = "see all", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 13.sp, style = TextStyle(textDecoration = TextDecoration.Underline),fontFamily = Constants.FONT_MEDIUM, color = Color.White)            }
            ChildMicroPostsInDetailsScreen(
                    events = response.childPosts ?: emptyList(),
                    onAddChildPostClicked = { /*TODO*/ },
                    onChildPostClicked ={}
            )
            //----------------------------------------------------------------------------------------------------------//
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.8f))
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Event pings:", modifier = Modifier, fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
                Text(text = "see all", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 13.sp, style = TextStyle(textDecoration = TextDecoration.Underline),fontFamily = Constants.FONT_MEDIUM, color = Color.White)           }
            ChildMicroPingsInDetailsScreen(
                    events = response.childPings ?: emptyList(),
                    onAddChildPostClicked = { /*TODO*/ },
                    onChildPostClicked ={}
            )
            //----------------------------------------------------------------------------------------------------------//

//                ThreeOptions(
//                    events = response.childPosts ?: listOf(),
//                    authViewModel,
//                    eventsViewModel,
//                    navController
//                )



           // }
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
        .zIndex(4f)
        .shadow(elevation = 20.dp)
        .width(50.dp)
        .wrapContentHeight()
        .clip(shape = RoundedCornerShape(2.dp))
        .background(Color.Transparent))
    {
        Column(
            modifier = Modifier
                .padding(start = 4.dp)
                .fillMaxSize()
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
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = totalUpVotes.toString(),
                    fontSize = 12.sp,
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
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = totalDownVotes.toString(),
                    fontSize = 12.sp,
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
//            Button(onClick = { onContributeClicked() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF062442))) {
//                Text(text= "+Contribute" , fontSize = 14.sp, color = Color.White, fontFamily = Constants.FONT_MEDIUM)
//            }
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
fun ThreeOptions(events:List<EventResponse>, authViewModel:AuthViewModel, homeViewModel:HomeViewModel, chatViewModel: ChatViewModel, navController:NavHostController) {
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
                1 -> DirectChatScreen(scrollBehavior, authViewModel, homeViewModel, chatViewModel , navController)
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