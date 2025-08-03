package com.example.finalapp.screens._1home.EventAndPingDesigns.events

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.finalapp.R
import com.example.finalapp.model.EventResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._1home.commonUI.shareDeepLink
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants


@Composable
fun EventScreen(event: EventResponse, navController: NavHostController, onUpVotesClicked: (String) -> Unit) {
    val context= LocalContext.current
    val window = (context as Activity).window

    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Constants.HOME_STATUS_BAR_COLOR.toArgb()
    window.navigationBarColor = Constants.HOME_NAV_BAR_COLOR.toArgb()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF03092E)
    ) {
        if(event.isChildPost){
            ChildEventScreenUI(event = event)
        }else{
        EventScreenUI(
            event=event,
            onUpVotesClicked ={onUpVotesClicked(it)},
            onContributeClicked = {navController.navigate(SCREENS.CREATE_EVENT.createRoute(event._id))},
            onJoinEventClicked={ navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.createRoute(event._id))},
            onAddChildPostClicked = {navController.navigate(SCREENS.CREATE_EVENT.createRoute(event._id))},
            onChildPostClicked = {navController.navigate(SCREENS.PUBLIC_EVENT_DETAILS_SCREEN_WRAPPER.createRoute(it))}
        )
        }
    }
}

@Composable
fun EventScreenUI(
    event: EventResponse,
    onUpVotesClicked: (String) -> Unit,
    onContributeClicked: () -> Unit,
    onJoinEventClicked: () -> Unit,
    onAddChildPostClicked: () ->Unit,
    onChildPostClicked: (String) ->Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .shadow(
                    elevation = 20.dp,
                    ambientColor = floatingActionBtnColor,
                    spotColor = floatingActionBtnColor
                )
                .zIndex(5f)){
            AsyncImage(model = imagePrefix+event.image, contentDescription ="", modifier = Modifier
                .fillMaxSize(), filterQuality = FilterQuality.High, contentScale = ContentScale.Crop
            )
            JoinEventButton(modifier = Modifier.align(Alignment.BottomEnd)) {
                 onJoinEventClicked()
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            AsyncImage(model = imagePrefix+ event.user.profileImage,
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape)
                    .shadow(elevation = 6.dp, ambientColor = Color.White, spotColor = Color.White)
                    .zIndex(4f)
                , contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.High
            )
            Column(modifier = Modifier
                .wrapContentSize()
                .shadow(60.dp, spotColor = Color.White)) {
                Text(text = event.userName, fontFamily = Constants.USER_NAME_FONT,fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                EventTitle(event.title)
            }
        }

        EventDescription(event.description ?: "")
      //  JoinEventButton(onJoinEventClicked)
        UserReactionUI(event.totalUpVotes,event.totalComments,event.totalViews,{onUpVotesClicked(event._id)},{ shareDeepLink(context ,event._id) },onContributeClicked)

        Stats(event.totalViews,event.totalChildPosts)
        MicroPosts(event.childPosts ?: listOf(), onAddChildPostClicked = onAddChildPostClicked, onChildPostClicked ={onChildPostClicked(it)} )
    }
}

@Composable
fun EventTitle(eventTitle:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
            Text(text = eventTitle.capitalize(), fontFamily = Constants.FONT_LIGHT, fontSize = 18.sp, color = Color.White)
        }
}
@Composable
fun EventDescription(
    text: String="",
) {
    if(text.isNotEmpty()) {

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = text,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontFamily = Constants.FONT_EXTRA_LIGHT,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp,
            )
        }
    }
}

@Composable
fun JoinEventButton(modifier: Modifier,onJoinEventClicked:()->Unit) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.End) {
        Button(onClick = { onJoinEventClicked()}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A0505))) {
            Text(text = "JOIN EVENT")
        }
    }

}

@Composable
fun Stats(totalViews: Int, totalChildPosts: Int) {
    Row(modifier = Modifier
        .padding(horizontal = 8.dp)
        .fillMaxWidth()
        .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth(0.5f)
            .height(60.dp), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 2.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(text = "Total views", color = Color.White, fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp)
                Text(text = totalViews.toString(),color = Color.White, fontFamily = Constants.USER_NAME_FONT, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Last 3 hours",color = Color.White, fontFamily = Constants.FONT_LIGHT)
                
            }

            
        }
        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .height(60.dp), colors = CardDefaults.cardColors(containerColor = Color.LightGray)) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 2.dp), verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(text = "Child Posts", color = Color.Black, fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp)
                Text(text = totalChildPosts.toString(),color = Color.Black, fontFamily = Constants.USER_NAME_FONT, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "10/26m", color = Color.Black, fontFamily = Constants.FONT_LIGHT)

            }


        }
    }
}

@Composable
fun UserReactionUI(totalUpVotes:Int,totalComments: Int, totalViews: Int,onUpVotesClicked:()->Unit,onShareClicked:()->Unit,onContributeClicked:()->Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.DarkGray)) {
    Row(modifier = Modifier
        .padding(horizontal = 8.dp)
        .fillMaxWidth()
        .wrapContentHeight(), horizontalArrangement = Arrangement.spacedBy(30.dp), verticalAlignment = Alignment.CenterVertically) {

        IconWithText(icon = R.drawable.upvote, text =totalUpVotes.toString() ){
            onUpVotesClicked()
        }
        IconWithText(icon = R.drawable.people, text = totalViews.toString())
        IconWithText(icon = R.drawable.comment_filled, text =totalComments.toString() )
        IconWithText(icon = R.drawable.share_event, text ="Share" , tint = Constants.HOME_TOP_BAR_TITLE_COLOR){
            onShareClicked()
        }
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF050F53))) {
            Text(
                text = "+Contribute",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier= Modifier
                    .padding(4.dp)
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
fun IconWithText(icon: Int, text: String, tint: Color = Color.White,textColor:Color=Color.White,onClick:()->Unit={}) {
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
            modifier = Modifier.size(16.dp),
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
fun MicroPosts(events: List<EventResponse>,onAddChildPostClicked:()->Unit,onChildPostClicked:(String)->Unit) {
    ChildMicroPosts(events = events, onAddChildPostClicked = onAddChildPostClicked, onChildPostClicked = {onChildPostClicked(it)})
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChildMicroPosts(events: List<EventResponse>, onAddChildPostClicked: () -> Unit, onChildPostClicked:(String)->Unit) {
    LazyHorizontalGrid(
        modifier=Modifier.padding(start=6.dp),
        rows = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (events.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(color = Color.Gray)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            colorFilter=ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .clickable { onAddChildPostClicked() }
                                .size(40.dp)
                        )
                        Text(text ="Add child post", fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp )
                    }
                }

            }
        } else {
            items(events) { event ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = imagePrefix+event.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable { onChildPostClicked(event._id) }
                            .fillMaxSize(), alpha = 0.8f,
                        filterQuality = FilterQuality.High
                    )
                    Text(text = event.user.userName, modifier = Modifier.align(Alignment.TopStart))
                    Text(text = event.title, modifier = Modifier.align(Alignment.BottomStart))
                    Text(text = event.description ?: "", modifier = Modifier.align(Alignment.BottomStart))
                }
            }
        }
    }
}

