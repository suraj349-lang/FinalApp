package com.spint.app.screens._4profile.dropProfileUserProfile


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.model.User
import com.spint.app.ui.imagePrefix
import com.spint.app.viewmodels.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.spint.app.navigation.SCREENS
import com.spint.app.utils.RequestState
import com.spint.app.utils.UserObject
import com.spint.app.utils.constants.Constants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// when the dropped profile is clicked then it is shown
@Composable
fun UserPublicProfile(homeViewModel: HomeViewModel, navController:NavHostController, userId: String?) {
    val buttonsVisible = remember { mutableStateOf(false) }
    val directChatUser by  homeViewModel.userProfileResponse.collectAsState()
    val user by UserObject.user.collectAsState()
    LaunchedEffect(key1 = Unit ){
      homeViewModel.getUserData(userId!!)
    }
    when(val response=directChatUser){
        is RequestState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is RequestState.Success -> {
            Scaffold(
               // bottomBar = {BottomBar(navController = navController, state = buttonsVisible)},
                content = {
                    UserPublicProfileUI(
                        homeViewModel,
                        navController,
                        paddingValues = it,
                        user = response.data,
                        onSendMessageClicked = {
                            homeViewModel.saveUserToChatList(
                                currentUserId = user.user,
                                otherUserUserId = response.data.user
                            )

                        },
                        onBackPressed = { navController.navigateUp() }
                    )
                }
            )
        }
        is RequestState.Error   -> {
            Box(modifier = Modifier.fillMaxSize()){
                Text("Error Getting User Data",modifier = Modifier.align(Alignment.Center))
            }
        }
        else ->{}
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserPublicProfileUI(homeViewModel: HomeViewModel, navController: NavHostController, paddingValues: PaddingValues, user: User?, onSendMessageClicked:()->Unit, onBackPressed:()->Unit) {
    val saveToChatListSuccess by homeViewModel.saveUserToChatListResponseState.collectAsState()
    val userObject by UserObject.user.collectAsState()
    when(val response=saveToChatListSuccess){
        is RequestState.Error ->  {
            androidx.compose.material3.Text(text = response.error.toString())
        }
        is RequestState.Success ->{
            Log.i("Userr", "DirectChatProfiles: ${response.data.withUserId.name} other user id ${response.data.withUserId._id}")
            val encodedImageUrl = URLEncoder.encode(response.data.withUserId.profileImage, StandardCharsets.UTF_8.toString())
            navController.navigate(
                SCREENS.SINGLE_CHAT.createPath(
                    userName = response.data.withUserId.name,
                    profileImage = encodedImageUrl,
                    chatListUserId = response.data.withUserId._id
                )
            )
            homeViewModel.resetSaveToChatListSuccessToIdle()
        }
        is RequestState.Loading ->{
            CircularProgressIndicator()
        }
        else  ->{}
    }
    if (user!=null) {
        Box(modifier = Modifier.fillMaxSize().background(color=Color.DarkGray)) {
            Column(
                modifier = Modifier.background(color=Color.White)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()){
                    GlideImage(
                        model = imagePrefix + user.profileImage,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.padding(top = 80.dp).align(Alignment.TopCenter).clip(shape = CircleShape)
                            .size(250.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = "Back",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp) // add padding for breathing space
                            .clickable { onBackPressed() }
                            .size(32.dp), // small icon size
                        colorFilter = ColorFilter.tint(Color.Black)
                    )

                    }
                Row(modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth(), horizontalAlignment = Alignment.Start) {
                        Text(
                            text = user.name,
                            fontFamily = Constants.FONT_MEDIUM,
                            fontSize = 22.sp
                        )
                        Text(
                            text = user.userName,
                            fontFamily = Constants.FONT_LIGHT,
                            fontSize = 16.sp
                        )

                    }

                    Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier
                        .size(20.dp), colorFilter = ColorFilter.tint(Color.DarkGray)
                    )

                    Button(onClick = { onSendMessageClicked() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96053E))) {
                        Text(text = "Send Message", color = Color.White, fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp)
                    }


                }
                Divider(Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color.Gray)

            }

        }
    }

}