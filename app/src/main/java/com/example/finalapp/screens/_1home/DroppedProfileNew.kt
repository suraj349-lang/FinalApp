package com.example.finalapp.screens._1home


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.utils.RequestState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileModel
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
    val droppedProfiles by remember {
        mutableStateOf(eventsViewModel.droppedProfilesList)
    }
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
                Row(modifier = Modifier.fillMaxWidth().height(60.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
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
                                    eventsViewModel.getAllDropProfiles()
                                }
                            },
                            shape = CircleShape
                        ) {
                            Text(text = "Search",color= Color.White , fontSize = 20.sp,fontFamily = FontFamily(Font(R.font.dongle_bold)), modifier = Modifier.padding(4.dp))

                        }
                       // DateRangePicker(newDate = "", onDateChange ={} )
                    }
                }
            when (val result=eventsViewModel.getDropProfileResponse.value){
                is RequestState.Success->{
                    eventsViewModel.droppedProfilesList.value=result.data
                    Log.d("Suraj", "image uri in main screen ${droppedProfiles.value} ")

                    DropProfileSearchedList(scrollBehavior,droppedProfiles)

                }
                is RequestState.Error->{
                    Log.d("Data received",result.error.message.toString())
                    Toast.makeText(context,"$result", Toast.LENGTH_SHORT).show()
                }
                RequestState.Loading->{
                    CircularProgressIndicator(color = Color(0xFF1289BE))
                }
                RequestState.Idle->{

                }

            }
            if(droppedProfiles.value.isEmpty()){
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
    droppedProfiles: MutableState<List<DropProfileModel>>,
) {
    Column(modifier = Modifier
        .zIndex(0f)
        .fillMaxSize()) {
            LazyVerticalGrid(
                modifier= Modifier
                    .zIndex(0f)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(2.dp),

            ) {
                items(droppedProfiles.value) { profile ->
                    DroppedProfile(profile = profile)
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

