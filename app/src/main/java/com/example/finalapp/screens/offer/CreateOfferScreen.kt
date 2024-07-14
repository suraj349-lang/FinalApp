package com.example.finalapp.screens.offer

import BottomBar
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.offer.OfferViewModel
import com.example.finalapp.screens.HomeTopBar
import com.example.finalapp.utils.RequestState


@Composable
fun CreateOfferScreen(authViewModel: AuthViewModel, offerViewModel: OfferViewModel, navController: NavHostController= NavHostController(LocalContext.current)) {
    val buttonsVisible = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context= LocalContext.current
    val address by authViewModel.address.collectAsState()
    var offerText by remember {
        mutableStateOf("")
    }
    var key by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            HomeTopBar(
                title = "Raise Offer",
                navController=navController,
                navIcon = false,
                actionIcon = false,
                icon = R.drawable.raise_offer
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
            if(key){
                Dialog(onDismissRequest = { key=false }) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_image_1),
                            contentDescription = "",
                            modifier=Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .padding(8.dp))
            {
                Box(modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()) {
                        Image(painter = painterResource(id = R.drawable.profile_image_1),
                            contentDescription ="",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            key = true
                                        }
                                    )
                                } )
                        Text(" Change Image ", fontSize = 12.sp, modifier = Modifier
                            .padding(4.dp)
                            .background(color = Color.White)
                            .align(
                                Alignment.BottomCenter
                            ))
                 
                            
                }
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Category")},
                    placeholder = { Text(text = "Category...")},
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription ="" )},
                    trailingIcon = { Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription ="" )}
                )
                Text(text = "Location: $address", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp), maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 18.sp)
                TextField(
                    value = offerText,
                    onValueChange ={offerText=it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 400.dp),
                    placeholder = { Text(text = "Request / offer")}
                )
                Button(modifier = Modifier.fillMaxWidth(0.4f), onClick = { offerViewModel.createOffer(OfferModel(image = "fdkgflkdnbfndjb", category = "basketball", location = address, offer = "Need 4 basketball players", expirationTime = "0800")) }) {
                    Text("Create Offer", color = Color.White)

                }
                when(val result=offerViewModel.offerResponse.value){
                    is RequestState.Idle ->{

                    }
                    is RequestState.Error->{
                        Toast.makeText(context,"Error creating Offer",Toast.LENGTH_SHORT).show()
                        Log.d("OfferCreation", "CreateOfferScreen:${result.error.message} ")

                    }
                    is RequestState.Loading->{
                        CircularProgressIndicator()
                    }
                    is RequestState.Success->{
                        Toast.makeText(context,"Offer Raised successfully",Toast.LENGTH_SHORT).show()
                        navController.navigate(SCREENS.PAST_OFFERS.route)

                    }
                }





            }


        }
    }
}


@Composable
fun CreateOffer(){
    var offerText by remember {
        mutableStateOf("")
    }

    Card(modifier = Modifier
        .wrapContentSize(), colors = CardDefaults.cardColors(containerColor = Color(0xFFE4D50F)), shape= RoundedCornerShape(12.dp)) {
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
        onValueChange ={offerText=it},
        label = { Text(text = "Offer Text")},
        placeholder = { Text(text = "Offer Text")},
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 300.dp),
        maxLines = 12
    )
}