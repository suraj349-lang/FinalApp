package com.example.finalapp.screens._1home


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.viewmodels.EventsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.testdata.Item
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DroppedProfilesNew(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    eventsViewModel: EventsViewModel,
) {
    val context= LocalContext.current
    val triggerFetch by eventsViewModel.triggerFetch.collectAsState()
    val droppedProfiles by eventsViewModel.droppedProfiles.collectAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    var query by remember { mutableStateOf("") }
    var showPredictionBoxForSearch by remember {
        mutableStateOf(false)
    }
    var showSearchUI by remember {
        mutableStateOf(false)
    }
    val scope= rememberCoroutineScope()
    var labelText by remember {
        mutableStateOf("Enter location")
    }

    val predictions by eventsViewModel.getAutocompletePredictions(query).collectAsState(emptyList())

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding()) {
        Column(
            modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = scrollBehavior.state.overlappedFraction == 0f) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = {
                            query = it
                            showPredictionBoxForSearch = it.isNotEmpty()
                        },
                        label = {
                            Text(
                                text = "Search Profiles",
                                fontFamily = FontFamily(Font(R.font.dongle_bold))
                            )
                        },
                        placeholder = { Text(text = labelText) },
                        modifier = Modifier
                            .clickable {
                                labelText = "Search location"
                            }
                            .fillMaxWidth()
                            .padding(4.dp),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
                if (showPredictionBoxForSearch) {
                    Box(
                        modifier = Modifier
                            .zIndex(2f)
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(start = 20.dp, end = 20.dp)
                            .border(1.dp, color = Color.LightGray)

                    ) {
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
                                            showPredictionBoxForSearch = false
                                        }
                                )
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(visible = scrollBehavior.state.overlappedFraction == 0f) {
                    Row(
                        modifier = Modifier
                            .zIndex(0f)
                            .fillMaxWidth()
                            .height(40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "Last 1 Week ")
                        Image(painter = painterResource(id = R.drawable.edit_new), contentDescription ="",modifier=Modifier.size(30.dp), colorFilter = ColorFilter.tint(
                            Color.Black) )
                        Card(modifier = Modifier
                            .clickable {
                                scope.launch {
                                   eventsViewModel.loadDroppedProfiles()
                                }
                            }.width(80.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = floatingActionBtnColor)
                        ) {
                            Text(text = "Search",color= Color.White , fontSize = 20.sp,fontFamily = FontFamily(Font(R.font.dongle_bold)), modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)

                        }
                       // DateRangePicker(newDate = "", onDateChange ={} )
                    }
                }

            if(droppedProfiles==null && !triggerFetch){
                Card(modifier = Modifier
                    .padding(start = 4.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(), shape = RoundedCornerShape(6.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "Greater Noida", fontFamily = FontFamily.Serif, modifier = Modifier
                            .wrapContentSize()
                            .padding(4.dp),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold, color = Color.Black
                    )

                }

                LazyRow(modifier = Modifier.padding(start = 4.dp)){
                    items(com.example.finalapp.utils.testdata.items){item->
                        LazyRowItem(item)

                    }
                }
            }else{
                val droppedProfiles = droppedProfiles!!.collectAsLazyPagingItems()
                Column(
                    modifier = Modifier
                        .zIndex(0f)
                        .fillMaxSize()
                ) {
                    LazyVerticalStaggeredGrid(
                        modifier = Modifier
                            .zIndex(0f)
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(2.dp),
                    ) {
                        items(droppedProfiles.itemCount) { index ->
                            val item = droppedProfiles[index]
                            if (item != null) {
                                DroppedProfile(item){
                                    Log.d("DropProfileTesting", "DroppedProfilesNew:callback called ")
                                    Log.d("DropProfileTesting", "DroppedProfilesNew:${item} ")
                                    try {
                                        val route= item.let {
                                            SCREENS.DROP_PROFILE_USER_PROFILE.passProfile(it)
                                        }
                                        Log.d("DropProfileTesting", "DroppedProfilesNew:$route ")
                                        navController.navigate(route)
                                    }catch (e:Exception){
                                        Log.d("DropProfileTesting", "DroppedProfilesNew:${e.message} ")
                                    }


                                }
                            }
                        }
                        droppedProfiles.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item {
                                        DialogLoading()
                                    }
                                }

                                loadState.append is LoadState.Loading -> {
                                    item {
                                        DialogLoading()
                                    }
                                }

                                loadState.refresh is LoadState.Error -> {
                                    val error = (loadState.refresh as LoadState.Error).error
                                    item {
                                        Text(text = "Error: ${error.message}", color = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LazyRowItem(item: Item) {
    Box(modifier = Modifier
        .padding(end = 3.dp)
        .width(150.dp)
        .height(200.dp)
        .clip(shape = RoundedCornerShape(6.dp))) {
        GlideImage(model =item.imageUrl , contentDescription ="", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize() )
        Column(
            modifier = Modifier
                .padding(4.dp)
                .wrapContentSize()
                .align(Alignment.BottomStart)
        ) {
            Text(text = item.name, color = Color.White, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleSmall)
        }
        Column(
            modifier = Modifier
                .padding(4.dp)
                .wrapContentSize()
                .align(Alignment.TopStart)
        ) {
          //  Text(text = item.date, color = Color.White, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropProfileSearchedList(
    scrollBehavior: TopAppBarScrollBehavior,
    droppedProfiles: LazyPagingItems<DropProfileResponse>,
) {
    val droppedProfilesList = remember { droppedProfiles }
    Column(
        modifier = Modifier
            .zIndex(0f)
            .fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .zIndex(0f)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            columns = StaggeredGridCells.Fixed(2), // Ensure column count is defined
            contentPadding = PaddingValues(2.dp),
        ) {
            items(droppedProfilesList.itemCount) { index ->
                val item = droppedProfiles[index] // Access item safely
                if (item != null) {
                    DroppedProfile(item){

                    }
                }
            }
        }

    }
}
@Composable
fun DateRangePicker(newDate:String,onDateChange:(String)->Unit) {
    OutlinedTextField(
        value = newDate, 
        onValueChange ={onDateChange(it)}, 
        modifier= Modifier
            .fillMaxWidth(0.7f)
            .padding(end = 16.dp),
        shape = RoundedCornerShape(20.dp),
        trailingIcon = { Image(painter = painterResource(id = R.drawable.calendar), contentDescription ="" )},
        
        
    )
    
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DroppedProfile(profile: DropProfileResponse, onProfileClicked:()->Unit) {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.5f
    Card(modifier = Modifier
        .clickable {
            onProfileClicked()
        }
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
                model = imagePrefix+ profile.image, // Replace with your image resource
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop, // Crop to fill the space
                modifier = Modifier
                    .clickable {
                        Log.d("DropProfileTesting", "DroppedProfile: onProfileClicked() called")
                        onProfileClicked()
                    }
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
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    profile.createdBy?.let {
                        Text(
                            text = it.name,//profile.location,
                            modifier=Modifier.fillMaxWidth(0.8f),
                            maxLines=1,
                            overflow= TextOverflow.Ellipsis,
                            style = TextStyle(color = Color.White, fontSize = 18.sp)
                        )
                    }
                    Text(
                        text = profile.expirationTime +" hrs.",
                        modifier=Modifier.fillMaxWidth(1f),
                        maxLines=1,
                        overflow=TextOverflow.Ellipsis,
                        style = TextStyle(color = Color.White, fontSize = 10.sp)
                    )

                }

                profile.message?.let {
                    Text(
                        text = it,
                        style = TextStyle(color = Color.White, fontSize = 10.sp)
                    )
                }
            }
        }

    }

}