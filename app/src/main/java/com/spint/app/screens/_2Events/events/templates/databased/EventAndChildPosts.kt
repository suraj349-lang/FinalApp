package com.spint.app.screens._2Events.events.templates.databased

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.spint.app.R
import com.spint.app.model.EventResponse
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._2Events.events.templates.IconWithTextVertical2
import com.spint.app.screens._2Events.events.templates.databased.util.FunctionsAndStatsDataBased
import com.spint.app.screens._2Events.events.utils.FollowButtonVertical
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.constants.Constants



@Composable
fun EventsAndChildPostsParent(event: EventResponse, navController: NavHostController, onUpVotesClicked: (String) -> Unit) {
    val context= LocalContext.current
    val window = (context as Activity).window
    val backgroundColor= Constants.HOME_TOP_BAR_COLOR

    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Constants.HOME_STATUS_BAR_COLOR.toArgb()
    window.navigationBarColor = Constants.HOME_NAV_BAR_COLOR.toArgb()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {

        EventAndChildPostUI(
            event=event,
            navController,
            backgroundColor,
            onEventDetailsClicked={navController.navigate(SCREENS.XHAM_LIVE_SCREEN.route)},
            onUpVotesClicked ={onUpVotesClicked(it)},
            onContributeClicked = {navController.navigate(SCREENS.CREATE_EVENT.createRoute(event._id))},
            onJoinEventClicked={ navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.createRoute(event._id))},
            onAddChildPostClicked = {navController.navigate(SCREENS.CREATE_EVENT.createRoute(event._id))},
            onChildPostClicked = {navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.createRoute(it))}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventAndChildPostUI(
    event: EventResponse,
    navController: NavHostController,
    backgroundColor:Color,
    onEventDetailsClicked:(String)->Unit,
    onUpVotesClicked: (String) -> Unit,
    onContributeClicked: () -> Unit,
    onJoinEventClicked: () -> Unit,
    onAddChildPostClicked: () ->Unit,
    onChildPostClicked: (String) ->Unit
) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = backgroundColor)
            ) {
                AsyncImage(
                    model = imagePrefix + event.image, contentDescription = "", modifier = Modifier
                        .clickable { onEventDetailsClicked(event._id) }
                        .fillMaxSize(), contentScale = ContentScale.Crop
                )
                FunctionsAndStatsDataBased(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                        .wrapContentWidth()
                        .wrapContentHeight(),totalUpVotes = event.totalUpVotes, totalComments = event.totalComments, totalViews = event.totalViews,{},{},{}
                )

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomStart), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {

                    Row(
                        modifier = Modifier.padding(start = 8.dp)
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = imagePrefix + event.user.profileImage,
                            contentDescription = "",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(shape = CircleShape), contentScale = ContentScale.Crop,
                            filterQuality = FilterQuality.High
                        )

                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            Text(
                                text = event.userName,
                                fontFamily = Constants.USER_NAME_FONT,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "expiring at : 12 pm.",
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 9.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }
                        FollowButtonVertical(
                            modifier = Modifier

                        ) {
                            onJoinEventClicked()
                        }
                    }
                    Column(modifier = Modifier.padding(start = 16.dp,top=6.dp)
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()) {
                        Text(text = event.title, fontSize = 16.sp, fontFamily = Constants.FONT_MEDIUM, color = Color.White, lineHeight = 12.sp)
                        Text(text = event.description ?: "", fontSize = 12.sp, fontFamily = Constants.FONT_LIGHT, color = Color.LightGray, lineHeight = 12.sp)

                    }
//                    event.childPosts?.let {
//                        ChildMicroPostsDataBasedNew(modifier= Modifier
//                            //  .align(Alignment.BottomStart)
//                            .fillMaxWidth()
//                            .height(150.dp)
//                            .padding(start = 4.dp, bottom = 8.dp, top = 4.dp),
//                            it,onAddChildPostClicked,onChildPostClicked)
//                    }

                    //  EventDescriptionVertical(event.description ?: "",Modifier.padding(horizontal = 4.dp))

                }

            }


//            event.childPosts?.let {
//                ChildMicroPostsDataBased(modifier= Modifier
//                    .align(Alignment.BottomStart)
//                    .fillMaxWidth()
//                    .height(180.dp)
//                    .padding(start = 4.dp, bottom = 8.dp, top = 4.dp),
//                it,onAddChildPostClicked,onChildPostClicked)
//            }



        }
    }






@Composable
fun StatsDataBasedUI(isChildPostPresent: Boolean?, totalViews: Int = 45, totalChildPosts: Int = 57) {
    val darkBgColor=Color.DarkGray
    //0xFF8D0808 0xFF060635
    val lightBgColor=Color.DarkGray
    val darkTextColor= Color(0xFFFFFFFF)
    val lightTextColor= Color(0xFFFFFFFF)
    Row(modifier = Modifier
        .background(color = Color(0xFFFFFFFF))
        .padding(vertical = 2.dp)
        // .padding(start = 4.dp)
        .fillMaxWidth()
        .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceEvenly) {
        UserReactionItemCard(null, darkBgColor ,null, darkTextColor,"Pings","100k")
        UserReactionItemCard(null, darkBgColor ,null, darkTextColor,"Drops","23k")
        Card(modifier = Modifier
            .width(90.dp)
            .height(44.dp), colors = CardDefaults.cardColors(containerColor = lightBgColor), elevation = CardDefaults.cardElevation(20.dp)) {
            Column(modifier = Modifier
                .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if(isChildPostPresent==false){
                    Text(text = totalChildPosts.toString(),color = lightTextColor, fontFamily = Constants.USER_NAME_FONT, fontSize = 16.sp, lineHeight = 18.sp)
                    Text(text = "RePosts", color = lightTextColor, fontFamily = Constants.FONT_LIGHT, fontSize = 10.sp,lineHeight = 18.sp, fontWeight = FontWeight.Bold)
                }else{
                    Text(text = "+ post",color = lightTextColor, fontFamily = Constants.USER_NAME_FONT, fontSize = 16.sp, lineHeight = 18.sp)
                }
            }
        }

        UserReactionItemCardDataBased(lightBgColor,null,lightTextColor,null,"Nearby","335k")


    }
}

