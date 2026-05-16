package com.spint.app.screens._4profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.model.DropProfileResponse
import com.spint.app.model.EventResponse
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._3createEventOrFlashPost.CreateEventOrPingBottomSheet
import com.spint.app.screens._4profile.userPings.MyPings
import com.spint.app.screens._4profile.privateUsername.PasswordForPrivateUsername
import com.spint.app.screens.dialogBox.DropProfileDialog
import com.spint.app.screens.dialogBox.uriToFile
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserObject
import com.spint.app.utils.RequestState
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.viewmodels.HomeViewModel
import com.spint.app.viewmodels.ImageUploadViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.spint.app.screens.dialogBox.ShowDialog
import com.spint.app.screens.dialogBox.ShowQRDialog
import kotlinx.coroutines.delay
import java.io.File

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreenNew(navController: NavHostController, authViewModel:AuthViewModel, homeViewModel:HomeViewModel, imageUploadViewModel:ImageUploadViewModel) {
    val context= LocalContext.current
    var showPasswordDialog by remember {
        mutableStateOf(false)
    }
    var expanded by remember { mutableStateOf(false) }
    val tabs = listOf(Pair("Events",R.drawable.event), Pair("Flash Posts",R.drawable.ping))
    var selectedTabIndex by remember { mutableStateOf(0) }
    val lazyListState = rememberLazyListState()
    val user by UserObject.user.collectAsState()

    var showDropProfileDialog by remember { mutableStateOf(false) }
    var showSheetForImageUpdate by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val startProfileImageUpload=imageUploadViewModel.startProfileImageUpload.collectAsState()
    val userPingsList by homeViewModel.userPingsListResponse.collectAsState()
    val userEventsList by homeViewModel.userEventsListResponse.collectAsState()
    val userDropProfilesList by homeViewModel.userDropProfilesListResponse.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            var imageFile by mutableStateOf<File?>(null)
            if(imageUri != Uri.EMPTY) imageFile = uriToFile(uri, context )
            imageFile?.let {
                homeViewModel.uploadImageAndThenCreateEvent(user.user, it){ urlKey->
                    homeViewModel.updateUserDetails(user.user, urlKey)
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
    val backgroundColor = Constants.HOME_TOP_BAR_COLOR//0xFF040A36 0xFFC7A246 0xFF90941D
    val upperCardColor= Color(0xFF121212)
    val navColor=Color.DarkGray
    var showQR by remember { mutableStateOf(ShowDialog.CLOSE) }
    var dropCount by remember { mutableStateOf(0) }
    var postCount by remember { mutableStateOf(0) }
    var pingCount by remember { mutableStateOf(0) }

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


    if (showDropProfileDialog) {
        DropProfileDialog(authViewModel ,homeViewModel , imageUploadViewModel ,navController ) { showDropProfileDialog = !showDropProfileDialog }
    }
    if(showPasswordDialog){
        PasswordForPrivateUsername(onDismiss = {showPasswordDialog=false}, onEnterClicked = {navController.navigate(SCREENS.PRIVATE_PROFILE.route)})
    }
    LaunchedEffect(key1 = user.user){
        if(homeViewModel.canFetchEvents.value && homeViewModel.canFetchDroppedProfiles.value) {
            homeViewModel.getUserFlashPosts(user.user)
            homeViewModel.getUserEvents(user.user)
            homeViewModel.getUserDropProfiles(user.user)
        }
    }
    var isRefreshing by remember { mutableStateOf(false) }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            homeViewModel.getUserFlashPosts(user.user)
            homeViewModel.getUserEvents(user.user)
            homeViewModel.getUserDropProfiles(user.user)
            delay(1500L)
            isRefreshing = false
        }
    }
    if (showQR == ShowDialog.OPEN) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // optional dim
                .blur(16.dp) // actual blur
        )

        ShowQRDialog(
            navController = navController,
            onDismiss = { showQR = ShowDialog.CLOSE }
        )
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
                        .height(300.dp)
                        .background(color = upperCardColor)
                ) { // 0xFF1B1A1A
//                    AsyncImage(
//                        model = imagePrefix + user.backgroundImage,
//                        contentDescription = "",
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .align(Alignment.Center),
//                        contentScale = ContentScale.Crop
//                    )
                    Image(
                        painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                            .clickable { navController.navigate(SCREENS.HOME.route) }
                            .align(Alignment.TopStart),
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Row(modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                       /* Text(
                            text = "Add Background image",
                            fontSize = 8.sp,
                            modifier=Modifier.clickable {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            fontFamily = Constants.FONT_MEDIUM,
                            color = Color.White
                        )
                        */

                        Image(
                            painterResource(id = R.drawable.profile_colored),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    showPasswordDialog = true
                                }
                                .padding(16.dp)
                                .size(28.dp),
                            contentScale = ContentScale.Crop,
                           // colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f))
                        )
                        Image(
                            painterResource(id = R.drawable.new_qr),
                            contentDescription = "",
                            modifier = Modifier
                                .clickable {
                                    showQR = ShowDialog.OPEN
                                }
                                .padding(16.dp)
                                .size(24.dp),
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.9f))
                        )
                        Box {
                            Image(
                                painterResource(id = R.drawable.settings_new),
                                contentDescription = "Menu",
                                modifier = Modifier
                                    .clickable { expanded = true }
                                    .padding(16.dp)
                                    .size(30.dp),
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8f))
                            )

                            DropdownMenu(
                                expanded = expanded,
                                modifier = Modifier.wrapContentSize(),
                                containerColor = Color.White,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                        "Background",
                                        fontSize = 16.sp,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        color = Color.Black)
                                           },
                                    modifier = Modifier.wrapContentSize(),
                                    leadingIcon = {
                                        Image(
                                            painterResource(id = R.drawable.edit_new),
                                            contentDescription = "settings",
                                            modifier = Modifier.size(20.dp),
                                            contentScale = ContentScale.Crop,
                                            colorFilter = ColorFilter.tint(Color.Black)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text(
                                        "Settings",
                                        fontSize = 16.sp,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        color = Color.Black
                                    ) },
                                    modifier = Modifier.wrapContentSize(),
                                    leadingIcon = {
                                        Image(
                                            painterResource(id = R.drawable.settings_new),
                                            contentDescription = "settings",
                                            modifier = Modifier.size(20.dp),
                                            contentScale = ContentScale.Crop,
                                            colorFilter = ColorFilter.tint(Color.Black)
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(SCREENS.SETTINGS.route)
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                        "Logout",
                                        fontSize = 16.sp,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        color = Color.Black)
                                           },
                                    modifier = Modifier.wrapContentSize(),
                                    leadingIcon = {
                                        Image(
                                            painterResource(id = R.drawable.logout),
                                            contentDescription = "Logout",
                                            modifier = Modifier
                                                .size(20.dp),
                                            contentScale = ContentScale.Crop,
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        authViewModel.logout {
                                            navController.navigate(SCREENS.LOGIN.route) {
                                                popUpTo(0) {
                                                    inclusive = true
                                                } // This clears the entire back stack
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(
                                        "Delete Account",
                                        fontSize = 16.sp,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        color = Color.Black
                                    ) },
                                    modifier = Modifier.wrapContentSize(),
                                    leadingIcon = {
                                        Image(
                                            painterResource(id = R.drawable.delete),
                                            contentDescription = "Delete account",
                                            modifier = Modifier
                                                .size(20.dp),
                                            contentScale = ContentScale.Crop,
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        homeViewModel.deleteAccount(user.user) {
                                            navController.navigate(SCREENS.SIGNUP.route) {
                                                popUpTo(0) {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
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
                                        .size(80.dp)
                                        .clickable {
                                            showSheetForImageUpdate =
                                                true;/*showImageCropper = true*/
                                        },
                                    shape = CircleShape,
                                   // border = BorderStroke(1.dp, color = Color.LightGray),
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
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            ActivityCountUI(dropCount,postCount,pingCount)
                            Row(
                                modifier = Modifier.padding(top=10.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .clickable { navController.navigate(SCREENS.SETTINGS.route) }
                                        .fillMaxWidth(0.5f)
                                        .height(36.dp),
                                    backgroundColor = Constants.HOME_TOP_BAR_COLOR,
                                   // border = BorderStroke(width = 2.dp, color = Color.White),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Edit account",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontFamily = Constants.FONT_LIGHT
                                        )
                                    }

                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Card(modifier = Modifier
                                    .clickable { showPasswordDialog = true }
                                    .fillMaxWidth(1f)
                                    .height(36.dp),
                                    backgroundColor = Constants.HOME_TOP_BAR_COLOR,
                                    shape = RoundedCornerShape(12.dp)) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Share account",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontFamily = Constants.FONT_LIGHT
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
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Profile Drops:",
                                color = Color.White,
                                fontSize=13.sp,
                                fontFamily = Constants.FONT_LIGHT
                            )
//                            Image(
//                                painter = painterResource(id = R.drawable.showall),
//                                contentDescription = "",
//                                colorFilter = ColorFilter.tint(
//                                    Color(
//                                        0xFF041372
//                                    )
//                                ),
//                                modifier = Modifier.size(20.dp)
//                            )
                        }
//                        Divider(modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 10.dp), thickness = 0.25.dp, color = Color.Gray)

                    //--------------------------------------------------------------------------------

                    when (val response = userDropProfilesList) {
                        is RequestState.Loading -> {
                            LazyRow(Modifier.fillMaxWidth()) {
                                items(3){
                                    Box(modifier = Modifier
                                        .padding(2.dp)
                                        .width(120.dp)
                                        .height(180.dp)
                                        .background(color = Color.DarkGray)){
                                        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                            CircularProgressIndicator(Modifier.size(20.dp), backgroundColor = floatingActionBtnColor)

                                        }

                                    }
                                }

                            }
                        }
                        is RequestState.Error -> {
                            //Toast.makeText(context,"error getting recent drops",Toast.LENGTH_SHORT).show()
                            //Log.e("Error", "ProfileScreenNew: ${response.error.printStackTrace()}",response.error )
                            RecentDrops(
                                emptyList(),
                                onDropProfileClicked = { showDropProfileDialog = !showDropProfileDialog },
                                onItemClicked = {
                                    val route = SCREENS.DROP_PROFILE_USER_PROFILE.createRoute(it)
                                    navController.navigate(route)
                                }
                            )
                        }

//                            NoEventsFoundScreen(backgroundColor,error = "No profiles found!") {
//                            eventsViewModel.getUserDropProfiles(user.user)
//                        }

                        is RequestState.Success -> {
                            dropCount= response.data.data.size
                            RecentDrops(
                                response.data.data,
                                onDropProfileClicked = { showDropProfileDialog = !showDropProfileDialog },
                                onItemClicked = {
                                    val route = SCREENS.DROP_PROFILE_USER_PROFILE.createRoute(it)
                                    navController.navigate(route)
                                }
                            )
                        }

                        else -> {}
                    }
                }}
            }
                    //--------------------------------------------------------------------------------
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .height(800.dp)) {
                        TabRow(
                            indicator = { tabPositions ->
                                val currentTabPosition = tabPositions[selectedTabIndex]

                                Box(
                                    modifier = Modifier
                                        .tabIndicatorOffset(currentTabPosition)
                                        .fillMaxWidth(), // take full tab width
                                    contentAlignment = Alignment.Center
                                ) {
                                    TabRowDefaults.Indicator(
                                        modifier = Modifier.width(80.dp),
                                        color = Color.LightGray.copy(alpha = 1f),
                                        height = 1.dp
                                    )
                                }
                            },
                            selectedTabIndex = selectedTabIndex,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            backgroundColor = Constants.HOME_TOP_BAR_COLOR, //0xFF1C1C1D
                            contentColor = Color.Black
                        ) {
                            tabs.forEachIndexed { index, tabItem ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    modifier = Modifier.height(36.dp),
                                    selectedContentColor = Color.White,
                                    unselectedContentColor = Color.Gray,
                                    text = {
                                        Row(modifier=Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Image(painter=painterResource(tabItem.second),contentDescription = null,modifier=Modifier.size(12.dp))
                                            Text(tabItem.first, fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp) }
                                    }
                                )
                            }
                        }

                        when (selectedTabIndex) {
                            0 -> {
                                when (val response = userEventsList) {
                                    is RequestState.Loading -> {
                                        LazyRow(Modifier.fillMaxWidth()) {
                                            items(3){
                                               Box(modifier = Modifier
                                                   .padding(2.dp)
                                                   .width(120.dp)
                                                   .height(170.dp)
                                                   .background(color = Color.DarkGray)){
                                                   Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                                       CircularProgressIndicator(Modifier.size(20.dp), backgroundColor = floatingActionBtnColor)

                                                   }

                                               }
                                            }

                                        }
                                    }
                                    is RequestState.Error ->
                                        MyPosts(emptyList()) {
                                            navController.navigate(SCREENS.CREATE_EVENT.route)
                                        }
//                                        CommonErrorScreen(
//                                            error = "Error getting events!",
//                                            onRetryClicked = { eventsViewModel.getUserEvents(user.user) }
//                                        )

                                    is RequestState.Success -> {
                                        postCount=response.data.size
                                        Log.i("USER EVENTS", "ProfileScreenNew: ${response.data}")

                                        MyPosts(response.data) {
                                            navController.navigate(SCREENS.CREATE_EVENT.route)
                                        }
                                    }

                                    else -> {}
                                }
                            }

                            1 -> {
                                when (val response = userPingsList) {
                                    is RequestState.Loading ->{
                                        LazyRow(Modifier.fillMaxWidth()) {
                                            items(3){
                                                Box(modifier = Modifier
                                                    .padding(2.dp)
                                                    .width(120.dp)
                                                    .height(170.dp)
                                                    .background(color = Color.DarkGray)){
                                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                                        CircularProgressIndicator(Modifier.size(20.dp), backgroundColor = floatingActionBtnColor)

                                                    }

                                                }
                                            }

                                        }
                                    }
                                    is RequestState.Error -> MyPings(emptyList(),{ navController.navigate(SCREENS.CREATE_PING.route)}) {  }

                                    is RequestState.Success -> {
                                        pingCount=response.data.size
                                        MyPings(response.data, onFlashPostClicked = {navController.navigate(
                                            SCREENS.MY_FLASH_POST_DETAILS_SCREEN.createPath(it))}) {
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
fun MyPosts(
    items: List<EventResponse> = emptyList(),
    onAddEventClicked: () -> Unit
) {
    val eventsList = remember { items }
    Column(modifier = Modifier.fillMaxSize()) {
        if (items.isEmpty()) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color(0xFFEAF3FF) // light blue-ish for events
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.event), // add event icon
                            contentDescription = "",
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "🎉 No Posts Yet!",
                                fontFamily = Constants.FONT_MEDIUM,
                                fontSize = 14.sp,
                                color = Color(0xFF121212)
                            )
                            Text(
                                text = "Create your first Post",
                                fontFamily = Constants.FONT_MEDIUM,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF121212)
                            )
                            Text(
                                text = "Create a live post that nearby users can see. Let others drop their profile, join the conversation with child posts, or even raise a Ping linked to your post.",
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                color = Color.DarkGray,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 8.dp)
                            .clickable { onAddEventClicked() },
                        shape = RoundedCornerShape(50),
                        backgroundColor = Color(0xFF121212)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "➕ Create Event",
                                fontSize = 16.sp,
                                fontFamily = Constants.FONT_LIGHT,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(eventsList) { item ->
                    MyEventItem(item)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // CTA for adding event even when list exists
//            Card(
//                modifier = Modifier
//                    .clickable { onAddEventClicked() }
//                    .padding(horizontal = 16.dp)
//                    .fillMaxWidth()
//                    .height(45.dp),
//                shape = RoundedCornerShape(50),
//                backgroundColor = Color(0xFF121212)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = "➕ Create Event",
//                        fontSize = 14.sp,
//                        fontFamily = Constants.FONT_MEDIUM,
//                        color = Color.White
//                    )
//                }
//            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyEventItem(item: EventResponse) {
    Box(modifier = Modifier
        .aspectRatio(9f / 12f) //120 earlier
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
    val droppedProfilesList = remember { userDropProfilesList }

    if (droppedProfilesList.isEmpty()) {
        Card(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .width(120.dp)
                .height(180.dp), backgroundColor = Color(0xFF343435)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .clickable { onDropProfileClicked() }
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(id = R.drawable.add),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color(0xFFE1E9F1).copy(alpha = 0.8f)),
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        text = "Drop your profile",
                        modifier = Modifier,
                        fontSize = 10.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }

        }
    } else {
        LazyRow(modifier = Modifier) {
            items(droppedProfilesList) { item ->
                RecentProfileDropItem(item) { onItemClicked(item) }
                Spacer(modifier = Modifier.width(4.dp))
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
                .width(120.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(6.dp))
        ) {
            Column(modifier = Modifier.background(brush = Brush.verticalGradient(colors = listOf(Color(0xFF4D056B), Color(0xFF9E0642))))) {
                GlideImage(
                    model= imagePrefix+item.image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
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
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier
            .clickable { onLogoutClicked() }
            .wrapContentWidth()
            .height(40.dp)
        ) { //0xFF45065F
            Row(
                modifier = Modifier.wrapContentWidth() ,horizontalArrangement = Arrangement.Start
            ) {

            }
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp), thickness = 0.5.dp, color = Color.Gray)
    }

}

@Composable
fun ActivityCountUI(dropCount:Int,postCount:Int,pingCount: Int) {
    Row(modifier= Modifier.padding( vertical = 8.dp, horizontal = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        Box(modifier = Modifier.wrapContentSize()) {
            Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    dropCount.toString(),
                    fontSize = 20.sp,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.White
                )
                HorizontalDivider(modifier=Modifier.padding(bottom = 4.dp).width(60.dp), thickness = 0.4.dp, color = Color.DarkGray)
                Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Image(
                        painter = painterResource(R.drawable.drop_profile_filled_2),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(floatingActionBtnColor)
                    )
                    Text(
                        "Drops", fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }


        }
        Box(modifier = Modifier.wrapContentSize()) {
            Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    postCount.toString(),
                    fontSize = 20.sp,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.White
                )
                HorizontalDivider(modifier=Modifier.padding(bottom = 4.dp).width(60.dp), thickness = 0.4.dp, color = Color.DarkGray)
                Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Image(
                        painter = painterResource(R.drawable.event),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                       // colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        "Events", fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }


        }
        Box(modifier = Modifier.wrapContentSize()) {
            Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    pingCount.toString(),
                    fontSize = 20.sp,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.White
                )
                HorizontalDivider(modifier=Modifier.padding(bottom = 4.dp).width(60.dp), thickness = 0.4.dp, color = Color.DarkGray)
                Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Image(
                        painter = painterResource(R.drawable.ping),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                       // colorFilter = ColorFilter.tint(Color.White)
                    )
                    Text(
                        "Posts", fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.White
                    )
                }
            }
        }
    }
}