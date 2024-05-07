package com.example.finalapp.screens.profile

import BottomBar
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.model.User
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.DialogBOX.DialogBoxForCameraAndGallery
import com.example.finalapp.screens.DialogBOX.DialogBoxForImageEdit
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.example.finalapp.utils.Constants.Constants
import com.example.finalapp.utils.Constants.Constants.TAG

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenUI(
    navController: NavHostController = NavHostController(LocalContext.current),
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val buttonsVisible = remember { mutableStateOf(false) }
    val width= LocalConfiguration.current.screenWidthDp
    val context= LocalContext.current
    val dataStore=StoreUserData(context)
    var name by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true){
        name= dataStore.getUserNumber.toString()
    }

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ProfileTopBar(
                navIcon = R.drawable.arrow_back,
                actIcon = R.drawable.arrow_back,
                showActIcon = false,
                onNavIconClick = { navController.popBackStack() },
                onActIconClick = { navController.popBackStack() },
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            )
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PersonalInfo(profileImage = "",profileViewModel,navController)
                FlashInfo()
                EditProfile(navController, profileViewModel)
                //ProfileImages()
                ProfileBio(bio="")
                LogOut(navController, authViewModel )
            }
        }
    }
}


@Composable
fun AllProfiles(profileViewModel: ProfileViewModel) {
    val profileViewModel= hiltViewModel<ProfileViewModel>()
    val user by remember{ mutableStateOf(profileViewModel.usersList.value) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(user){ user->
            UsersItem(user)
        }
    }
}

@Composable
fun UsersItem(user: User) {
    Card(modifier = Modifier.fillMaxSize()) {
        Text(text = user.name)
    }
}


@Composable
fun EditProfile(navController: NavHostController, profileViewModel: ProfileViewModel) {
    var key by remember {
        mutableStateOf(false)
    }
    if(key){
        ImageCaptureFromCamera(profileViewModel)

    }
    Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start, modifier = Modifier
        .height(400.dp)
        .fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)) {
            Button(onClick = {navController.navigate(SCREENS.GALLERY.route)}, modifier = Modifier.padding(start = 30.dp)) {
                Text(text = "Open Gallery")
            }
            Button(onClick = {key = !key}, modifier = Modifier.padding(start = 30.dp)) {
                Text(text = "Open camera ")
            }
        }
        Image(painter = rememberImagePainter(data =profileViewModel.imageUri.value ), modifier = Modifier.fillMaxSize(), contentDescription ="", contentScale = ContentScale.Crop )

    }
}



@Composable
fun ProfileImages() {
    val width= LocalConfiguration.current.screenWidthDp.dp
    var showCustomDialog by remember { mutableStateOf(false) }
    var key by remember { mutableStateOf(-1) }
    var image:Int by remember { mutableStateOf(R.drawable.profile_image_1) }
    if(key==1) image=R.drawable.profile_image_1
    if(key==2) image=R.drawable.profile_image_2
    if(key==3) image=R.drawable.profile_image_3
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(4.dp)) {
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="" , contentScale = ContentScale.Crop, modifier = Modifier.clickable {
                key=1;
                showCustomDialog=!showCustomDialog
                key=-1;
            })

        }
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(
                painter = painterResource(id = R.drawable.profile_image_2),
                contentDescription ="" , contentScale = ContentScale.Crop,
                modifier = Modifier.clickable {
                    key=2;
                    showCustomDialog = !showCustomDialog
                    key=-1;
                })
        }
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(painter = painterResource(id = R.drawable.profile_image_3), contentDescription ="" , contentScale = ContentScale.Crop, modifier = Modifier.clickable {
                key=3;
                showCustomDialog=!showCustomDialog;
                key=-1;
            })

        }
    }


    if(showCustomDialog) DialogBoxForImageEdit(image ) { showCustomDialog=!showCustomDialog }


}

var str="About Me:\n" + "Hey there! I'm Jiya, a 24-year-old actor who's passionate about movies and acting." +
        " By day, you'll find me 15 july, but by night, I'm sleepy bug."

@Composable
fun ProfileBio(bio:String) {
    Card(
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "BIO:", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 24.sp)
            Text(text =if(bio.isNotEmpty()) bio else str, maxLines = 10, overflow = TextOverflow.Ellipsis, color = Color.Black)

        }

    }
}

