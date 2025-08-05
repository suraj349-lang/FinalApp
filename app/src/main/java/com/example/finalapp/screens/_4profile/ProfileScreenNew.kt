package com.example.finalapp.screens._4profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.model.EventResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.example.finalapp.screens._4profile.myPings.MyPings
import com.example.finalapp.screens._4profile.privateUsername.PasswordForPrivateUsername
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.screens.dialogBox.DropProfileDialog
import com.example.finalapp.screens.dialogBox.uriToFile
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.viewmodels.ImageUploadViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreenNew(navController: NavHostController,authViewModel:AuthViewModel,eventsViewModel:EventsViewModel,imageUploadViewModel:ImageUploadViewModel) {
    val context= LocalContext.current
    var showPasswordDialog by remember {
        mutableStateOf(false)
    }
    val tabs = listOf("Events", "Pings")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val lazyListState = rememberLazyListState()
    val user by UserObject.user.collectAsState()

    var showCustomDialog by remember { mutableStateOf(false) }
    var showSheetForImageUpdate by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val startProfileImageUpload=imageUploadViewModel.startProfileImageUpload.collectAsState()
    val userPingsList by eventsViewModel.userPingsListResponse.collectAsState()
    val userEventsList by eventsViewModel.userEventsListResponse.collectAsState()
    val userDropProfilesList by eventsViewModel.userDropProfilesListResponse.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            var imageFile by mutableStateOf<File?>(null)
            if(imageUri != Uri.EMPTY) imageFile = uriToFile(uri, context )
            imageFile?.let {
                eventsViewModel.uploadImageAndThenCreateEvent(user.user, it){ urlKey->
                    eventsViewModel.updateUserDetails(user.user, urlKey)
                }
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    val imageUploadStatus by imageUploadViewModel.userProfileImageUpdateStatus.collectAsState()
    val lifecycleOwner= LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                Log.i("profileImage", "ProfileScreenNew: onStart called")
//                LaunchedEffect(key1 =startProfileImageUpload.value){
                    //send for upload
                    if(startProfileImageUpload.value) {
                        val uri = imageUploadViewModel.profileImageUri.value
                        var imageFile by mutableStateOf<File?>(null)
                        if(uri != Uri.EMPTY) imageFile = uriToFile(uri, context )
                        Log.i("profileImage", "ProfileScreenNew: called with $imageFile")
                        imageUploadViewModel.uploadImageAndThen(userId =user.user, file = imageFile!!){ url->
                            Log.i("profileImage", "updateUserProfileImage: called with $url")
                            imageUploadViewModel.updateUserProfileImage(user.user,url)
                        }

                        imageUploadViewModel.startProfileImageUpload.value=false
                    }
                //}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val systemUiController = rememberSystemUiController()
    val backgroundColor = Color(0xFF040A36)
    val upperCardColor= Color.Black
    val navColor=Color.DarkGray

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navColor,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = navColor,     // Your desired color
            darkIcons = false        // true = dark icons (for light backgrounds)
        )
    }
    BackHandler(true) {
        navController.navigate(SCREENS.HOME.route)

    }


    if (showCustomDialog) {
        DropProfileDialog(authViewModel ,eventsViewModel , imageUploadViewModel ,navController ) { showCustomDialog = !showCustomDialog }
    }
    if(showPasswordDialog){
        PasswordForPrivateUsername(onDismiss = {showPasswordDialog=false}, onEnterClicked = {navController.navigate(SCREENS.PRIVATE_PROFILE.route)})
    }
    LaunchedEffect(key1 = user.user){
        if(eventsViewModel.canFetchEvents.value && eventsViewModel.canFetchDroppedProfiles.value) {
            eventsViewModel.getUserPings(user.user)
            eventsViewModel.getUserEvents(user.user)
            eventsViewModel.getUserDropProfiles(user.user)
        }
    }
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            eventsViewModel.getUserPings(user.user)
            eventsViewModel.getUserEvents(user.user)
            eventsViewModel.getUserDropProfiles(user.user)
            delay(1500L)
            isRefreshing = false
        }
    }


    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { isRefreshing = true }
    )
    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullRefreshState)
        .background(color = backgroundColor)) {
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp) // for spacing
                .zIndex(1f),
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(), state = lazyListState) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = upperCardColor)
                ) { // 0xFF1B1A1A
                    AsyncImage(
                        model = imagePrefix + user.backgroundImage,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painterResource(id = R.drawable.back),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(30.dp)
                            .clickable { navController.navigate(SCREENS.HOME.route) }
                            .align(Alignment.TopStart),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Row(modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.TopEnd)
                        .clickable {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Add Background image",
                            fontSize = 8.sp,
                            fontFamily = Constants.FONT_MEDIUM,
                            color = Color.White
                        )
                        Image(
                            painterResource(id = R.drawable.edit_new),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(16.dp)
                                .size(30.dp),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(Color.White)
                        )

                    }


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Card(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clickable {
                                            showSheetForImageUpdate =
                                                true;/*showImageCropper = true*/
                                        },
                                    shape = CircleShape,
                                    border = BorderStroke(1.dp, color = Color.LightGray),
                                    elevation = 20.dp
                                ) {
                                    //user dp
                                    when (imageUploadStatus) {
                                        is RequestState.Error -> Toast.makeText(
                                            context,
                                            "Error Uploading image",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        is RequestState.Success -> {
                                            GlideImage(
                                                model = imagePrefix + user.profileImage,
                                                contentDescription = "",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        is RequestState.Loading -> {
                                            CircularProgressIndicator(
                                                color = Color.LightGray,
                                                modifier = Modifier.size(10.dp)
                                            )
                                        }

                                        else -> {
                                            GlideImage(
                                                model = imagePrefix + user.profileImage,
                                                contentDescription = "",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = user.name,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = user.userName,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(30.dp),
                                    backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.2f),
                                    border = BorderStroke(width = 2.dp, color = Color.White),
                                    shape = RoundedCornerShape(30.dp)
                                ) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Edit account",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Card(modifier = Modifier
                                    .clickable { showPasswordDialog = true }
                                    .fillMaxWidth(1f)
                                    .height(30.dp),
                                    backgroundColor = Color.White.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(30.dp)) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Private profile",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                }

                            }


                        }

                    }

                }
            }
            item {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    //--------------------------------------------------------------------------------

                    when (val response = userDropProfilesList) {
                        is RequestState.Loading -> CircularProgressIndicator()
                        is RequestState.Error -> CommonErrorScreen(error = "Error getting events!") {
                            eventsViewModel.getUserDropProfiles(user.user)
                        }

                        is RequestState.Success -> {
                            RecentDrops(
                                response.data.data,
                                onDropProfileClicked = { showCustomDialog = !showCustomDialog },
                                onItemClicked = {
                                    val route = SCREENS.DROP_PROFILE_USER_PROFILE.createRoute(it)
                                    navController.navigate(route)
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
                    //--------------------------------------------------------------------------------
                item {
                    Column(modifier = Modifier) {
                        TabRow(
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                    color = Constants.TAB_ROW_INDICATOR_COLOR,
                                    height = 2.dp
                                )
                            },
                            selectedTabIndex = selectedTabIndex,
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.Black,
                            contentColor = Color.White
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = { Text(title, fontFamily = Constants.FONT_LIGHT, fontSize = 14.sp) }
                                )
                            }
                        }

                        when (selectedTabIndex) {
                            0 -> {
                                when (val response = userEventsList) {
                                    is RequestState.Loading -> CircularProgressIndicator()
                                    is RequestState.Error ->
                                        CommonErrorScreen(
                                            error = "Error getting events!",
                                            onRetryClicked = { eventsViewModel.getUserEvents(user.user) }
                                        )

                                    is RequestState.Success -> {
                                        MyEvents(response.data) {
                                            navController.navigate(SCREENS.CREATE_EVENT.route)
                                        }
                                    }

                                    else -> {}
                                }
                            }

                            1 -> {
                                when (val response = userPingsList) {
                                    is RequestState.Loading -> CircularProgressIndicator()
                                    is RequestState.Error -> CommonErrorScreen(error = "Error getting pings!") {
                                        eventsViewModel.getUserPings(user.user)
                                    }

                                    is RequestState.Success -> {
                                        MyPings(response.data) {
                                            navController.navigate(SCREENS.CREATE_PING.route)
                                        }
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                }
                    //----------------------------------------------------------------------------------------
            item {


                //UserStatsScreen(12,12,34)
                LogoutUser(
                    onLogoutClicked = {
                        authViewModel.logout {
                            navController.navigate(SCREENS.LOGIN.route) {
                                popUpTo(0) {
                                    inclusive = true
                                } // This clears the entire back stack
                                launchSingleTop = true
                            }
                        }
                    })
            }
        }
    }
    CreateEventOrPingBottomSheet(
        showSheet = showSheet,
        onDismiss = {showSheet=false },
        navHostController = navController
    )
    ImageUpdateBottomSheet(
        showSheet = showSheetForImageUpdate,
        onDismiss = {showSheetForImageUpdate=false },
        currentProfileImage=user.profileImage,
        navHostController = navController,
        onChangeImageClicked={ navController.navigate("camerax/profile");/*showImageCropper=true*/}
    )
}




@Composable
fun MyEvents(items: List<EventResponse>, onAddEventClicked:()->Unit) {
    val eventsList=remember{ items}
    Column() {
        LazyRow{
            items(eventsList){item->
                MyEventItem(item)

            }
        }
        Card(modifier = Modifier
            .clickable { onAddEventClicked() }
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(40.dp),
            backgroundColor = Color(0xFF121212),
            border = BorderStroke(width = 1.dp, brush = Brush.linearGradient(colors = listOf(Color(0xFFF7B206), Color(0xFF540575)))))
        {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painterResource(id = R.drawable.add),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color(0xFF033666)),
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = "Create new Event",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.White
                )

            }
            
        }
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyEventItem(item: EventResponse) {
    Box(modifier = Modifier
        .size(180.dp) //120 earlier
        .padding(end = 8.dp, top = 8.dp)
        .clip(shape = RoundedCornerShape(6.dp))) {
        GlideImage(model=  imagePrefix+item.image/*R.drawable.profile_image_1*/ , contentDescription = "", contentScale = ContentScale.Crop) //todo add imagePrefix when upload is happening
//        Row(modifier = Modifier
//            .align(Alignment.BottomStart)
//            .padding(4.dp)
//            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            item.title?.let { Text(text = it, color = Color.White, fontWeight = FontWeight.SemiBold) }
//            Card(shape = CircleShape,backgroundColor = Color.Black.copy(alpha = 0.4f)) {
//                Text(text=item.expirationTime, color = Color.White, modifier = Modifier.padding(2.dp))
//            }
//
//
//        }


        
    }
}


@Composable
fun RecentDrops(
    userDropProfilesList: List<DropProfileResponse>,
    onDropProfileClicked: () -> Unit,
    onItemClicked: (DropProfileResponse) -> Unit
) {
    val droppedProfilesList=remember{userDropProfilesList}
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recent Profile Drops",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize=18.sp,
                    fontFamily = Constants.FONT_MEDIUM
                )
                Image(
                    painter = painterResource(id = R.drawable.showall),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(
                        Color(
                            0xFF041372
                        )
                    ),
                    modifier = Modifier.size(20.dp)
                )
            }
            Card(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), elevation = 40.dp, backgroundColor = Color(0xFF040A36)
            ) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    LazyRow(modifier = Modifier.padding(vertical = 8.dp).padding(start = 8.dp)) {
                        items(droppedProfilesList) { item ->
                            RecentProfileDropItem(item) { onItemClicked(item) }
                        }
                    }

                        Row(
                            modifier = Modifier
                                .clickable { onDropProfileClicked() }
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Image(
                                painterResource(id = R.drawable.drop_profile_filled_rounded),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(Color(0xFFE1E9F1)),
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Text(
                                text = "Drop your profile",
                                modifier = Modifier,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Constants.FONT_MEDIUM,
                                color = Color.White
                            )
                    }
                }
            }

        }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecentProfileDropItem(item: DropProfileResponse,onItemClicked:(DropProfileResponse)->Unit) {
        Box(
            modifier = Modifier
                .clickable { onItemClicked(item) }
                .width(100.dp)
                .height(140.dp)
                .padding(end = 8.dp)
                .clip(shape = RoundedCornerShape(6.dp))
        ) {
            Column(modifier = Modifier.background(brush = Brush.verticalGradient(colors = listOf(Color(
                0xFF4D056B
            ),
                Color(0xFF9E0642)
            )))) {
//                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp),backgroundColor = Color(
//                    0xFFF1EDE3
//                )
//                ) {
//                    Text(text = item.location.take(10), fontWeight = FontWeight.SemiBold,color = Color(0xFF072747),
//                        modifier = Modifier.padding(start=4.dp,top=2.dp), fontSize = 8.sp)
//                }
                GlideImage(
                    model= imagePrefix+item.image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

        }

    }
}

@Composable
fun UserStats() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Your Stats", fontWeight = FontWeight.Bold,color= Color.Black, fontSize = 16.sp)
            
        }
        
    }
}

@Preview(showBackground = true)
@Composable
fun LogoutUser(onLogoutClicked:()->Unit={}) {
    Column(modifier = Modifier
        .wrapContentSize()
        .padding(bottom = 20.dp)) {
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
        OutlinedButton(onClick = { onLogoutClicked()  }, modifier = Modifier
            .wrapContentWidth()
            .height(40.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(width = 1.dp, color = Color(
            0xFF990707
        )
        )) { //0xFF45065F
            Row(
                modifier = Modifier.wrapContentWidth() ,horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    fontFamily = Constants.FONT_MEDIUM,
                    modifier = Modifier,
                    color = Color.White
                )
            }
        }
    }

}