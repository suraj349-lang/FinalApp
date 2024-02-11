package com.example.finalapp.screens


import BottomBar
import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.offer.OfferViewModel
import com.example.finalapp.screens.DialogBOX.CustomAlertDialog
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.floatingActionBtnTextColor
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.utils.RequestState


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun HomeScreenUI(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val buttonsVisible = remember { mutableStateOf(true) }
    val offerViewModel= hiltViewModel<OfferViewModel>()
    val notificationLauncher=
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission() ){

    }
    LaunchedEffect(true ){
        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

   



    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
             topBar = { HomeTopBar(
                 "Frisbee",
                     navController,
                     true ,
                     R.drawable.send_24)
                      },
             bottomBar = { BottomBar(
                navController = navController,
                state = buttonsVisible,
                modifier = Modifier.height(45.dp))
             },
             floatingActionButton = {
                 HomeFloatingActionButton(offerViewModel , navController );
             }
            ){
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(it)
                    .height(5000.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ImageScreen()
                HomeScreenOffer()
                ProfileData()

            }
        }
    }

@Composable
fun HomeFloatingActionButton(offerViewModel: OfferViewModel,navController: NavHostController  ) {
    val context = LocalContext.current
    var showCustomDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showCustomDialog = !showCustomDialog},
        Modifier.size(75.dp),
        shape= CircleShape,
        containerColor = statusAndTopAppBarColor, //0xFFEBDB55
        contentColor = floatingActionBtnTextColor,//0xFF090200
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painterResource(id = R.drawable.up_arrow), contentDescription = "Add",
                Modifier
                    .padding(top = 4.dp)
                    .size(32.dp))
            Text(text = "Raise Offer", fontSize = 12.sp, modifier = Modifier.padding(top=0.dp))
        }
    }
    if (showCustomDialog) { CustomAlertDialog(offerViewModel , navController ) { showCustomDialog = !showCustomDialog } }
}

@Composable
fun ProfileData() {
    Box(modifier = Modifier
        .padding(6.dp)
        .fillMaxWidth()
        .heightIn(min = 100.dp, max = 300.dp)
        .background(brush = Brush.linearGradient(colors = listOf(Color(0xFFE8E9E2), Color.White))))
    {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "INTERESTS: ", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
            {
               Text(text = "Party" + " " + " Clubbing" + " " + "Dancing", style = MaterialTheme.typography.bodySmall , color = DarkBlue, fontSize = 24.sp, modifier = Modifier.padding(start = 4.dp) )
            }
            Text(text = "D.O.B : ", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))
            {
                Text(text = "15-07-1999", style = MaterialTheme.typography.bodySmall , color = DarkBlue, fontSize = 24.sp, modifier = Modifier.padding(start = 4.dp) )
            }

        }


    }

}

@Composable
fun HomeScreenOffer(){
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.35f
    Box(modifier = Modifier
        .padding(6.dp)
        .fillMaxWidth()
        .heightIn(min = 100.dp, max = heightInDp)
        ) {
        Column (verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start){
            Row(modifier= Modifier
                .fillMaxWidth()
                .heightIn(200.dp)
                .background(
                    brush = Brush.linearGradient(colors = listOf(Color(0xFFE8E9E2), Color.White))
                )) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp)) {
                    Text(text = "OFFER", style = MaterialTheme.typography.bodyMedium, color = Color(
                        0xFF05000C
                    )
                    )
                    Text(
                        text = "Dedicate me a song and we ll have coffee",
                        modifier = Modifier.padding(start = 4.dp),
                        maxLines = 4,
                        overflow = TextOverflow.Visible,
                        style = MaterialTheme.typography.titleMedium, 
                        color = Color(0xFFE40B54)
                    )
//                    Row(modifier = Modifier
//                        .fillMaxWidth()
//                        .height(24.dp)) {
//
//                        Icon(painter = painterResource(id = R.drawable.food_24), contentDescription ="Insta icon", modifier = Modifier.size(18.dp) )
//                        Spacer(modifier = Modifier.width(2.dp))
//                        Text(text = "Insta Handle :", style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp, modifier = Modifier.padding(top=4.dp))
//                        Spacer(modifier = Modifier.width(2.dp))
//                        Text(text ="suraj__singh94", style = MaterialTheme.typography.bodyMedium, fontSize = 14.sp,color = DarkBlue,modifier = Modifier.padding(top=4.dp))
//
//                    }
                    Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF063608))) {
                        Text(text = "Accept", style = MaterialTheme.typography.bodySmall, color = Color.White)
                        
                        
                    }

                }
              

            }

        }


    }
}

//0xFFEEF3B9 -> yellow   0xFFE8E9E2  -> grey  

@Composable
fun ImageScreen() {
    val configuration = LocalConfiguration.current
    val widthInDp = configuration.screenWidthDp.dp
    val heightInDp = configuration.screenHeightDp.dp * 0.58f
   Box(modifier = Modifier
       .background(color = Color.Transparent, shape = RoundedCornerShape(6.dp))
       .padding(top = 2.dp, bottom = 2.dp, start = 2.dp, end = 2.dp)
       .fillMaxWidth()
       .height(heightInDp), contentAlignment = Alignment.BottomStart,)
   {
       Image(painter = painterResource(id = R.drawable.girl),
           contentDescription ="" ,
           modifier = Modifier
               .fillMaxWidth()
               .height(heightInDp)
               .clip(shape = RoundedCornerShape(12.dp)),
           contentScale = ContentScale.Crop
       )
       Text(text = "Kiran JayShankar", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

   }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(title:String,navController: NavHostController,actionIcon:Boolean,icon:Int){


    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = statusAndTopAppBarColor
        ),
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top=8.dp), color = Color(0xFFE8E9E2), style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_bakery_dining_24
                ),
                tint = Color.White,
                contentDescription ="" ,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(40.dp))
        }, actions = {
            if (actionIcon) {
                Icon(painter = painterResource(id = icon),
                    contentDescription = "",
                    tint = Color(0xFFE8E9E2),
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(-40f)
                        .shadow(elevation = 12.dp, shape = CircleShape, spotColor = Color.White)
                        .clickable {
                            navController.navigate(SCREENS.CHAT.route) {
                                popUpTo(SCREENS.CHAT.route)
                            }
                        })
            }
        }
    )
}



@Composable
fun OfferResponseDataAndAction(offerViewModel: OfferViewModel,navController: NavHostController){
    val context= LocalContext.current
    Log.d("Data received","Into the function")
    when (val result=offerViewModel.offerResponse.value){
        is RequestState.Success->{
            offerViewModel.key.value=0;
            Log.d("Suraj",result.data.toString())
            Toast.makeText(context,"${result.data}", Toast.LENGTH_SHORT).show()
            navController.navigate(SCREENS.HOME.route){
                popUpTo(0)
            }

        }
        is RequestState.Error->{
            Log.d("Data received","error final found")
            Toast.makeText(context,"$result", Toast.LENGTH_SHORT).show()
        }
        RequestState.Loading->{
            CircularProgressIndicator(color = Color(0xFF1289BE))
        }
        RequestState.Idle->{

        }

    }


}


