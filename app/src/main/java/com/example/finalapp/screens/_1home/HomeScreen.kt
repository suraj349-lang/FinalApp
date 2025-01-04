package com.example.finalapp.screens._1home


import BottomBar
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.User
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.testing.TabItem
import com.example.finalapp.screens.dialogBox.DropProfileDialog
import com.example.finalapp.screens.dialogBox.ShowQRDialog
import com.example.finalapp.screens.dialogBox.showDialog
import com.example.finalapp.testing.items
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.statusBarColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.TAG
import com.example.finalapp.utils.RequestState
import kotlinx.coroutines.launch



/*
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun HomeScreenUI(navController: NavHostController, profileViewModel: ProfileViewModel, authViewModel: AuthViewModel) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val buttonsVisible = remember { mutableStateOf(true) }
    val offerViewModel= hiltViewModel<OfferViewModel>()
    val offersList=profileViewModel.offersList.value
    val scope= rememberCoroutineScope()
    val heightInDp = LocalConfiguration.current.screenHeightDp.dp * 0.78f
    var showQR:showDialog by remember { mutableStateOf(showDialog.CLOSE) }
    if (showQR == showDialog.OPEN) {
        ShowQRDialog(
            image = R.drawable.bigqr,
            navController = navController,
            onDismiss = { showQR = showDialog.CLOSE })
    }
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
             topBar = { HomeTopBar(
                 Constants.APP_NAME,
                     navController,
                     true ,
                 true,
                     R.drawable.chat){showQR=showDialog.OPEN}
                      },
             bottomBar = { BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp))
             },
             floatingActionButton = {
                 HomeFloatingActionButton(authViewModel ,offerViewModel ,profileViewModel, navController );
             }
            ) { it->

        val padding = it
        LaunchedEffect(key1 = true) {
                profileViewModel.getAllOffers()
        }
        ModalNavigationDrawer(
            // scrimColor = Color.Yellow,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    drawerContainerColor = Color.Transparent,
                    drawerContentColor = Color.Black) {
                    items.forEachIndexed { index, item ->
                        Spacer(modifier = Modifier.height(10.dp))
                        NavigationDrawerItem(
                            colors = NavigationDrawerItemDefaults
                                .colors(
                                    selectedContainerColor = Color(0xFF035697),
                                    unselectedContainerColor = Color(0xFFFFFFFF).copy(alpha = 0.8f)
                                ),
                            label = {
                                Text(text = item.title, modifier = Modifier
                                )
                            },
                            selected = index == selectedItemIndex,
                            onClick = {
//                                            navController.navigate(item.route)
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Image(painterResource(id  = if(index==selectedItemIndex) item.selectedIcon else item.unselectedIcon), contentDescription = "", modifier = Modifier.size(40.dp))
                            },
//                            badge = {
//                                item.badgeCount?.let {
//                                    Text(text = item.badgeCount.toString())
//                                }
//                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                                .wrapContentSize(),shape = RoundedCornerShape(6.dp),
                        )

                    }
                }
            },
            drawerState = drawerState,
            gesturesEnabled = true // todo remove it to allow right swipe to open the side navigation drawer
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                val tabItems= listOf(
                    TabItem("Vectors"),
                    TabItem("DirectChat"),
                    TabItem("Profiles")
                )
                var selectedTabIndex by remember {
                    mutableStateOf(0)
                }
                val pagerState= rememberPagerState {
                    tabItems.size

                }
                LaunchedEffect(selectedTabIndex){
                     pagerState.animateScrollToPage(selectedTabIndex)

                }
                LaunchedEffect(pagerState.currentPage,pagerState.isScrollInProgress){
                   if(!pagerState.isScrollInProgress)  selectedTabIndex=pagerState.currentPage


                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)) {
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(), thickness = 1.dp, color = Color(0xFFDCD6DD)
                    )
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        backgroundColor = Color(0xFFFFFFFE),
                        contentColor = Color(0xFFEB1809),
                        modifier = Modifier
                            .padding(bottom = 0.dp)
                            .fillMaxWidth()
                            .height(20.dp)) {
                        tabItems.forEachIndexed { index, item ->
                            Tab(selected = index==selectedTabIndex,
                                selectedContentColor = Color(0xFF9CC2E0),
                                onClick = {
                                    selectedTabIndex=index
                                },
                                text = {
                                    if (selectedItemIndex!=index) Text(text = item.title, color = Color(0xFF636368), style = MaterialTheme.typography.titleMedium, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                                    else Text(text = item.title, color = Color(0xFF000000), style = MaterialTheme.typography.titleMedium, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                                
                                
                                }
                            )
                        }

                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxWidth()) {index->
                        Box(modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxSize()) {
                            if(pagerState.currentPage==0){
                                VectorsUI(profileViewModel = profileViewModel,offersList,padding)

                            }else if(pagerState.currentPage==1){
                                DirectChat()
                            }else{
                                GetDroppedProfiles(navController = navController, offerViewModel =offerViewModel )

                            }


                        }

                    }

                }

                
               

            }
        }
    }
}
*/

