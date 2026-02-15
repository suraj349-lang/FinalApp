package com.spint.app.screens._2Events.events

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import coil.compose.AsyncImage
import com.spint.app.R
import com.spint.app.model.EventResponse
import com.spint.app.navigation.SCREENS
import com.spint.app.screens.dialogBox.DialogError
import com.spint.app.screens.dialogBox.DialogLoading
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.RequestState
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.viewmodels.HomeViewModel



@Composable
fun EventsDetailsVerticalWrapper(id:String, navController: NavHostController, homeViewModel: HomeViewModel, authViewModel: AuthViewModel) {

    val eventDetailsResponse by homeViewModel.eventDetailsResponse.collectAsState()
    Log.i("EventDetailsResponse", "PublicEventDetailsScreenWrapper: $eventDetailsResponse")

    LaunchedEffect(id ){
        homeViewModel.getEventDetails(id)
    }

    Scaffold(
        content = {
            when (val response = eventDetailsResponse) {
                is RequestState.Success -> {
                    androidx.compose.material3.Surface(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize(), color = Color.Black
                    ) {
                        EventsDetailsVertical(
                            response.data,
                            authViewModel,
                            homeViewModel,
                            navController,
                            { navController.navigateUp() }
                        ){
                            navController.navigate(SCREENS.COMMENT.route)
                        }
                    }
                }

                is RequestState.Error -> {
                    androidx.compose.material3.Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it), color = Color.Black
                    ) {
                        DialogError {
                            navController.navigateUp()
                        }
                    }
                }

                is RequestState.Loading -> {
                    androidx.compose.material3.Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it), color = Color.Black
                    ) {
                        DialogLoading() {
                            navController.navigateUp()
                        }
                    }
                }

                else -> {

                }
            }

        }
    )
}

@Composable
fun EventsDetailsVertical(
    event: EventResponse,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    onBackClicked: () -> Unit,
    onCommentClicked: () -> Unit
) {


    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {}, navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(
                    Color.White
                ),
                contentDescription = ""
            )
        })
    }) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)){
            Column {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)) {
                    AsyncImage(model = imagePrefix+ event, contentDescription ="", modifier = Modifier.fillMaxSize()
                        , contentScale = ContentScale.Crop )

                }

                // title and description
                EventDetailsTitleAndDescription(event.title,event.description ?: "")
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Gray)
                SideNavAndContent()
            }



        }
        
    }
}

@Composable
fun SideNavAndContent() {
    Row(Modifier) {
        Column(modifier = Modifier
            .width(60.dp)
            .wrapContentHeight()) {
            IconWithTextNav(icon = R.drawable.create_event, text ="20" )
            IconWithTextNav(icon = R.drawable.ping, text ="20" )
            IconWithTextNav(icon = R.drawable.drop_profile_filled_2, text ="20" )
            IconWithTextNav(icon = R.drawable.direct_chat, text ="20" )

        }

    }
}

@Composable
fun IconWithTextNav(icon: Int, text: String, tint: Color = Color.White,textColor:Color=Color.Black,onClick:()->Unit={}) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .wrapContentHeight()
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
         //   colorFilter = ColorFilter.tint(tint)
        )
        androidx.compose.material3.Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            color = textColor, lineHeight = 18.sp
        )
    }
}


@Composable
fun EventDetailsTitleAndDescription(title:String,description:String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Text(text =title , fontSize = 14.sp, fontFamily = Constants.FONT_MEDIUM, color = Color.White)
        Text(text =description , fontSize = 11.sp, fontFamily = Constants.FONT_LIGHT, color = Color.LightGray)
    }
}
