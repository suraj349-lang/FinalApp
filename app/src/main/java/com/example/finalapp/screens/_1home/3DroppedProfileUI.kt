package com.example.finalapp.screens._1home


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.viewmodels.EventsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.screens.common.CommonErrorScreen
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.UserLocation
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.testdata.Item
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DroppedProfilesUI(
    pagerState: PagerState,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavHostController,
    eventsViewModel: EventsViewModel,
) {
    val triggerFetch by eventsViewModel.triggerFetch.collectAsState()
    val droppedProfiles by eventsViewModel.droppedProfiles.collectAsState()
    val droppedProfilesList = droppedProfiles?.collectAsLazyPagingItems()
    var showLoader by remember {
        mutableStateOf(false)
    }
    var query by remember { mutableStateOf("") }
    var showPredictionBoxForSearch by remember {
        mutableStateOf(false)
    }
    var showDateDialog by remember {
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
    val shouldLoadDroppedProfiles by eventsViewModel.shouldLoadDroppedProfiles.collectAsState()
    LaunchedEffect(pagerState.currentPage) {
        // if page is not checked then on scrolling it will make the api call i.e. in the direct screen itself
        if (pagerState.currentPage == 2 && !shouldLoadDroppedProfiles) {
            eventsViewModel.getDefaultDropProfiles(ProfileObject.profile?.address!!)
            eventsViewModel.resetShouldLoadDroppedProfiles()
        }
    }

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding()) {
        Column(
            modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = scrollBehavior.state.overlappedFraction == 0f) {
                Column(
                    modifier=Modifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(showLoader && droppedProfilesList?.itemCount==0) LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(8.dp), color = floatingActionBtnColor)
                    UserLocation.address?.let { DroppedProfileLocation(location = it) }
                    OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        showPredictionBoxForSearch = it.isNotEmpty()
                    },
                    label = {
                        Text(
                            text = "Enter location...",
                            fontSize=20.sp,
                            color=Color.LightGray,
                            fontFamily = FontFamily(Font(R.font.dongle_bold))
                        )
                    },
                    trailingIcon = {
                                   Text(
                                       text = "Search",
                                       fontSize = 24.sp,
                                       fontFamily = DONGLE_BOLD,
                                       color=Color.DarkGray,
                                       modifier = Modifier
                                           .padding(end = 8.dp)
                                           .clickable {
                                               scope.launch {
                                                   eventsViewModel.getDefaultDropProfiles("")
                                               }
                                           })
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
                if (showPredictionBoxForSearch) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(start = 20.dp, end = 20.dp)
                            .border(1.dp, color = Color.LightGray)

                    ) {
                        LazyColumn(modifier = Modifier) {
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
        }
            if(droppedProfiles==null && !triggerFetch){
                LazyRow(modifier = Modifier.padding(start = 4.dp)){
                    items(com.example.finalapp.utils.testdata.items){item->
                        LazyRowItem(item)
                    }
                }
            }else{
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
                        droppedProfilesList?.itemCount?.let {
                            items(it) { index ->
                                val item = droppedProfilesList[index]
                                if (item != null) {
                                    DroppedProfileItem(item){
                                        try {
                                            val route= item.let {
                                                SCREENS.DROP_PROFILE_USER_PROFILE.passProfile(it)
                                            }
                                            navController.navigate(route)
                                        }catch (e:Exception){
                                            Log.d("DropProfileTesting", "DroppedProfilesNew:${e.message} ")
                                        }


                                    }
                                }
                            }
                        }
                        droppedProfilesList?.apply {
                            when {
                                loadState.refresh is LoadState.Loading -> {
                                    item {
                                        showLoader=true
                                    }
                                }

                                loadState.append is LoadState.Loading -> {
                                    item {
                                        showLoader=true
                                    }
                                }

                                loadState.refresh is LoadState.Error -> {
                                    showLoader=false
                                    val error = (loadState.refresh as LoadState.Error).error
                                    item {
                                        Log.e("Error in dropped profiles", "DroppedProfilesUI: $error ", )
                                        CommonErrorScreen(error = "Error getting profiles.",true){
                                            eventsViewModel.getDefaultDropProfiles("")
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
                    DroppedProfileItem(item){

                    }
                }
            }
        }

    }
}
@Composable
fun DateRangePicker(newDate:String,onDateChange:(String)->Unit,onDismiss:()->Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = false)
    ) {

    }
    
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DroppedProfileItem(profile: DropProfileResponse, onProfileClicked:()->Unit) {
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


@Composable
fun DroppedProfileLocation(location: String, trim: Boolean=false) {
    val textWidth = remember { mutableStateOf(0f) }
    val containerWidth = remember { mutableStateOf(0f) }

    val offsetX = remember { Animatable(0f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(if (trim) RoundedCornerShape(12.dp) else RectangleShape)
            .background(Color(0xFF077CDA))
            .onGloballyPositioned { coordinates ->
                containerWidth.value = coordinates.size.width.toFloat()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row (modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(R.drawable.location_new),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp)
                    .padding(horizontal = 4.dp)
            )
            Text(
                text = location,
                overflow =TextOverflow.Ellipsis,
                color = Color.White,
                modifier = Modifier,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
            )
        }
    }
}