//TODO the below code has no lagging while moving left or right
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun HomeScreenUI(navController: NavHostController, eventsViewModel: EventsViewModel, authViewModel: AuthViewModel) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val buttonsVisible = remember { mutableStateOf(true) }
    val eventsViewModel = hiltViewModel<EventsViewModel>()
    val offersList = eventsViewModel.offersList.value
    val scope = rememberCoroutineScope()
    val heightInDp = LocalConfiguration.current.screenHeightDp.dp * 0.78f
    var showQR: showDialog by remember { mutableStateOf(showDialog.CLOSE) }

    if (showQR == showDialog.OPEN) {
        ShowQRDialog(
            image = R.drawable.bigqr,
            navController = navController,
            onDismiss = { showQR = showDialog.CLOSE }
        )
    }

    val pagerState = rememberPagerState(0, pageCount = { 3 })
    val tabItems = listOf(
        TabItem("Vectors"),
        TabItem("DirectChat"),
        TabItem("Profiles")
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopBar(
                Constants.APP_NAME,
                navController,
                true,
                true,
                R.drawable.chat
            ) { showQR = showDialog.OPEN }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            )
        },
        floatingActionButton = {
            HomeFloatingActionButton(authViewModel, eventsViewModel, navController)
        }
    ) { padding ->
        LaunchedEffect(key1 = true) {
            eventsViewModel.getAllEvents()
        }

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    drawerContainerColor = Color.Transparent,
                    drawerContentColor = Color.Black
                ) {
                    items.forEachIndexed { index, item ->
                        Spacer(modifier = Modifier.height(10.dp))
                        NavigationDrawerItem(
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFF035697),
                                unselectedContainerColor = Color(0xFFFFFFFF).copy(alpha = 0.8f)
                            ),
                            label = { Text(text = item.title) },
                            selected = index == selectedItemIndex,
                            onClick = {
                                selectedItemIndex = index
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Image(
                                    painterResource(id = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp)
                                )
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                                .wrapContentSize(),
                            shape = RoundedCornerShape(6.dp)
                        )
                    }
                }
            },
            drawerState = drawerState,
            gesturesEnabled = true
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color(0xFFDCD6DD)
                    )
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = Color(0xFFEB1809)
                            )
                        },
                        backgroundColor = Color(0xFFFFFFFE),
                        contentColor = Color(0xFFEB1809),
                        modifier = Modifier
                            .padding(bottom = 0.dp)
                            .fillMaxWidth()
                            .height(20.dp)
                    ) {
                        tabItems.forEachIndexed { index, item ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                                text = {
                                    Text(
                                        text = item.title,
                                        color = if (pagerState.currentPage == index) Color(0xFF000000) else Color(0xFF636368),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 16.sp,
                                        fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxWidth()
                    ) { page ->
                        when (page) {
                            0 -> VectorsUI( eventsViewModel, offersList, padding)
                            1 -> DirectChat()
                            2 -> GetDroppedProfiles(navController = navController, eventsViewModel = eventsViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VectorsUI(
    eventsViewModel: EventsViewModel,
    offersList: List<OfferModel>,
    padding: PaddingValues
) {
    when (val result = eventsViewModel.allOffers.value) {
        is RequestState.Success -> {
            eventsViewModel.offersList.value = result.data
            LazyColumn {
                items(offersList) { offer ->
                    //ImageScreen(user)
                    // PostScreen(Post("", offer))
                    Log.d("offerData", "HomeScreenUI: $offer")
                    VectorsListScreen(offer)
                }
            }

        }

        is RequestState.Error -> {
            HomeError()
            Toast.makeText(LocalContext.current, "${result.error.message}", Toast.LENGTH_SHORT).show()
        }

        RequestState.Loading -> {
            HomeLoading(padding)
        }

        RequestState.Idle -> {
            HomeLoading(padding)
        }


    }
    
}
data class NavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val badgeCount: Int? = null,
    val route:String
)




@Composable
fun HomeError(){
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.oops), contentDescription = "oops")
        Text(text = "Error loading Profiles !", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color(
            0xFFE91E63
        )
        )
        
    }
}

@Composable
fun LoadingIndicator(){
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {
            LinearProgressIndicator(color = Color(0xFFE9EEE3), trackColor = Color(0xFF284705))
           // Image(painter = painterResource(id = R.drawable.loading), contentDescription ="" )
           // CircularProgressIndicator(color = Color(0xFFE9EEE3), trackColor = Color(0xFF284705), strokeWidth = 4.dp, strokeCap = StrokeCap.Square, modifier = Modifier.size(70.dp))


        }


    }

}

@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun HomeLoading(padding: PaddingValues= PaddingValues(65.dp)){
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.78f
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.profileloading),
                contentDescription = "",
                modifier = Modifier
                    .background(brush = ShimmerEffect())
                    .fillMaxHeight()
                    .width(widthInDp * 0.99f)
                , contentScale = ContentScale.FillBounds
            )
        }
}
@Composable
fun ShimmerEffect(showShimmer: Boolean = true, targetValue: Float = 10000f): Brush {
    return if (showShimmer) {
        // Colors for the shimmer effect
        val shimmerColors = listOf(
            Color.Gray.copy(alpha = 0.6f),
            Color.White.copy(alpha = 0.01f),
            Color.Gray.copy(alpha = 0.6f),
        )

        // Start the animation transition
        val transition = rememberInfiniteTransition(label = "")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            ), label = ""
        )

        // Return a linear gradient brush
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        // If shimmer is turned off, return a transparent brush
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}


