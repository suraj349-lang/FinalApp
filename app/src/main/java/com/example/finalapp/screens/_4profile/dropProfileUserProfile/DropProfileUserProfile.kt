package com.example.finalapp.screens._4profile.dropProfileUserProfile

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
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.DONGLE_LIGHT
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL
// when the dropped profile is clicked then it is shown
@Composable
fun DropProfileUserProfile(navController: NavHostController,dropProfileResponse: DropProfileResponse?) {
    val buttonsVisible = remember { mutableStateOf(false) }
    if(dropProfileResponse!=null) {
        Scaffold(
            bottomBar = {BottomBar(navController = navController, state = buttonsVisible)},
            content = {
            DropProfileUserProfileUI(
                paddingValues = it,
                dropProfileResponse=dropProfileResponse,
                onSendMessageClicked = {
                 navController.navigate(SCREENS.SINGLE_CHAT.createPath(dropProfileResponse.createdBy.username,dropProfileResponse.createdBy._id))
                },
                onBackPressed = {navController.navigateUp()}
            )
        })

    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DropProfileUserProfileUI(paddingValues: PaddingValues, dropProfileResponse: DropProfileResponse?,onSendMessageClicked:()->Unit,onBackPressed:()->Unit) {
    if (dropProfileResponse!=null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)) {
                        GlideImage(model =  imagePrefix+dropProfileResponse.image,
                            contentDescription ="" , contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        Text(text = dropProfileResponse.createdBy.name.uppercase(), fontFamily = DONGLE_NORMAL, fontWeight = FontWeight.SemiBold, fontSize = 35.sp, color = Color.White, modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp))
                        Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier
                            .padding(end = 30.dp, bottom = 16.dp)
                            .align(Alignment.BottomEnd)
                            .size(20.dp), colorFilter = ColorFilter.tint(Color.White) )
                        Card(modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.TopStart)
                            .padding(8.dp), shape = CircleShape, elevation = 20.dp, backgroundColor = Color.White.copy(alpha = 0.4f)) {
                            Image(painter = painterResource(id = R.drawable.back), contentDescription ="", modifier = Modifier
                                .clickable { onBackPressed() }
                                .padding(4.dp)
                                .size(30.dp), colorFilter = ColorFilter.tint(Color.White)
                            )

                        }

                    }

                }

                Row(modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Row(modifier = Modifier.wrapContentHeight().fillMaxWidth(0.6f)) {
                        Card(modifier = Modifier.wrapContentSize(), shape = CircleShape) {
                            GlideImage(model = dropProfileResponse.createdBy.profileImage, contentDescription ="", modifier = Modifier.size(40.dp), contentScale = ContentScale.Crop )
                        }
                        Text(
                            text = dropProfileResponse.createdBy.username.lowercase(),
                            fontFamily = DONGLE_NORMAL,
                            fontSize = 30.sp
                        )
                    }

                    Button(onClick = { onSendMessageClicked() },
                           colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96053E))
                    ) {
                        Text(text = "Send Message", color = Color.White, fontFamily = DONGLE_LIGHT, fontSize = 20.sp)
                    }


                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.calendar), contentDescription ="", modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp), colorFilter = ColorFilter.tint(floatingActionBtnColor) )
                    Text(text = dropProfileResponse.createdAt.toString(), color = Color.Black, fontFamily = DONGLE_NORMAL, fontSize = 25.sp)

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
                        text = dropProfileResponse.location,
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

@Composable
fun UserTopBar(title:String) {
    TopAppBar(
        title = { Text(text = title.uppercase(), fontFamily = DONGLE_BOLD, fontSize = 35.sp, color = Color.White)},
        backgroundColor = floatingActionBtnColor,
        actions = {
            Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier
                .padding(end = 16.dp)
                .size(20.dp), colorFilter = ColorFilter.tint(
                Color.White) )
        }

    )
}