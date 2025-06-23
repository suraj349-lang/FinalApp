package com.example.finalapp.screens._4profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.model.EventResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._3createEventOrPing.CreateEventOrPingBottomSheet
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.screens.dialogBox.DropProfileDialog
import com.example.finalapp.screens.dialogBox.uriToFile
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.PURPLE
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.viewmodels.ImageUploadViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.io.File

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreenNew(navController: NavHostController,authViewModel:AuthViewModel,eventsViewModel:EventsViewModel,imageUploadViewModel:ImageUploadViewModel) {
    val context= LocalContext.current
    var showCustomDialog by remember { mutableStateOf(false) }
    var showSheetForImageUpdate by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val temporaryImage by remember { mutableStateOf(ProfileObject.profile?.profileImage) }
    var imageUri by remember { mutableStateOf(Uri.EMPTY) }
    val startProfileImageUpload=imageUploadViewModel.startProfileImageUpload.collectAsState()
    val userEventsList by eventsViewModel.userEventsListResponse.collectAsState()
    val userDropProfilesList by eventsViewModel.userDropProfilesListResponse.collectAsState()

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imageUri=uri
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
                        imageUploadViewModel.uploadImageAndThen(userId = ProfileObject.profile?.userId!!, file = imageFile!!){url->
                            Log.i("profileImage", "updateUserProfileImage: called with $url")
                            imageUploadViewModel.updateUserProfileImage(ProfileObject.profile?.userId!!,url)
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
    val backgroundColor = Color.Gray
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


    if (showCustomDialog) {
        DropProfileDialog(authViewModel ,eventsViewModel , imageUploadViewModel ,navController ) { showCustomDialog = !showCustomDialog }
    }
    LaunchedEffect(key1 = ProfileObject.profile?.userId!!){
        if(eventsViewModel.canFetchEvents.value && eventsViewModel.canFetchDroppedProfiles.value) {
            eventsViewModel.getUserEvents(ProfileObject.profile?.userId!!)
            eventsViewModel.getUserDropProfiles(ProfileObject.profile?.userId!!)
        }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color.DarkGray)){ // 0xFF1B1A1A
                    Image(
                        painterResource(id = R.drawable.back),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(30.dp)
                            .clickable { navController.navigateUp() }
                            .align(Alignment.TopStart),
                        contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.White)
                    )
                    Row(modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.TopEnd)
                        .clickable {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(text = "Add Background image", fontSize = 8.sp, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
                        Image(
                            painterResource(id = R.drawable.edit_new),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(16.dp)
                                .size(30.dp),
                            contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.White)
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
                                    when(val response=imageUploadStatus){
                                        is RequestState.Error -> Toast.makeText(context,"Error Uploading image",Toast.LENGTH_SHORT).show()
                                        is RequestState.Success->{
                                            GlideImage(
                                                model=  "${imagePrefix}${response.data.profileImage}",
                                                contentDescription = "",
                                                modifier=Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                        is RequestState.Loading ->{
                                            CircularProgressIndicator(color=Color.LightGray, modifier = Modifier.size(10.dp))
                                        }
                                        else ->{
                                            GlideImage(
                                                model=  imagePrefix+temporaryImage,
                                                contentDescription = "",
                                                modifier=Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    ProfileObject.profile?.let {
                                        Text(
                                            text = it.name,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily=Constants.FONT_MEDIUM,
                                            color = Color.White,
                                            fontSize = 18.sp
                                        )
                                    }
                                    ProfileObject.profile?.let {
                                        Text(
                                            text = it.username,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }

                                }

                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()) {
                                Card(modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(30.dp), backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.2f),border = BorderStroke(width = 2.dp, color = Color.White),shape = RoundedCornerShape(30.dp)) {
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "My account", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Card(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .height(30.dp), backgroundColor = Color.White.copy(alpha = 0.5f),shape = RoundedCornerShape(30.dp)) {
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Edit Profile", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                }
                                
                            }


                        }

                    }

            }
            Column(modifier = Modifier.padding(start = 16.dp,end=16.dp,top=10.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                //--------------------------------------------------------------------------------


                when(val response=userEventsList){
                    is RequestState.Loading -> CircularProgressIndicator()
                    is RequestState.Error -> CommonErrorScreen(error = "Error getting pings!")
                    is RequestState.Success -> {
                        MyPings(response.data){
                            showSheet=true
                        }
                    }
                    else ->{}
                }

                //--------------------------------------------------------------------------------------


                when(val response=userDropProfilesList){
                    is RequestState.Loading -> CircularProgressIndicator()
                    is RequestState.Error -> CommonErrorScreen(error = "Error getting events!")
                    is RequestState.Success -> {
                        RecentDrops(response.data.data){
                            showCustomDialog=!showCustomDialog
                        }
                    }
                    else ->{}
                }

                //----------------------------------------------------------------------------------------


                UserStats()
                LogoutUser(
                    onLogoutClicked = {authViewModel.logout {
                    navController.navigate(SCREENS.LOGIN.route){
                        popUpTo(0) { inclusive = true } // This clears the entire back stack
                        launchSingleTop = true
                } }})
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
        currentProfileImage=temporaryImage,
        navHostController = navController,
        onChangeImageClicked={ navController.navigate("camerax/profile");/*showImageCropper=true*/}
    )

//    ImageUpdateDialogBox(
//        showSheet=showSheetForImageUpdate,
//        onDismiss = {showSheetForImageUpdate=false},
//        temporaryImage,
//        onCameraClicked = {cameraDialog=true},
//        onGalleryClicked = {galleryDialog=true}
//    )

//    if (cameraDialog) { ImageCaptureFromCameraForDropProfile({imageFile=it}){imageUri=it} }
//    if(galleryDialog) GalleryPickerForDropProfile(navController = navController,{imageFile=it}) { imageUri = it }
    if(imageUri!=Uri.EMPTY) navController.navigate(SCREENS.IMAGE_CROPPER.route)
}




@Composable
fun MyPings(items: List<EventResponse>, onAddEventClicked:()->Unit) {
    val eventsList=remember{ items}
    Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "My Pings", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = Constants.FONT_MEDIUM)
            }
        LazyRow{
            items(eventsList){item->
                MyPingItem(item)

            }
        }
        Card(modifier = Modifier
            .clickable { onAddEventClicked() }
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(40.dp),
            backgroundColor = Color.Gray,
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
fun MyPingItem(item: EventResponse) {
    Log.i("CALLAPI", "LiveEventItem:$item ")
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
    onDropProfileClicked: () -> Unit
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
                Column {
                    Text(text = "Recent Profile Drops", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = Constants.FONT_MEDIUM)
                    Text(text = "upto 1 week", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 8.sp,fontFamily = Constants.FONT_MEDIUM)

                }
                Image(painter = painterResource(id = R.drawable.showall), contentDescription ="", colorFilter = ColorFilter.tint(Color(
                    0xFFABB5F5
                )
                ) , modifier = Modifier.size(20.dp))
            }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp), color = Color.LightGray)
        LazyRow(modifier = Modifier.padding(top =8.dp)){
            items(droppedProfilesList){item->
                RecentProfileDropItem(item)
            }
        }
        Card(modifier = Modifier
            .clickable { onDropProfileClicked() }
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(40.dp),
            backgroundColor = Color.Gray,
            border = BorderStroke(width = 1.dp, brush = Brush.linearGradient(colors = listOf(
            Color(0xFFF7B206), Color(0xFF540575)
        )))
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Image(painterResource(id = R.drawable.drop_profile_filled_rounded),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color(0xFF0481FD)),
                    modifier = Modifier
                        .size(30.dp)
                        .padding(16.dp))
                Text(text = "Drop your profile", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecentProfileDropItem(item: DropProfileResponse) {
        Box(
            modifier = Modifier
                .size(100.dp)
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

@Composable
fun LogoutUser(onLogoutClicked:()->Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .heightIn(40.dp)
        .background(color = Color.Gray)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
            Text(text = "Logout", fontWeight = FontWeight.SemiBold, fontSize = 16.sp,fontFamily = Constants.FONT_MEDIUM, modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clickable { onLogoutClicked() }, color = Color.White
            )
        }
    }
}