@Composable
fun HomeFloatingActionButton(authViewModel: AuthViewModel, eventsViewModel: EventsViewModel, navController: NavHostController  ) {
    var showCustomDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showCustomDialog = !showCustomDialog},
        Modifier.size(75.dp),
        shape= CircleShape,
       // containerColor = Color(0xFFAFD7E9), // 0xFFE4E47F  0xFFFFEB3B
        contentColor = Color.Black, //0xFFE4420E
        containerColor = Color(0xFF9980C5)//statusAndTopAppBarColor, //0xFFEBDB55
//        contentColor = floatingActionBtnTextColor,//0xFF090200
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(id = R.drawable.nearby_chat), contentDescription = "Add",
                Modifier
                    .padding(top = 4.dp)
                    .size(50.dp))
            Text(text = "Drop Profile", fontSize = 8.sp, modifier = Modifier.padding(top=0.dp))
        }
    }
    if (showCustomDialog) {
       // CustomAlertDialog(offerViewModel , navController ) { showCustomDialog = !showCustomDialog }
        DropProfileDialog(authViewModel ,eventsViewModel , navController ) { showCustomDialog = !showCustomDialog }
    }
}

@Composable
fun ProfileData() {
    Box(modifier = Modifier
        .padding(6.dp)
        .fillMaxWidth()
        .heightIn(min = 100.dp, max = 300.dp)
        .background(brush = Brush.linearGradient(colors = listOf(Color(0xFFE8E9E2), Color.White))))
    {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "INTERESTS: ", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
            {
               Text(text = "Party" + " " + " Clubbing" + " " + "Dancing", style = MaterialTheme.typography.bodySmall , color = DarkBlue, fontSize = 24.sp, modifier = Modifier.padding(start = 4.dp) )
            }
            Text(text = "D.O.B : ", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
            {
                Text(text = "15-07-1999", style = MaterialTheme.typography.bodySmall , color = DarkBlue, fontSize = 24.sp, modifier = Modifier.padding(start = 4.dp) )
            }

        }


    }

}