@Composable
fun UserReactionItemCard(x0: Nothing?, x1: Color, x2: Nothing?, x3: Color, x4: String, x5: String) {
    TODO("Not yet implemented")
}

@Composable
fun UserReactionItemCardDataBased(lightBgColor:Color? ,darkBgColor: Color?,lightTextColor:Color?,darkTextColor:Color?,title:String,value:String) {
    Card(modifier = Modifier
        .width(90.dp)
        .height(44.dp), colors = CardDefaults.cardColors(containerColor = lightBgColor ?: darkBgColor!!), elevation = CardDefaults.cardElevation(20.dp)) {
        Column(modifier = Modifier
            .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value,color = lightTextColor ?: darkTextColor!!, fontFamily = Constants.USER_NAME_FONT, fontSize = 16.sp, lineHeight = 18.sp)
            Text(text = title, color = lightTextColor ?: darkTextColor!!, fontFamily = Constants.FONT_LIGHT, fontSize = 10.sp,lineHeight = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun UserReactionUIDataBased(totalUpVotes:Int,totalComments: Int, totalViews: Int,onUpVotesClicked:()->Unit,onShareClicked:()->Unit,onContributeClicked:()->Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFE4AE29)), shape = RoundedCornerShape(0.dp)
    ) {
        Row(modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(30.dp), verticalAlignment = Alignment.CenterVertically) {

            IconWithTextDataBased(icon = R.drawable.upvote, text =totalUpVotes.toString() ){
                onUpVotesClicked()
            }
            IconWithTextDataBased(icon = R.drawable.people, text = totalViews.toString())
            IconWithTextDataBased(icon = R.drawable.comment_filled, text =totalComments.toString() )
            IconWithTextDataBased(icon = R.drawable.share_event, text ="Share" , tint = Constants.HOME_TOP_BAR_TITLE_COLOR){
                onShareClicked()
            }
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF689F38))) { //0xFF050F53
                Text(
                    text = "+Contribute ",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier= Modifier
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                        .clickable { onContributeClicked() },
                    color= Color.White
                )

            }
//0xFF08087C
//        Card(
//            colors = CardDefaults.cardColors(containerColor = Color(0xFF08087C)), modifier = Modifier.padding(top=2.dp, start = 20.dp)
//        ) {
//            Text(text = "+ Contribute", fontFamily = Constants.FONT_MEDIUM, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp))
//
//        }
        }

    }
}

@Composable
fun IconWithTextDataBased(icon: Int, text: String, tint: Color = Color.White,textColor:Color=Color.White,onClick:()->Unit={}) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxHeight()
            .wrapContentWidth(),
        horizontalArrangement=Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(tint)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            color=textColor
        )
    }
}




@Composable
fun ChildMicroPostsDataBased(modifier:Modifier, events: List<EventResponse>, onAddChildPostClicked: () -> Unit, onChildPostClicked:(String)->Unit) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
                Column(modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(), verticalArrangement = Arrangement.Top) {
                   // Text(text = "Trails ->", fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp, color = Color.White, lineHeight = 8.sp)
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            //.clip(RoundedCornerShape(8.dp))
                            .background(color = Color.Black)
                            //.fillMaxWidth()
                            .fillMaxSize()

                    ) {
                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.add),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.tint(Color.White),
                                modifier = Modifier
                                    .clickable { onAddChildPostClicked() }
                                    .size(40.dp)
                            )
                            Text(
                                text = "Add a trail",
                                fontFamily = Constants.FONT_LIGHT,
                                fontSize = 12.sp, color = Color.White
                            )
                        }
                    }

                }
            }

            items(events) { event ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = imagePrefix+event.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable { onChildPostClicked(event._id) }
                            .fillMaxSize(),
                        filterQuality = FilterQuality.High
                    )
                    Text(text = "@"+event.user.userName,fontFamily=Constants.USER_NAME_FONT, fontWeight = FontWeight.ExtraBold,fontSize=12.sp, modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.BottomStart)
                        .shadow(
                            elevation = 20.dp,
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        ), color = Color.White)
//                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
//                        Text(text = event.title, color = Color.White, fontFamily = Constants.FONT_LIGHT, lineHeight = 18.sp, fontSize = 9.sp)
//                        Text(text = event.description ?: "", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, lineHeight = 18.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 9.sp)
//                    }
                }
            }
        }

}



@Composable
fun UserReactionDataBased(totalUpVotes:Int,totalComments: Int, totalViews: Int,onUpVotesClicked:()->Unit,onShareClicked:()->Unit,onContributeClicked:()->Unit) {
    Column(modifier = Modifier
        .padding(end = 16.dp, bottom = 16.dp)
        .wrapContentWidth()
        .wrapContentHeight(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        IconWithTextVertical2(icon = R.drawable.upvote, text =totalUpVotes.toString() ){
            onUpVotesClicked()
        }
        IconWithTextVertical2(icon = R.drawable.people, text = totalViews.toString())
        IconWithTextVertical2(icon = R.drawable.comment_filled, text =totalComments.toString() )
        IconWithTextVertical2(icon = R.drawable.share_event, text ="Share" , tint = Constants.HOME_TOP_BAR_TITLE_COLOR) {
            onShareClicked()
        }
    }

}



