package com.example.finalapp.screens._3createEvent


import BottomBar
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.OfferModel
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.repository.Resource
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.screens.dialogBox.GalleryPickerForDropProfile
import com.example.finalapp.screens.dialogBox.ImageCaptureFromCameraForDropProfile
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CreateEvent(
    eventsViewModel: EventsViewModel,
    navController: NavHostController
) {
    var placeText by remember { mutableStateOf("") }
    var eventTitle by remember {
        mutableStateOf("")
    }
    var eventEntered by remember {
        mutableStateOf(true)
    }
    var eventLocation by remember {
        mutableStateOf("")
    }
    var uri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var activeBtnKey by remember {
        mutableStateOf(0)
    }
    val expirationTime by remember {
        mutableStateOf(
            when(activeBtnKey){
            0-> "12"
            1-> "24"
            2->"24*7"
            else -> {"12"}
        })
    }
    var key by remember { mutableStateOf(false) }
    if (key) {
        ImageCaptureFromCameraForDropProfile { uri = it }
    }
    var keyForGallery by remember {
        mutableStateOf(0)
    }
    val buttonsVisible = remember { mutableStateOf(true) }
    val isLoading=eventsViewModel.isLoading.collectAsState()
    val isSuccess=eventsViewModel.isSuccess.collectAsState()
    if(isSuccess.value) {
        Toast.makeText(LocalContext.current,"Event Created",Toast.LENGTH_SHORT).show()
        eventsViewModel.isSuccess.value=false
        navController.navigate(SCREENS.HOME.route)
    }
    if(isLoading.value) DialogLoading()

    if (keyForGallery != 0) {
        //todo change this , its not correct
        GalleryPickerForDropProfile(
            navController = navController,{}
        ) {
            Log.d("DropProfileDialog", "DropProfileDialog: $it")
            uri = it
        }
    }

    val predictions by eventsViewModel.getAutocompletePredictions(placeText)
        .collectAsState(emptyList())
    Scaffold(
        topBar = {
            CreateEventTopBar(
                title = "Event",
                actionIcon = R.drawable.personal
            ) {
                navController.navigate(SCREENS.PREMIUM_CREATE_EVENT.route)
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp)
            )
        }
    ) { it ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState(), enabled = true)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
                {
                    if (eventEntered) {
                        //search the place
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Text(text = "Enter Event title:")
                            TextField(value = eventTitle,
                                onValueChange = {
                                    eventTitle = if (it.length <= 50) it else eventTitle
                                },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent
                                ),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "",
                                        modifier = Modifier.clickable { eventEntered =!eventEntered })
                                }
                            )


                        }

                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(0.8f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "Event name: ",
                                    modifier = Modifier.padding(end = 20.dp)
                                )
                                Text(
                                    text = eventTitle,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Normal,
                                    style = MaterialTheme.typography.titleMedium
                                )

                            }
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .clickable { eventEntered = !eventEntered }
                            )

                        }
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(), thickness = 1.dp, color = Color(0xFFDCD6DD)
                    )
                    if (eventTitle.length >= 50) Toast.makeText(
                        LocalContext.current,
                        "Max Limit reached",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (eventLocation == "") {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(text = "Location:")
                            OutlinedTextField(
                                value = placeText,
                                onValueChange = { placeText = it },
                                label = { Text("Search Places") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp)
                            )
                            if (placeText != "") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp)
                                ) {
                                    LazyColumn() {
                                        items(predictions) { prediction ->
                                            Box(modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .padding(16.dp)
                                                .clickable {
                                                    placeText = prediction
                                                        .getPrimaryText(null)
                                                        .toString()
                                                    eventLocation = placeText
                                                }) {
                                                Text(
                                                    text = prediction.getPrimaryText(null)
                                                        .toString()
                                                )

                                            }

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 12.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.location_icon),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(text = "Location: ", modifier = Modifier.padding(end = 20.dp))
                            Text(
                                text = eventLocation,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal,
                                style = MaterialTheme.typography.titleMedium
                            )

                        }

                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(), thickness = 1.dp, color = Color(0xFFDCD6DD)
                    )
                    //---------------------------------------Label image-------------------------------------------------------------------
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .padding(top = 8.dp),
                    ) {
                        if (uri != Uri.EMPTY) {
                            GlideImage(
                                model = uri,
                                contentDescription = "",
                                transition = CrossFade,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxSize(0.5f)
                                        .padding(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .background(color = Color(0xFFFFFFFE))
                                                .padding(top = 16.dp)
                                                .fillMaxWidth()
                                                .wrapContentHeight(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "Choose from Camera / Gallery.",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Black
                                            )
                                        }

                                        Row(
                                            Modifier
                                                .fillMaxSize()
                                                .background(Color.White),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Box(modifier = Modifier.size(100.dp)) {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Image(painterResource(id = R.drawable.camera),
                                                        contentDescription = "",
                                                        modifier = Modifier
                                                            .size(50.dp)
                                                            .clickable { key = !key }
                                                    )
                                                    Text(text = "Camera", modifier = Modifier)

                                                }
                                            }
                                            Box(modifier = Modifier.size(100.dp)) {
                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Image(painterResource(id = R.drawable.gallery),
                                                        contentDescription = "",
                                                        modifier = Modifier
                                                            .size(50.dp)
                                                            .clickable {
                                                                keyForGallery = 1


                                                            })
                                                    Text(text = "Gallery", modifier = Modifier)

                                                }
                                            }


                                        }
                                    }
                                }
                            }

                        }
                    }

                    //------------------------------------------------------------------------------------------------------------------------------------------//
                    //Expiration time
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Availability Time :",
                            color = Color.DarkGray,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { activeBtnKey = 0 },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (activeBtnKey == 0) statusAndTopAppBarColor else Color.LightGray,
                                    contentColor = if (activeBtnKey == 0) topAppBarTextColor else Color.DarkGray
                                )
                            ) {
                                Text(text = "12 hrs")
                            }
                            Button(
                                onClick = { activeBtnKey = 1 },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (activeBtnKey == 1) statusAndTopAppBarColor else Color.LightGray,
                                    contentColor = if (activeBtnKey == 1) topAppBarTextColor else Color.DarkGray
                                )
                            ) {
                                Text(text = "24 hrs")
                            }
                            Button(
                                onClick = { activeBtnKey = 2 },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (activeBtnKey == 2) statusAndTopAppBarColor else Color.LightGray,
                                    contentColor = if (activeBtnKey == 2) topAppBarTextColor else Color.DarkGray
                                )
                            ) {
                                Text(text = "1 week")
                            }
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(), thickness = 1.dp, color = Color(0xFFDCD6DD)
                    )
                    Button(
                        onClick = {
                            eventsViewModel.createEvent(
                                OfferModel(
                                    image= uri.toString(),
                                    category = "Event",
                                    location =  eventLocation,
                                    offer = "date",
                                    expirationTime=expirationTime
                                )
                            )
                        },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = statusAndTopAppBarColor,
                            contentColor = topAppBarTextColor,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.DarkGray
                        )
                    ) {
                        Text(text = "Create Event")
                    }


                }

            }

        }
    }
}