@Composable
fun HomeScreenOffer(){
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.35f
    Box(modifier = Modifier
        .padding(6.dp)
        .fillMaxWidth()
        .heightIn(min = 100.dp, max = heightInDp)
        ) {
        Column (verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start){
            Row(modifier= Modifier
                .fillMaxWidth()
                .heightIn(200.dp)
                .background(
                    brush = Brush.linearGradient(colors = listOf(Color(0xFFE8E9E2), Color.White))
                )) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)) {
                    Text(text = "OFFER", style = MaterialTheme.typography.bodyMedium, color = Color(
                        0xFF05000C
                    )
                    )
                    Text(
                        text = "Dedicate me a song and we ll have coffee",
                        modifier = Modifier.padding(start = 4.dp),
                        maxLines = 4,
                        overflow = TextOverflow.Visible,
                        style = MaterialTheme.typography.titleMedium, 
                        color = Color(0xFFE40B54)
                    )
//                    Row(modifier = Modifier
//                        .fillMaxWidth()
//                        .height(24.dp)) {
//
//                        Icon(painter = painterResource(id = R.drawable.food_24), contentDescription ="Insta icon", modifier = Modifier.size(18.dp) )
//                        Spacer(modifier = Modifier.width(2.dp))
//                        Text(text = "Insta Handle :", style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp, modifier = Modifier.padding(top=4.dp))
//                        Spacer(modifier = Modifier.width(2.dp))
//                        Text(text ="suraj__singh94", style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp,color = DarkBlue,modifier = Modifier.padding(top=4.dp))
//
//                    }
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF063608))) {
                        Text(text = "Accept", style = MaterialTheme.typography.bodySmall, color = Color.White)
                        
                    }
                }
            }
        }
    }
}

//0xFFEEF3B9 -> yellow   0xFFE8E9E2  -> grey  

