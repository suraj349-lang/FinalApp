package com.example.finalapp.screens._4profile


import BottomBar
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.finalapp.R
import com.example.finalapp.model.User
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants.DONGLE_LIGHT
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL
import com.example.finalapp.viewmodels.EventsViewModel
import androidx.compose.runtime.getValue
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants

// when the dropped profile is clicked then it is shown
@Composable
fun UserPublicProfile(eventsViewModel: EventsViewModel,navController:NavHostController,userId: String?) {
    val buttonsVisible = remember { mutableStateOf(false) }
    val user by  eventsViewModel.userProfileResponse.collectAsState()
    LaunchedEffect(key1 = Unit ){
      eventsViewModel.getUserData(userId!!)
    }
    when(val response=user){
        is RequestState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is RequestState.Success -> {
            Scaffold(
                bottomBar = {BottomBar(navController = navController, state = buttonsVisible)},
                content = {
                    UserPublicProfileUI(
                        paddingValues = it,
                        user = response.data,
                        onSendMessageClicked = {
                                               navController.navigate(SCREENS.SINGLE_CHAT.createPath(response.data.username,response.data.userId))
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
fun UserPublicProfileUI(paddingValues: PaddingValues, user: User?,onSendMessageClicked:()->Unit,onBackPressed:()->Unit) {
    if (user!=null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(modifier = Modifier.padding( top= paddingValues.calculateTopPadding())
                        .fillMaxWidth()
                        .height(400.dp)) {
                        GlideImage(model =  imagePrefix+user.profileImage,
                            contentDescription ="" , contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        Card(modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.TopStart)
                            .padding(8.dp), shape = CircleShape, elevation = 20.dp, backgroundColor = Color.White.copy(alpha = 0.8f)) {
                            Image(painter = painterResource(id = R.drawable.back), contentDescription ="", modifier = Modifier
                                .clickable { onBackPressed() }
                                .padding(4.dp)
                                .size(30.dp), colorFilter = ColorFilter.tint(Color.Black)
                            )

                        }

                    }
                Row(modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                            text = user.name,
                            fontFamily = Constants.FONT_MEDIUM,
                            fontSize = 24.sp
                    )
                    Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier
                        .size(20.dp), colorFilter = ColorFilter.tint(Color.DarkGray)
                    )

                    Button(onClick = { onSendMessageClicked() }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96053E))) {
                        Text(text = "Send Message", color = Color.White, fontFamily = DONGLE_LIGHT, fontSize = 20.sp)
                    }


                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.drop_profile_filled_rounded), contentDescription ="", modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp), colorFilter = ColorFilter.tint(floatingActionBtnColor) )
                    Text(
                        text = user.address,
                        maxLines = 3, color = Color.Black,
                        fontFamily = DONGLE_NORMAL,
                        fontSize = 20.sp,
                        lineHeight = 12.sp
                    )

                }
                Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)

            }

        }
    }

}