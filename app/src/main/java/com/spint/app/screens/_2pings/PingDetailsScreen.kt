package com.spint.app.screens._2pings

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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.spint.app.R
import com.spint.app.model.pings.PingResponse
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._1home.commonUI.shareEventDeepLink
import com.spint.app.testing.CommonTopBar
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.formatDateTime



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PingDetailsScreen(navController: NavHostController, pingResponse: PingResponse?) {
          Scaffold(
            topBar = {CommonTopBar(title = "Ping") },
            //  bottomBar = {BottomBar(navController = navController, state = buttonsVisible)},
            content = {
                androidx.compose.material.Surface(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .padding(it)
                        .fillMaxSize()
                ) {
                    PingDetailsScreenUI(
                        pingResponse = pingResponse,
                        onSendMessageClicked = {
                            if (pingResponse?.user != null) {
                                navController.navigate(
                                        SCREENS.SINGLE_CHAT.createPath(
                                            pingResponse.user.userName,
                                            pingResponse.user.user
                                        )
                                )
                            }
                        },
                        onBackPressed = { navController.navigateUp() }
                    )

                }

            })

    }


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PingDetailsScreenUI(pingResponse: PingResponse?, onSendMessageClicked:()->Unit, onBackPressed:()->Unit) {
    val context = LocalContext.current
    if (pingResponse != null) {
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                modifier = Modifier.align(Alignment.TopStart)
                    .clickable {onBackPressed() }
                    .padding(8.dp)
                    .shadow(elevation = 10.dp, spotColor = Color.White).zIndex(4f)
                    .size(30.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Column(
                modifier = Modifier.padding(bottom = 16.dp)
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp)
                    ) {
                        GlideImage(
                            model = imagePrefix + pingResponse.image,
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
                                pingResponse.user?.name?.uppercase()?.let {
                                    Text(
                                        text = it,
                                        fontFamily = Constants.FONT_MEDIUM,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 20.sp,
                                        color = Color.White,
                                        lineHeight = 12.sp,
                                        modifier = Modifier
                                            .shadow(elevation = 10.dp, spotColor = Color.White)
                                            .zIndex(4f)
                                    )
                                }
                            Text(
                                text = pingResponse.location,
                                maxLines = 3,
                                color = Color.White.copy(alpha = 0.95f),
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                            )
                        }

                        Image(painter = painterResource(id = R.drawable.share),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 30.dp, bottom = 26.dp)
                                .align(Alignment.BottomEnd)
                                .clickable { shareEventDeepLink(context, pingResponse.user?.user ?: "") }
                                .size(20.dp),
                            colorFilter = ColorFilter.tint(Color.White))

                    }

                }

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(0.6f), horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(modifier = Modifier.wrapContentSize(), shape = CircleShape) {
                            GlideImage(
                                model = imagePrefix + pingResponse.user?.profileImage,
                                contentDescription = "",
                                modifier = Modifier.size(40.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            pingResponse.user?.userName?.lowercase()?.let {
                                Text(
                                    text = it,
                                    fontFamily = Constants.USER_NAME_FONT,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row() {
                                Text(
                                    text = "dropped: ",
                                    color = Color.Black,
                                    fontFamily = Constants.FONT_EXTRA_LIGHT,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = formatDateTime(pingResponse.expirationTime).lowercase(),
                                    color = floatingActionBtnColor,
                                    fontFamily = Constants.FONT_EXTRA_LIGHT,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                    Button(
                        onClick = { onSendMessageClicked() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF96053E))
                    ) {
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.chat_new),
                                contentDescription = "",
                                modifier = Modifier.size(18.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Text(
                                text = "Chat",
                                color = Color.White,
                                fontFamily = Constants.FONT_MEDIUM,
                                fontSize = 16.sp
                            )
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
                        text = formatDateTime(pingResponse.expirationTime),
                        modifier = Modifier,
                        color = floatingActionBtnColor,
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 14.sp,
                    )

                }

                Text(
                    text = pingResponse.offer.capitalize(),
                    modifier = Modifier
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
}