data class Offer(
    val offerImage:String,
    val user: User
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VectorsListScreen(offer:OfferModel){
    val configuration = LocalConfiguration.current
    val heightInDp = configuration.screenHeightDp.dp * 0.78f
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .wrapContentHeight()
        , contentAlignment = Alignment.Center)

    {
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .border(
                width = 0.5.dp, color = Color.DarkGray
            )) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(45.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                Card(modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 8.dp), shape = CircleShape) {

                    GlideImage(
                        model = if(offer.image !="") offer.image else R.drawable.baseline_person_24,
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(text = if(offer.category !="") offer.category else "Miscellaneous ", modifier = Modifier
                    .padding(start = 10.dp), color = Color.Black, fontSize = 20.sp,style = MaterialTheme.typography.titleMedium)

                Text(text = if(offer.expirationTime !="") offer.expirationTime else "Forever ", modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp)
                    , textAlign = TextAlign.End, color = Color.Red, fontSize = 18.sp,style = MaterialTheme.typography.titleMedium)



            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(heightInDp - 80.dp) , contentAlignment = Alignment.BottomCenter) {


                    GlideImage(
                        model = if (offer.image != "") offer.image else R.drawable.baseline_person_24,//
                        contentDescription = "",
                        transition = CrossFade,
                        modifier=Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop

                    )
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
                    Image(painter = painterResource(id = R.drawable.marker), contentDescription ="", modifier = Modifier.size(24.dp) )
                    Text(text = "Delhi", modifier = Modifier.padding(start = 4.dp))

                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 12.dp), verticalArrangement = Arrangement.Bottom) {

                    Text(
                        text = if (offer.offer != "") offer.offer else "",
                        color = Color.Black,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (offer.location != "") offer.location else "",
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black,
                        fontSize = 18.sp,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageScreen(user: User) {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.78f
    val imageUrl=user.profileImage
    Log.d(TAG, "ImageScreen:${user.profileImage}")
   Box(modifier = Modifier
       .padding(start = 2.dp, end = 2.dp, top = 8.dp)
       .fillMaxWidth()
       .height(heightInDp)
       , contentAlignment = Alignment.Center)

   {
       Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
           .fillMaxHeight()
           .fillMaxWidth(0.90f)
           .border(
               width = 0.5.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp)
           )) {
           Row(verticalAlignment = Alignment.CenterVertically) {
               GlideImage(
                   model ="${Constants.BASE_URL}${user.profileImage}" ,
                   contentDescription ="",
                   transition=CrossFade,
                   contentScale = ContentScale.FillBounds,
                   modifier = Modifier
                       .padding(2.dp)
                       .size(30.dp)
                       .clip(CircleShape)
                       .border(1.dp, Color.DarkGray, CircleShape)
               )
//               Image(
//                   painter = rememberGlidePainter(request ="https://localhost:5000/"+ user.profileImage),
//                   contentDescription = "Round Image",
//                   contentScale = ContentScale.FillBounds,
//                   modifier = Modifier
//                       .padding(2.dp)
//                       .size(30.dp)
//                       .clip(CircleShape)
//                       .border(1.dp, Color.DarkGray, CircleShape)
//               )
           Column(
               modifier = Modifier
                   .padding(2.dp)
                   .fillMaxWidth()
                   .wrapContentHeight()
                   .padding(start = 4.dp),
               verticalArrangement=Arrangement.Center,
               horizontalAlignment = Alignment.Start
           ) {
               Text(
                   text = if(user.name!="") user.name else "Suraj",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.fillMaxWidth(),
                   fontSize = 15.sp,
                   color = Color.Black
               )
               Text(
                   text = if(user.address!="") user.address else "Botanical Garden",
                   style = MaterialTheme.typography.bodyMedium,
                   modifier = Modifier.fillMaxWidth(),
                   fontSize = 6.sp,
                   color = Color.Black
               )
           }
           }
           GlideImage(
               model = "${Constants.BASE_URL}${user.profileImage}",
               contentDescription = "",
               transition=CrossFade,
               modifier = Modifier
                   .fillMaxWidth()
                   .height(heightInDp - 80.dp)
               // .clip(shape = RoundedCornerShape(12.dp))
               , contentScale = ContentScale.Crop
           )

           Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
               Row(modifier = Modifier
                   .fillMaxWidth(0.33f)
                   .fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                   Text(text = "Raise offer for: ", textAlign = TextAlign.Start)
               }
               Row(modifier = Modifier
                   .fillMaxWidth(0.7f)
                   .fillMaxHeight(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                   Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top=3.dp)) {
                       Image(painter = painterResource(id = R.drawable.heart), contentDescription = "", modifier = Modifier
                           .padding(end = 8.dp)
                           .size(24.dp))
                       Text(text = "Like", fontSize = 8.sp, fontWeight = FontWeight.Bold)

                   }
                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                       Image(painter = painterResource(id = R.drawable.cinema), contentDescription = "", modifier = Modifier
                           .padding(end = 8.dp)
                           .size(30.dp))
                       Text(text = "Movie", fontSize = 8.sp, fontWeight = FontWeight.Bold)

                   }
                   Column(horizontalAlignment = Alignment.CenterHorizontally) {
                       Image(painter = painterResource(id = R.drawable.coffeecup), contentDescription = "", modifier = Modifier
                           .padding(end = 8.dp)
                           .size(30.dp))
                       Text(text = "Coffee", fontSize = 8.sp, fontWeight = FontWeight.Bold)

                   }



//                   Image(painter = painterResource(id = R.drawable.dinner), contentDescription = "", modifier = Modifier
//                       .padding(end = 8.dp)
//                       .size(32.dp))

               }
               Row(modifier = Modifier
                   .padding(2.dp)
                   .fillMaxWidth(1f)
                   .fillMaxHeight(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                   Column() {
                       Image(painter = painterResource(id = R.drawable.instant), contentDescription ="", modifier = Modifier
                           .size(30.dp))
                       Text(text = "Instant Chat", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                       
                   }

               }


               
           }

       }


   }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Preview(showBackground = true)
