package com.spint.app.screens._1home


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.spint.app.viewmodels.EventsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.model.DropProfileResponse
import com.spint.app.navigation.SCREENS
import com.spint.app.ui.imagePrefix
import com.spint.app.screens.common.NoProfilesFoundScreen
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.UserObject
import com.spint.app.utils.UserLocationObject
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.formatDateTime
import com.spint.app.utils.getFormattedTimeAndFlag
import com.spint.app.utils.testdata.Item
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
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
    var name by remember { mutableStateOf("") }
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
    val userLocation by UserLocationObject.userLocation.collectAsState()

    val predictions by eventsViewModel.getAutocompletePredictions(query).collectAsState(emptyList())
    val shouldLoadDroppedProfiles by eventsViewModel.shouldLoadDroppedProfiles.collectAsState()
    LaunchedEffect(pagerState.currentPage) {
        // if page is not checked then on scrolling it will make the api call i.e. in the direct screen itself
        if (pagerState.currentPage == 2 && !shouldLoadDroppedProfiles) {
            eventsViewModel.getDefaultDropProfiles(UserObject.user.value.address)
            eventsViewModel.resetShouldLoadDroppedProfiles()
        }
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 500)
    )
    var changeLocation by remember {
        mutableStateOf(false)
    }
    val gridState = rememberLazyStaggeredGridState()
    var previousScrollOffset by remember { mutableStateOf(0) }
    val isScrollingUp = remember {
        derivedStateOf {
            val currentOffset = gridState.firstVisibleItemScrollOffset
            val isUp = currentOffset < previousScrollOffset
            previousScrollOffset = currentOffset
            isUp
        }
    }


    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(), color = Color(0xFF140F01)//0xFF021930
    ) {
        Column(
            modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Column(
                    modifier=Modifier,
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(showLoader && droppedProfilesList?.itemCount==0) {
                        LinearProgressIndicator(modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp), color = floatingActionBtnColor)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0E0A00))//0xFF0064C9
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {

                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = "Dropped profiles here at :",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.White,
                                    fontFamily=Constants.FONT_MEDIUM,
                                    modifier = Modifier.alpha(animatedAlpha)
                                )
                                Row(modifier = Modifier.clickable { changeLocation = !changeLocation }, horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Image(painter = painterResource(id = R.drawable.edit_new), contentDescription ="", modifier = Modifier
                                        .size(12.dp), colorFilter = ColorFilter.tint(Color.White) )
                                    Text(
                                        text = "Search location",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 10.sp,
                                        color = Color.White,
                                        modifier = Modifier.alpha(animatedAlpha),
                                        style = TextStyle(textDecoration = TextDecoration.Underline)
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            userLocation.address?.let {
                                DroppedProfileLocation(trim = true, backgroundColor = Color.Transparent,location = it)
                            }
                        }
                    }
                    if(changeLocation) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                            },
                            label = {
                                Text(
                                    text = "Search by name",
                                    fontSize = 14.sp,
                                    color = Color.LightGray,
                                    fontFamily = Constants.FONT_MEDIUM
                                )
                            },
