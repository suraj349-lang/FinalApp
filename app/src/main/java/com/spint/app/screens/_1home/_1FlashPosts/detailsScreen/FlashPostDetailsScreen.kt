package com.spint.app.screens._1home._1FlashPosts.detailsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.spint.app.R
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._1home._1FlashPosts.detailsScreen.flashPosts.AddPingOnFlashPost
import com.spint.app.screens._1home._1FlashPosts.detailsScreen.flashPosts.CommentRoundUI
import com.spint.app.screens._1home._1FlashPosts.detailsScreen.flashPosts.CountdownTimer
import com.spint.app.screens._1home._1FlashPosts.detailsScreen.flashPosts.ShareRoundUI
import com.spint.app.screens._1home._1FlashPosts.detailsScreen.flashPosts.ViewRoundUI
import com.spint.app.screens._1home.commonUI.sharePingDeepLink
import com.spint.app.screens._2Events.events.eventWarScreen.CommentsScreen
import com.spint.app.testing.CommonTopBar
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun FlashPostDetailsScreen(navController: NavHostController, flashPostResponse: FlashPostResponse?) {
    Scaffold(
        topBar = {
            flashPostResponse?.let {
                CommonTopBar(
                    flashPostResponse = it,
                    onUserProfileClicked = {navController.navigate(SCREENS.USER_PUBLIC_PROFILE.createPath(flashPostResponse.user?.user!!))},
                    onSendMessageClicked = {
                        if (flashPostResponse.user != null) {
                            val encodedImageUrl = URLEncoder.encode(flashPostResponse.user.profileImage, StandardCharsets.UTF_8.toString())
                            navController.navigate(SCREENS.SINGLE_CHAT.createPath(userName = flashPostResponse.user.userName, profileImage = encodedImageUrl, chatListUserId = flashPostResponse.user.user))
                        }
                     },
                    onBackClicked = { navController.navigateUp() }
                )
            }
        },
              content = {
                  Surface(
                      modifier = Modifier
                          .background(Color.Transparent)
                          .padding(it)
                          .fillMaxSize()
                  ) {
                      FlashPostDetailsScreenUI(
                          flashPostResponse = flashPostResponse
                      )

                  }

              })

}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FlashPostDetailsScreenUI(flashPostResponse: FlashPostResponse?) {
    val context = LocalContext.current
    var showFullImage by remember { mutableStateOf(false) }
    val systemUiController = rememberSystemUiController()



    SideEffect {
        systemUiController.setNavigationBarColor(
            color = Color.DarkGray,
            darkIcons = false
        )
    }
    
    if (flashPostResponse != null) {
        Box(modifier = Modifier
            .background(color = Color.DarkGray)
            .fillMaxSize()
            .statusBarsPadding()) {
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        GlideImage(
                            model = imagePrefix + flashPostResponse.image,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { showFullImage = true }
                        )

                    }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment=Alignment.Bottom,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        // .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(color = Color.Transparent)
                ) {

                    ViewRoundUI(flashPostResponse.totalViews)
                    CommentRoundUI(flashPostResponse.commentsCount,{})
                    CountdownTimer(flashPostResponse.expirationTime)
                    AddPingOnFlashPost(flashPostResponse.peopleJoined,flashPostResponse.pingCount,{ /*onPingOfFlashPostClicked(flashPostResponse._id,"I am interested")*/})
                    ShareRoundUI(flashPostResponse.totalShared){
                        val deeplink="http://${Constants.APP_NAME}.com/ping/${flashPostResponse._id}"
                        sharePingDeepLink(context,deeplink)
                    }


                }
                Row(modifier = Modifier.padding(top=8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(R.drawable.location_new), contentDescription = "",modifier= Modifier.padding(end=6.dp).size(8.dp))
                    Text(
                        text = flashPostResponse.location,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 8.sp,
                        lineHeight = 12.sp,
                    )
                }
                Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(color=Color.DarkGray)) {
                    Text(
                        text = flashPostResponse.title ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, top = 4.dp),
                        color = Color(0xFFE7E7E7),
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = flashPostResponse.description ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, top = 4.dp, bottom = 4.dp),
                        color = Color(0xFFCAD8E8),
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                FlashPostDetailsTabsScreen()
            }
            if(showFullImage){
                FullScreenImageViewDialogBox(image =flashPostResponse.image ,onCloseClicked={showFullImage=false })
            }

        }
    }
}

@Composable
fun FlashPostDetailsTabsScreen() {

    val tabs = listOf("Comments", "Pings", "Related")
    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(
            modifier = Modifier.height(40.dp).fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Gray,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        tabPositions[pagerState.currentPage]
                    ),
                    height = 3.dp,
                    color = floatingActionBtnColor
                )
            }
        ) {

            tabs.forEachIndexed { index, title ->

                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontFamily = Constants.FONT_MEDIUM
                        )
                    }
                )
            }
        }

        // 🔥 Swipeable Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(top=8.dp).fillMaxSize()
        ) { page ->

            when (page) {
                0 -> CommentsScreen()
                1 -> {}
                2 -> {}
            }
        }
    }
}


@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = title,
        color = if (isSelected) Color.Black else Color.DarkGray,
        fontSize = 14.sp,
        fontFamily = Constants.FONT_MEDIUM,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 6.dp)
    )
}

enum class DebateTab {
    COMMENTS,
    PINGS,
    SECTION1,
    SECTION2
}


@Composable
fun FullScreenImageViewDialogBox(modifier: Modifier = Modifier,image: String,onCloseClicked:()-> Unit) {
    Dialog(onDismissRequest = {onCloseClicked()}, properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)) {
        Box(modifier= Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight()){
            AsyncImage(
                model = imagePrefix + image,
                contentDescription = "",
                contentScale = ContentScale.Inside,
                modifier = Modifier.fillMaxSize()
            )
            Image(painter = painterResource(R.drawable.cross), contentDescription = "",modifier=Modifier
                .align(
                    Alignment.TopEnd
                )
                .clickable { onCloseClicked() }
                .size(24.dp))
        }
    }
}