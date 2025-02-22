package com.example.finalapp.screens._1home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.ui.imagePrefix


@Composable
fun DroppedProfiles(navController:NavHostController, eventsViewModel: EventsViewModel) {
    val context= LocalContext.current
    val droppedProfiles by remember {
        mutableStateOf(eventsViewModel.droppedProfilesList)
    }
    val widthInDp = LocalConfiguration.current.screenWidthDp
    var query by remember { mutableStateOf("") }
    val predictions by eventsViewModel.getAutocompletePredictions(query).collectAsState(emptyList())
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

                                LazyColumn(modifier = Modifier.zIndex(1f)) {
                                    items(predictions) { prediction ->
                                        Text(
                                            text = prediction.getPrimaryText(null).toString(),
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .clickable {
                                                    // Handle click on prediction
                                                    query = prediction
                                                        .getPrimaryText(null)
                                                        .toString()
                                                }
                                        )
                                    }
                                }
                            }
                            val items=remember{droppedProfiles.value}
                            LazyVerticalGrid(
                                modifier=Modifier.zIndex(0f),
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(2.dp),
                                verticalArrangement = Arrangement.spacedBy(1.dp),
                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                            ) {

                                items(items,key={item->item.id.toString()}) { profile ->
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

/*
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
 */



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DroppedProfile(profile: DropProfileModel) {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.5f
    Card(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
        shape = RoundedCornerShape(4.dp)
        )

    {
        Box(
            modifier = Modifier.fillMaxSize() // Box to overlay content
        ) {
            // Image in the background
            GlideImage(
                model = imagePrefix+profile.image, // Replace with your image resource
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop, // Crop to fill the space
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heightInDp - 120.dp) // Fill the entire space
            )

            // Multiple texts
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart) // Center the entire column
                    .padding(8.dp), // Add padding for spacing
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom// Center texts horizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Suraj singh",//profile.location,
                        modifier=Modifier.fillMaxWidth(0.8f),
                        maxLines=1,
                        overflow=TextOverflow.Ellipsis,
                        style = TextStyle(color = Color.White, fontSize = 18.sp)
                    )
                    Text(
                        text = profile.expirationTime+" hrs.",
                        modifier=Modifier.fillMaxWidth(1f),
                        maxLines=1,
                        overflow=TextOverflow.Ellipsis,
                        style = TextStyle(color = Color.White, fontSize = 10.sp)
                    )

                }

                Text(
                    text =profile.message,
                    style = TextStyle(color = Color.White, fontSize = 10.sp)
                )
            }
        }

    }

}