fun PostSplitScreen(post: Offer = Offer("",User())){
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.78f
    Box(modifier = Modifier
        .padding(top = 60.dp)
        .fillMaxWidth()
        .height(heightInDp)
        , contentAlignment = Alignment.Center)

    {
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(
                            0xFF5D096B
                        ),
                        Color(0xFF360986)
                    )
                )
            )
            .border(
                width = 0.5.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp)
            )) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(45.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Beauty/random", modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp), textAlign = TextAlign.Start, color = Color.White, fontWeight = FontWeight.SemiBold)


            }
            GlideImage(
                model = R.drawable.profile_image_1,
                contentDescription = "",
                transition=CrossFade,
                modifier = Modifier
                    .fillMaxWidth(.97f)
                    .height(heightInDp - 80.dp)
                // .clip(shape = RoundedCornerShape(12.dp))
                , contentScale = ContentScale.Crop
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(title:String,navController: NavHostController,navIcon:Boolean,actionIcon:Boolean,icon:Int?=null,onQRClicked:()->Unit={}){


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
           // containerColor = statusAndTopAppBarColor
        containerColor = statusBarColor  //  0xF8E2A61A
        ),
        title = {
            Text(
                title,
                fontSize = 20.sp,
                maxLines = 1,
                fontWeight=FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top=8.dp), color = Color(
                    0xFF000000
                ), style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
//            if(navIcon) {
//                Image(
//                    painter = painterResource(
//                        id = R.drawable.boy
//                    ),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .clickable { navController.navigate(SCREENS.PROFILE.route) }
//                        .padding(top = 6.dp)
//                        .size(40.dp)
//                )
//            }
            Image(
                painter = painterResource(
                    id = R.drawable.app_icon
                ),
                colorFilter = ColorFilter.tint(color= Color(0xFF000000)),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(28.dp)
            )
        }, actions = {
            if(actionIcon) {
                Image(painter = painterResource(id = R.drawable.qr),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color= Color(0xFF000000)),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                        .clickable {
                            onQRClicked()
                        })
                Image(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable { navController.navigate(SCREENS.NOTIFICATIONS.route) }
                        .padding(end = 16.dp)
                        .size(32.dp)
                )
                icon?.let { painterResource(id = it) }?.let {
                    Image(painter = it,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(28.dp)
                            .rotate(-40f)
                            .shadow(elevation = 12.dp, shape = CircleShape, spotColor = Color.White)
                            .clickable {
                                navController.navigate(SCREENS.CHAT.route)
                            })
                }
            }

        }
    )
}



@Composable
fun OfferResponseDataAndAction(eventsViewModel: EventsViewModel, navController: NavHostController){
    val context= LocalContext.current
    Log.d("Data received","Into the function")
    when (val result=eventsViewModel.offerResponse.value){
        is RequestState.Success->{
            eventsViewModel.key.value=0;
            Log.d("Suraj",result.data.toString())
            Toast.makeText(context,"${result.data}", Toast.LENGTH_SHORT).show()
            navController.navigate(SCREENS.HOME.route){
                popUpTo(0)
            }

        }
        is RequestState.Error->{
            Log.d("Data received","error final found")
            Toast.makeText(context,"$result", Toast.LENGTH_SHORT).show()
        }
        RequestState.Loading->{
            CircularProgressIndicator(color = Color(0xFF1289BE))
        }
        RequestState.Idle->{

        }

    }


}


