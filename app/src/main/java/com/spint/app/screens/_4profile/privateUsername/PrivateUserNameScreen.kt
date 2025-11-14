package com.spint.app.screens._4profile.privateUsername

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.screens._1home.publicEvent.ActiveButton
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateProfileScreenWrapper(navController: NavHostController) {
    Scaffold(
        //topBar = {PrivateProfileTopBar(){navController.navigateUp()} },
        content = {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .background(floatingActionBtnColor)) {//0xFF1976D2
                    Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                            .size(24.dp)
                            .clickable { navController.navigateUp() },
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                    Row(modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                        .wrapContentWidth()
                        .height(30.dp)
                        , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Image(painter = painterResource(id = R.drawable.privacy),
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { },
                        )
                        dynamicText(text = "Private-SPINT", fontSize = 10)

                    }
                    Image(painter = painterResource(id = R.drawable.settings_new),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(20.dp)
                            .clickable { navController.navigateUp() },
                        colorFilter = ColorFilter.tint(Color.LightGray),
                    )
                    Column(modifier = Modifier
                        .padding(top = 70.dp)
                        .align(Alignment.TopCenter)
                        .wrapContentSize(), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(Modifier.size(120.dp), shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color.Gray)) {
                            Image(painter = painterResource(id = R.drawable.colored_man),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .fillMaxSize()
                                    .clickable { /*TODO*/ }
                            )
                        }

                        Text(text = "@ lion_King", fontSize = 22.sp, color = Color(0xFFDDDDDD), fontFamily = Constants.FONT_MEDIUM)
                        Text(text = "Be careful what you wish for!", fontSize = 12.sp, color = Color(0xFFDDDDDD), fontFamily = Constants.FONT_LIGHT)
                        
                    }
                    Text(text = "account created on : 23 may 2025 ", fontSize = 8.sp, color = Color(0xFFDDDDDD), fontFamily = Constants.FONT_LIGHT, modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 10.dp))


                }

               // Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.2.dp, color = Color.LightGray.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(2.dp))
                PrivateUserNameScreen()
            }
        }
    }
    )
}
val titlesAndDescriptions = listOf(
    Pair("Looking for a workout buddy", "Preferably someone into weight training."),
    Pair("Need someone to visit art gallery with", "Group of 2-3 people max."),
    Pair("Searching for a hiking partner", "Moderate trail, must love nature."),
    Pair("Looking for a co-op gaming partner", "PC or console, flexible timing."),
    Pair("Need someone to grab coffee with", "Casual, any time this week."),
    Pair("Seeking a road trip buddy", "Planning a weekend getaway, 2 people max."),
    Pair("Looking for someone to study with", "University-level subjects, quiet environment."),
    Pair("Need someone to attend a concert with", "Music lover, 1-2 people max."),
    Pair("Searching for a book club member", "Looking for casual readers, 3-4 people max."),
    Pair("Need a dance partner for lessons", "Preferably someone with some experience, 1 person.")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrivateUserNameScreen() {
    val icons = arrayOf(R.drawable.ping, R.drawable.drop_profile_filled_2, R.drawable.chat_new)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val scope = rememberCoroutineScope()

    var selectedIndex by remember { mutableStateOf(pagerState.currentPage) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        androidx.compose.material.TabRow(
            selectedTabIndex = selectedIndex,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    color = Constants.TAB_ROW_INDICATOR_COLOR,
                    height = 0.5.dp
                )
            },
            backgroundColor = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            icons.forEachIndexed { index, item ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    icon = {
                        Box(modifier = Modifier.size(50.dp)){
                            Card(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp), shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color(
                                    0xFF770707
                                )
                                )) {
                                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
                                    Text(text = "12", fontFamily = Constants.USER_NAME_FONT, fontSize = 10.sp,color= Color(
                                        0xFFF8F4ED
                                    ), lineHeight = 2.sp, modifier = Modifier.padding(2.dp))
                                }

                            }
                            Image(painter = painterResource(id = item), contentDescription = "", modifier = Modifier
                                .align(Alignment.Center)
                                .size(24.dp))
                        }

                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize().background(Color(0xFFB2D0EE))  //0xFFAFB42B
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                when(selectedIndex){
                    0 ->{
                        LazyColumn{
                            items(titlesAndDescriptions){
                                PrivatePingRowItem(it.first,it.second)
                            }
                        }
                    }
                    1->{

                    }
                    2->{

                    }else->{}
                }
            }

            }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateProfileTopBar(onBackClicked:()->Unit) {
    TopAppBar(title = {
        Text(
            text = "Private username",
            fontFamily = Constants.ROBOTO_CONDENSED,
            fontSize = 16.sp,
            color = Color.White
        )
    },
        //colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Constants.HOME_TOP_BAR_COLOR),
        navigationIcon = {
            Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBackClicked() },
                colorFilter = ColorFilter.tint(Color.White),
            )
        })
}

@Composable
fun Friends() {
    Row(modifier = Modifier
        .padding(vertical = 16.dp)
        .fillMaxWidth()
        .wrapContentHeight()
        .background(color = Constants.HOME_TOP_BAR_COLOR), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
        repeat(2){

            Card(modifier = Modifier
                .width(100.dp)
                .height(40.dp)) {
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text(text = "Friends")
                }

            }
        }
    }
}

@Composable
fun PrivatePingRowItem(title:String,des:String) {
    val list= arrayOf(R.drawable.view,R.drawable.comment,R.drawable.thumbsup,R.drawable.add_link)
    Box(
        Modifier
            .padding(horizontal = 1.dp).padding(top=1.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFFC2185B), Color(0xFF9B808B) //0xFFAFB42B 0xFF443307
//                    )
//                )
            color = Color(0xFF12011A)
            )) {
        Column(modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = ":: Clubbing/public", fontSize =12.sp, color = Color.White.copy(alpha = 0.7f), fontFamily = Constants.USER_NAME_FONT, lineHeight = 18.sp)
                    Text(text = "393,Sector 27 Gurgaon,Delhi-NCR", fontSize =7.sp, color = Color.LightGray, fontFamily = Constants.FONT_LIGHT
                        , lineHeight = 18.sp)
                }

                ActiveButton()

            }
            Divider(Modifier.fillMaxWidth(), thickness = 0.3.dp, color = floatingActionBtnColor)
            dynamicText(text = title, fontSize = 18, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
            dynamicText(text = des, fontSize = 10, fontFamily = Constants.FONT_LIGHT, color = Color.LightGray)
            Row(modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(40.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                list.forEach {
                    Column(modifier = Modifier.wrapContentSize()) {
                        Image(painter = painterResource(id = it), contentDescription = "", modifier = Modifier.size(18.dp), colorFilter = ColorFilter.tint(Color.Gray))
                        Text(text = "100", fontSize = 8.sp, fontFamily = Constants.FONT_MEDIUM, color = Color.LightGray, lineHeight = 18.sp)
                    }
                }
            }
            Text(text = "posted on: 23 may 2025, 12:30 hrs ", fontSize = 8.sp, color = Color(0xFFDDDDDD), fontFamily = Constants.FONT_LIGHT, modifier = Modifier)

        }

    }
}

@Composable
fun dynamicText(text:String,fontSize:Int=14,fontFamily: FontFamily=Constants.FONT_MEDIUM,color: Color= Color.White,lineHeight:Int=18) {
    Text(text = text, fontSize = fontSize.sp, fontFamily = fontFamily, color = color, lineHeight = lineHeight.sp)
}

