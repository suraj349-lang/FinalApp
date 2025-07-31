package com.example.finalapp.screens._4profile.dropProfileUserProfile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._1home.commonUI.shareDeepLink
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.formatDateTime

// when the dropped profile is clicked then it is shown
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DropProfileUserProfile(navController: NavHostController,dropProfileResponse: DropProfileResponse?) {
    val buttonsVisible = remember { mutableStateOf(true) }
    if(dropProfileResponse!=null) {
        Scaffold(
          //  bottomBar = {BottomBar(navController = navController, state = buttonsVisible)},
            content = {
                Surface(modifier = Modifier
                    .background(Color.White)
                    .padding(it)
                    .fillMaxSize()){
                    DropProfileUserProfileUI(
                        dropProfileResponse=dropProfileResponse,
                        onSendMessageClicked = {
                            navController.navigate(SCREENS.SINGLE_CHAT.createPath(dropProfileResponse.createdBy.username,dropProfileResponse.createdBy.userId))
                        },
                        onBackPressed = {navController.navigateUp()}
                    )

                }

        })

    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DropProfileUserProfileUI(dropProfileResponse: DropProfileResponse?,onSendMessageClicked:()->Unit,onBackPressed:()->Unit) {
    val context= LocalContext.current
    if (dropProfileResponse!=null) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)) {
                        Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "",
                            modifier = Modifier
                                .shadow(elevation = 10.dp, spotColor = Color.White)
                                .zIndex(4f)
                                .align(Alignment.TopStart)
                                .clickable { onBackPressed() }
                                .padding(4.dp)
                                .size(30.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        GlideImage(
                            model = imagePrefix + dropProfileResponse.image,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 8.dp)
                                .align(Alignment.BottomStart)
                                .fillMaxWidth(0.9f),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = dropProfileResponse.createdBy.name.uppercase(),
                                fontFamily = Constants.FONT_MEDIUM,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                color = Color.White,
                                lineHeight=12.sp,
                                modifier = Modifier
                                    .shadow(elevation = 10.dp, spotColor = Color.White)
                                    .zIndex(4f)
                            )
                            Text(
                                text = dropProfileResponse.location,
                                maxLines = 3,
                                color = Color.White.copy(alpha = 0.95f),
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                            )
                        }

                        Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier
                            .padding(end = 30.dp, bottom = 26.dp)
                            .align(Alignment.BottomEnd)
                            .clickable { shareDeepLink(context, dropProfileResponse.id ?: "") }
                            .size(20.dp), colorFilter = ColorFilter.tint(Color.White) )

                    }

                }

                Row(modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Row(modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(0.6f), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(modifier = Modifier.wrapContentSize(), shape = CircleShape) {
                            GlideImage(model = imagePrefix+dropProfileResponse.createdBy.profileImage, contentDescription ="", modifier = Modifier.size(40.dp), contentScale = ContentScale.Crop )
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                            Text(
                                text = dropProfileResponse.createdBy.username.lowercase(),
                                fontFamily = Constants.USER_NAME_FONT,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row() {
                                Text(
                                    text = "dropped: ",
                                    color = Color.Black,
                                    fontFamily = Constants.FONT_EXTRA_LIGHT,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = formatDateTime(dropProfileResponse.createdAt.toString()).lowercase(),
                                    color = floatingActionBtnColor,
                                    fontFamily = Constants.FONT_EXTRA_LIGHT,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Button(onClick = { onSendMessageClicked() },
                           colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96053E))
                    ) {
                        Row(modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Image(painter = painterResource(id = R.drawable.chat_new), contentDescription ="", modifier = Modifier.size(18.dp), colorFilter = ColorFilter.tint(Color.White) )
                            Text(text = "Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp)
                        }
                    }


                }
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Expiring at: ",
                        color = Color.Black,
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 14.sp
                    )
                    Text(
                        text = formatDateTime(dropProfileResponse.validTill.toString()),
                        modifier= Modifier,
                        color = floatingActionBtnColor,
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 14.sp,
                    )

                }

                Text(
                    text = dropProfileResponse.message.capitalize(),
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp),
                    color = Color(0xFF021A31),
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )


              //  Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)

            }

        }
    }

