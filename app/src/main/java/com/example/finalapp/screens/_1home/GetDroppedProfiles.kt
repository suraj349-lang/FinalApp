package com.example.finalapp.screens._1home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.utils.RequestState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.launch

@Composable
fun GetDroppedProfiles(navController:NavHostController, eventsViewModel: EventsViewModel) {
    val context= LocalContext.current
    val droppedProfiles by remember {
        mutableStateOf(eventsViewModel.droppedProfilesList)
    }
    val widthINDp = LocalConfiguration.current.screenWidthDp
    var query by remember { mutableStateOf("") }
    val predictions by eventsViewModel.getAutocompletePredictions(query).collectAsState(emptyList())
    LaunchedEffect(key1 = true){
        eventsViewModel.getDropProfile()

    }
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val result=eventsViewModel.getDropProfileResponse.value){
                    is RequestState.Success->{
                        eventsViewModel.droppedProfilesList.value=result.data
                      //  Toast.makeText(context,"profiles Received: SUCCESS", Toast.LENGTH_SHORT).show()
//                        LazyColumn{
//                            items(droppedProfiles.value){profile->
//                                DroppedProfile(profile)
//
//                            }
//                        }
                        var height by remember {
                            mutableStateOf(true)
                        }
                        Column(modifier = Modifier.fillMaxSize()) {
                            Column {
                                OutlinedTextField(
                                    value = query,
                                    onValueChange = { query = it },
                                    label = { Text("Search Places") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )

                                LazyColumn {
                                    items(predictions) { prediction ->
                                        Text(
                                            text = prediction.getPrimaryText(null).toString(),
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .clickable {
                                                    // Handle click on prediction
                                                    query = prediction.getPrimaryText(null).toString()
                                                }
                                        )
                                    }
                                }
                            }
                            Card(modifier = Modifier
                                .height(if (height) 30.dp else 100.dp)
                                .fillMaxWidth(), backgroundColor = Color(0xFFC5BBBE)
                            )
                            {
                                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                    if(height) Text(text = "Event Details: Marriage function at Patna", fontSize = 14.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(start=8.dp))
                                    else {
                                        Column(modifier = Modifier
                                            .fillMaxSize()
                                            .padding(2.dp)) {
                                            Text(text = "Event Details: Marriage function at Patna", fontSize = 14.sp, fontWeight = FontWeight.Normal, modifier = Modifier.padding(start=8.dp))
                                            Text(
                                                text = "Location: Ganpati marriage hall",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                            Text(
                                                text = "Location: Ganpati marriage hall",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                            Text(
                                                text = "Location: Ganpati marriage hall",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Normal,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )

                                        }


                                    }
                                    Icon(imageVector =if(height) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp, contentDescription ="", modifier = Modifier
                                        .padding(end = 12.dp)
                                        .clickable { height = !height } )
                                }

                            }

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(2.dp),
                                verticalArrangement = Arrangement.spacedBy(1.dp),
                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                items(droppedProfiles.value) { profile ->
                                    DroppedProfile(profile = profile)

                                }
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




@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DroppedProfile(profile: DropProfileModel) {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.5f
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp)
        )

    {
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxSize()
            .border(
                width = 0.1.dp, color = Color.LightGray
            )) {

            profile.expirationTime?.let { Text(text = it) }
            profile.message?.let { Text(text = it) }
            profile.location?.let { Text(text = it, maxLines = 1) }

            GlideImage(
                model =  if(profile.image!="") profile.image else R.drawable.femaleprofile,
                contentDescription = "",
                transition=CrossFade,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightInDp - 120.dp)
                // .clip(shape = RoundedCornerShape(12.dp))
                , contentScale = ContentScale.Crop
            )


            profile.landmark?.let { Text(text = it) }

        }
    }

}