@Composable
fun LogOut(navController:NavHostController,authViewModel:AuthViewModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(45.dp)) {
            Text(text = "Log Out", color = Color.Black, style = MaterialTheme.typography.displayMedium, fontSize = 28.sp, modifier = Modifier
                .padding(start = 16.dp)
                .clickable {
                    authViewModel.LogoutUser();
                    navController.navigate(SCREENS.LOGIN.route);
                })
    }
}
@Composable
fun PersonalInfo(profileImage:String,profileViewModel:ProfileViewModel,navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(130.dp)
            , shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfileIcon(profileImage = profileImage, profileViewModel , navController )
            ProfileName(name = "Suraj")
        }
    }
}

@Composable
fun FlashInfo() {
    val width= LocalConfiguration.current.screenWidthDp
    Row(modifier = Modifier.padding(start=16.dp,end=16.dp,top=4.dp)) {
        Card(modifier = Modifier
            .padding(end = 4.dp)
            .width((width / 3).dp - 16.dp)
            .height(80.dp), shape = RoundedCornerShape(12.dp))
        {
            Column(modifier=Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.thumbsup),
                    contentDescription = "", modifier = Modifier.size(40.dp)
                )
                Text(text = "12", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)

            }

        }
        Card(modifier = Modifier
            .padding(end = 4.dp)
            .width((width / 3).dp - 16.dp)
            .height(80.dp), shape = RoundedCornerShape(12.dp))
        {
            Column(modifier=Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.raise_offer),
                    contentDescription = "", modifier = Modifier.size(40.dp)
                )
                Text(text = "12", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)

            }

        }
        Card(modifier = Modifier
            .width((width / 3).dp - 16.dp)
            .height(80.dp), shape = RoundedCornerShape(12.dp))
        {
            Column(modifier=Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.requests),
                    contentDescription = "", modifier = Modifier.size(40.dp)
                )
                Text(text = "12", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)

            }

        }
    }
}

@Composable
fun ProfileName(name:String) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            Text(
                text =if(name!="") name else "Flash user",
                style = MaterialTheme.typography.displayMedium,
                fontSize = 30.sp,
                color = Color.Black)
        }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileIcon(profileImage:String,profileViewModel: ProfileViewModel,navController: NavHostController) {
    var showCustomDialog by remember {
        mutableStateOf(false)
    }
    val lifecycleOwner= LocalLifecycleOwner.current
    Surface(
        Modifier
            .size(120.dp)
            .padding(8.dp), shape = CircleShape, color = Color.LightGray, shadowElevation = 12.dp) {
        if(showCustomDialog) DialogBoxForCameraAndGallery(profileViewModel , navController ){ showCustomDialog=!showCustomDialog }
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    showCustomDialog=false
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            // When the effect leaves the Composition, remove the observer
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (profileImage.isNotEmpty())
                GlideImage(
                    model = "${Constants.BASE_URL}${profileImage}",
                    contentDescription = "",
                    transition = CrossFade,
                    modifier = Modifier.clip(
                        CircleShape
                    ), contentScale = ContentScale.Crop
                )
            else {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(onClick = { showCustomDialog=true }) {
                        Image(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Text(text = "Upload photo", fontSize = 8.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(navIcon:Int,actIcon:Int,showActIcon:Boolean,onNavIconClick:()-> Unit,onActIconClick:()-> Unit){


    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = statusAndTopAppBarColor
        ),
        title = {
            Text(
                "Profile",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top=8.dp), color = Color(0xFFE8E9E2), style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(
                    id = navIcon
                ),
                tint = Color.White,
                contentDescription ="" ,
                modifier = Modifier
                    .padding(top = 6.dp, start = 6.dp)
                    .size(24.dp)
                    .clickable { onNavIconClick.invoke() })
        }, actions = {
            if(showActIcon){
                Icon(painter = painterResource(id = actIcon),
                    contentDescription = "",
                    tint = Color(0xFFE8E9E2),
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(-40f)
                        .shadow(elevation = 12.dp, shape = CircleShape, spotColor = Color.White)
                        .clickable {
                            onActIconClick.invoke()

                        })
                }

        }
    )
}