//                            trailingIcon = {
//                                Text(
//                                    text = "Search",
//                                    fontSize = 16.sp,
//                                    fontFamily = Constants.FONT_MEDIUM,
//                                    color = Color.LightGray,
//                                    modifier = Modifier
//                                        .padding(end = 8.dp)
//                                        .clickable {
//                                            scope.launch {
//                                                eventsViewModel.getDefaultDropProfiles("")
//                                            }
//                                        })
//                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Gray,
                                unfocusedContainerColor = Color.Gray
                            )
                        )

                        OutlinedTextField(
                            value = query,
                            onValueChange = {
                                query = it
                                showPredictionBoxForSearch = it.isNotEmpty()
                            },
                            label = {
                                Text(
                                    text = "Enter location",
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    fontFamily = Constants.FONT_MEDIUM
                                )
                            },
//                            trailingIcon = {
//                                Text(
//                                    text = "Search",
//                                    fontSize = 16.sp,
//                                    fontFamily = Constants.FONT_MEDIUM,
//                                    color = Color.LightGray,
//                                    modifier = Modifier
//                                        .padding(end = 8.dp)
//                                        .clickable {
//                                            scope.launch {
//                                                eventsViewModel.getDefaultDropProfiles("")
//                                            }
//                                        })
//                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Gray,
                                unfocusedContainerColor = Color.Gray
                            )
                        )
                        if (showPredictionBoxForSearch) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(horizontal = 8.dp)
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

                        Button(
                            onClick = { scope.launch { eventsViewModel.getDefaultDropProfiles("") } },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Constants.HOME_TOP_BAR_TITLE_COLOR)) {
                            Text(text = "Search", fontFamily = Constants.FONT_LIGHT, fontSize = 14.sp, color = Color.White)

                        }
                        Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color.Gray)
                    }
            }

            if(droppedProfiles==null && !triggerFetch){
                LazyRow(modifier = Modifier.padding(start = 4.dp)){
                    items(com.spint.app.utils.testdata.items){ item->
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
                           // .nestedScroll(scrollBehavior.nestedScrollConnection),
                       , columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(2.dp),
                        verticalItemSpacing = 3.dp,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        droppedProfilesList?.itemCount?.let {
                            items(it) { index ->
                                val item = droppedProfilesList[index]
                                if (item != null) {
                                    DroppedProfileItem(item){
                                        try {
                                            val route= item.let {
                                                SCREENS.DROP_PROFILE_USER_PROFILE.createRoute(it)
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
                                            NoProfilesFoundScreen(error = "Error getting profiles.",) {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DroppedProfileItem(profile: DropProfileResponse, onProfileClicked:()->Unit) {
    Column(modifier = Modifier
        .wrapContentSize()
        .padding(vertical = 8.dp)) {
        Box(
            modifier = Modifier
                .zIndex(4f)
                .clickable { onProfileClicked() }
                .padding(2.dp)
                .fillMaxWidth()
                .height(300.dp)
                .clip(shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                .background(color = Color.LightGray))
        {
            AsyncImage(model = imagePrefix + profile.image, // Replace with your image resource
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop, // Crop to fill the space
                filterQuality = FilterQuality.High,
                modifier = Modifier
                    .clickable {
                        onProfileClicked()
                    }
                    .fillMaxSize())
            Text(
                text = getFormattedTimeAndFlag(profile.expirationTime).toString() + " hrs left",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp)
                    .shadow(elevation = 20.dp, spotColor = Color.White),
                fontFamily = Constants.FONT_MEDIUM,
                style = TextStyle(color = Color.White, fontSize = 9.sp)
            )



        }
        Box(modifier = Modifier.wrapContentSize()){
            // Multiple texts
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomCenter)
                    .background(color = Constants.HOME_TOP_BAR_COLOR) //0xFF2C2A2A  0xFFAFB42B  0xFF290438
                    .clip(shape = RoundedCornerShape(10.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 4.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = profile.createdBy.name ?: "",
                            modifier = Modifier
                               // .shadow(elevation = 10.dp, spotColor = Color.White)
                                .fillMaxWidth(0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = Constants.FONT_MEDIUM,
                            style = TextStyle(color = Color.White, fontSize = 18.sp)
                        )
                        Text(
                            text = formatDateTime(profile.createdAt ?: ""),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = Constants.FONT_LIGHT,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier
                              //  .shadow(elevation = 60.dp)
                                .zIndex(2f),
                            color = Color.White
                        )


                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                           // .shadow(elevation = 2.dp, spotColor = Color.White),
                       , colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.location_new),
//                            contentDescription = "",
//                            modifier = Modifier.size(16.dp),
//                            colorFilter = ColorFilter.lighting(
//                                multiply = Color.White,
//                                add = Color.Black
//                            )
//                        )
                            Text(
                                text = profile.location,
                                fontFamily = Constants.ROBOTO_CONDENSED,
                                fontSize = 10.sp,
                                maxLines = 1,
                                color = Color.LightGray,
                                overflow = TextOverflow.Ellipsis
                            )

                        }
                    }
                }
            }
        }

    }
}


@Composable
fun DroppedProfileLocation(location: String, backgroundColor:Color= Color(0xFF077CDA), trim: Boolean=false) {
    val textWidth = remember { mutableStateOf(0f) }
    val containerWidth = remember { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(if (trim) RoundedCornerShape(12.dp) else RectangleShape)
            .background(backgroundColor.copy(alpha = 0.9f))
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
                fontFamily=Constants.FONT_EXTRA_LIGHT,
                fontSize = 13.sp,
            )
        }
    }
}

