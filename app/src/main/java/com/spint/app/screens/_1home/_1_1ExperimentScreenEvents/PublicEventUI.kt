package com.spint.app.screens._1home._1_1ExperimentScreenEvents


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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.navigation.SCREENS
import com.spint.app.viewmodels.EventsViewModel
import com.spint.app.screens.dialogBox.GalleryPickerForDropProfile
import com.spint.app.ui.theme.statusAndTopAppBarColor
import com.spint.app.ui.theme.statusBarColor
import com.spint.app.utils.RequestState
import java.io.File


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PublicEventUI(authViewModel: AuthViewModel, eventsViewModel: EventsViewModel, navController: NavHostController= NavHostController(LocalContext.current)) {
    val buttonsVisible = remember { mutableStateOf(true) }
    val context= LocalContext.current
    var key by remember { mutableStateOf(true) }
    var keyForGallery by remember { mutableStateOf(0) }
    var uri by remember { mutableStateOf(Uri.EMPTY) }
    var imageFile by mutableStateOf<File?>(null)
    if(keyForGallery==1) GalleryPickerForDropProfile(navController  ,{imageFile=it}){uri=it}

    Scaffold(
        topBar = {
            CreateEventTopBar(
                title = "Event",
                actionIcon = R.drawable.personal){
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
                    if (uri != Uri.EMPTY) {
                        GlideImage(
                            model = uri,
                            contentDescription = "",
                            transition = CrossFade, contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        )
                    } else {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(8.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
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
                                            androidx.compose.material3.Text(
                                                text = "Camera",
                                                modifier = Modifier
                                            )

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
                    PopularCategories()
                    AvailabilityTime()
                    CategorySearch()

                    when (val result = eventsViewModel.premiumCreateEventResponse.value) {
                        is RequestState.Idle -> {

                        }

                        is RequestState.Error -> {
                            Toast.makeText(context, "Error creating event", Toast.LENGTH_SHORT).show()
                            Log.d("EventCreation", "CreateEventScreen:${result.error.message}" )
                        }

                        is RequestState.Loading -> {
                            CircularProgressIndicator()
                        }

                        is RequestState.Success -> {
                            Toast.makeText(context, "Event Created successfully", Toast.LENGTH_SHORT).show()
                            eventsViewModel.premiumCreateEventResponse.value = RequestState.Idle
                            //  navController.navigate(SCREENS.PAST_OFFERS.route)
                        }
                    }


                }


            }
        }

    }}

@Composable
fun CategorySearch() {
    var category by remember { mutableStateOf("") }
    OutlinedTextField(
        value = category,
        onValueChange = { category = it },
        label = { Text(text = "Category") },
        placeholder = { Text(text = "Category...") },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = ""
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = ""
            )
        }
    )
}

@Composable
fun AvailabilityTime() {
    var activeBtnKey by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(top = 4.dp)
        , horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Availability Time :",
            color = Color.DarkGray,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { activeBtnKey=0 },
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(activeBtnKey==0) statusAndTopAppBarColor else Color.LightGray
                )
            ) {
                Text(text = "12 hrs",color =if(activeBtnKey==0) Color(0xFFEEE6A2) else Color.DarkGray)
            }
            Button(onClick = {activeBtnKey=1 },
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(activeBtnKey==1) statusAndTopAppBarColor else Color.LightGray
                )
            ) {
                Text(text = "24 hrs",color =if(activeBtnKey==1) Color(0xFFEEE6A2) else Color.DarkGray)
            }
            Button(onClick = { activeBtnKey=2 },
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(activeBtnKey==2) statusAndTopAppBarColor else Color.LightGray
                )
            ) {
                Text(text = "1 week",color =if(activeBtnKey==2) Color(0xFFEEE6A2) else Color.DarkGray)
            }
        }
    }
}

@Composable
fun PopularCategories() {
    Text(text = "Popular categories:", modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp, bottom = 4.dp), fontSize = 12.sp)
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
        items(offerList) {offer->
            Column(
                modifier = Modifier.wrapContentSize().padding(start = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = offer.icon),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = offer.title,
                    fontSize = 8.sp,
                    color = Color(0xFF701C02),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }



    }
}


data class OfferType(
    val title:String,
    val icon:Int
)
val offerList= listOf(
    OfferType("Movie",R.drawable.cinema),
    OfferType("Clubs",R.drawable.nightclubnew),
    OfferType("Coffee",R.drawable.coffeecup),
    OfferType("Personal",R.drawable.heart),
    OfferType("Dinner",R.drawable.dinner),

    )

@Composable
fun CreateOffer() {
    var offerText by remember {
        mutableStateOf("")
    }

    Card(
        modifier = Modifier
            .wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE4D50F)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Offer Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp),
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                color = Color(0xFF111001)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_image_1),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.padding(2.dp)
                )

            }
        }


    }
    OutlinedTextField(
        value = offerText,
        onValueChange = { offerText = it },
        label = { Text(text = "Offer Text") },
        placeholder = { Text(text = "Offer Text") },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 300.dp),
        maxLines = 12
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventTopBar(title: String, actionIcon: Int,onActionClicked:()->Unit){


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = statusBarColor  //  0xF8E2A61A
        ),
        title = {
            Text(
                title,
                fontSize = 20.sp,
                maxLines = 1,
                fontWeight=FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top=8.dp), color = Color(
                    0xFF000000
                ), style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
//            if(navIcon.isNotEmpty()) {
//                Image(
//                    painter = painterResource(
//                        id = navIcon[0]
//                    ),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .clickable { onActionClicked() }
//                        .padding(top = 6.dp)
//                        .size(40.dp)
//                )
//            }
            Image(
                painter = painterResource(
                    id = R.drawable.app_icon
                ),
                colorFilter = ColorFilter.tint(color= Color(0xFF000000)),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(28.dp)
            )
        }, actions = {
            actionIcon.let {
                Image(painter = painterResource(id = actionIcon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color= Color(0xFF000000)),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp)
                        .clickable {
                            onActionClicked()
                        })

            }

        }
    )
}
