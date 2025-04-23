package com.example.finalapp.screens._1home._1_1ExperimentScreenEvents.old

/*
@Composable
fun DroppedProfiles(navController:NavHostController, eventsViewModel: EventsViewModel) {
    val context= LocalContext.current
    val droppedProfiles by remember {
        mutableStateOf("")//eventsViewModel.droppedProfilesList)
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
                        text ="Suraj singh",//profile.location,
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
*/