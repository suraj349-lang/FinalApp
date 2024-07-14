package com.example.finalapp.screens

import BottomBar
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.offer.OfferViewModel
import com.example.finalapp.utils.RequestState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@Composable
fun DirectChatScreenUI(navController:NavHostController,offerViewModel:OfferViewModel) {
    val buttonsVisible = remember { mutableStateOf(true) }
    val scope= rememberCoroutineScope()
    val context= LocalContext.current
    val droppedProfiles by remember {
        mutableStateOf(offerViewModel.droppedProfilesList)
    }
    LaunchedEffect(key1 = true){
        scope.launch {offerViewModel.getDropProfile()  }

    }
    Scaffold(
        topBar = {
            HomeTopBar(
                "Direct Chat",
                navController,
                false,
                true,
                R.drawable.send_24
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            )
        }
    ) { it ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Direct Chat")
                when (val result=offerViewModel.getDropProfileResponse.value){
                    is RequestState.Success->{
                        offerViewModel.droppedProfilesList.value=result.data
                      //  Toast.makeText(context,"profiles Received: SUCCESS", Toast.LENGTH_SHORT).show()
                        LazyColumn{
                            items(droppedProfiles.value){profile->
                                DroppedProfile(profile)

                            }
                        }

                    }
                    is RequestState.Error->{
                        Log.d("Data received",result.error.message.toString())
                        Toast.makeText(context,"$result", Toast.LENGTH_SHORT).show()
                    }
                    RequestState.Loading->{
                        CircularProgressIndicator(color = Color(0xFF1289BE))
                    }
                    RequestState.Idle->{
                        CircularProgressIndicator(color = Color(0xFF1289BE))

                    }

                }

            }

        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DroppedProfile(profile: DropProfileModel) {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.78f
    Box(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        //.height(heightInDp - 160.dp)
        .wrapContentHeight()
        , contentAlignment = Alignment.Center)

    {
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .border(
                width = 0.5.dp, color = Color.DarkGray
            )) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(45.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
                Card(modifier = Modifier.wrapContentSize(), shape = CircleShape) {

                    GlideImage(
                        model =  R.drawable.profile_image_1,
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                androidx.compose.material3.Text(
                    text = "Beauty/random",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleMedium
                )


            }
            GlideImage(
                model = R.drawable.profile_image_3,
                contentDescription = "",
                transition=CrossFade,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightInDp - 120.dp)
                // .clip(shape = RoundedCornerShape(12.dp))
                , contentScale = ContentScale.Crop
            )
            profile.location?.let { Text(text = it) }
            profile.image?.let { Text(text = it) }
            profile.expirationTime?.let { Text(text = it) }
            profile.message?.let { Text(text = it) }
            profile.landmark?.let { Text(text = it) }

        }
    }

}
