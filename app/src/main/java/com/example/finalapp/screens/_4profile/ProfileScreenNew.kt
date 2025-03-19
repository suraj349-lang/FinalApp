package com.example.finalapp.screens._4profile

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.database.Profile
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.DialogError
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.screens.dialogBox.DropProfileDialog
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.viewmodels.EventsViewModel
import com.example.finalapp.viewmodels.ImageUploadViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreenNew(navController: NavHostController,authViewModel:AuthViewModel,eventsViewModel:EventsViewModel,imageUploadViewModel:ImageUploadViewModel) {
    var showCustomDialog by remember {
        mutableStateOf(false)
    }
    if (showCustomDialog) {
        DropProfileDialog(authViewModel ,eventsViewModel , imageUploadViewModel ,navController ) { showCustomDialog = !showCustomDialog }
        Log.d("Suraj", "Profile Screen : Drop Profile ")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = floatingActionBtnColor) //Color(0xFFE4EE05)
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, Color(0xFF1B1A1A)
                            ), startY = 0f, endY = 600f
                        )
                    )) {
                    Card(modifier = Modifier
                        .padding(4.dp)
                        .size(25.dp)
                        .clickable { navController.navigateUp() }
                        .align(Alignment.TopStart),
                        backgroundColor = Color.Black.copy(alpha = 0.2f),
                        shape = CircleShape) {
                        Image(
                            painterResource(id = R.drawable.back),
                            contentDescription = "",
                            contentScale = ContentScale.Crop, colorFilter = ColorFilter.tint(Color.White)
                        )

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Card(
                                    modifier = Modifier.size(60.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, color = Color.Black),
                                    elevation = 20.dp
                                ) {
                                    GlideImage(
                                        model= ProfileObject.profile?.profileImage,
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )


                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    ProfileObject.profile?.let {
                                        Text(
                                            text = it.name,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 18.sp
                                        )
                                    }
                                    ProfileObject.profile?.let {
                                        Text(
                                            text = it.username,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            fontSize = 10.sp
                                        )
                                    }

                                }

                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()) {
                                Card(modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(30.dp), backgroundColor = Color(0xFFFFFFFF).copy(alpha = 0.2f),border = BorderStroke(width = 2.dp, color = Color.White),shape = RoundedCornerShape(30.dp)) {
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "My account", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Card(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .height(30.dp), backgroundColor = Color.White.copy(alpha = 0.5f),shape = RoundedCornerShape(30.dp)) {
                                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Public Profile", color = Color.White, fontWeight = FontWeight.Bold)
                                    }

                                }
                                
                            }


                        }

                    }

            }
            }
            Column(modifier = Modifier.padding(start = 16.dp,end=16.dp,top=10.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                MyLiveEvents(){
                    navController.navigate(SCREENS.CREATE_EVENT.route)
                }
                RecentDrops(){
                    showCustomDialog=!showCustomDialog
                }
                UserStats()
            }

        }

    }

}




@Composable
fun MyLiveEvents(onAddEventClicked:()->Unit) {
    Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = "My Live Events", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        LazyRow{
            items(items){item->
                LiveEventItem(item)

            }
        }
        Card(modifier = Modifier
            .clickable { onAddEventClicked() }
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(40.dp), border = BorderStroke(width = 1.dp, brush = Brush.linearGradient(colors = listOf(
            Color(0xFFF7B206), Color(0xFF540575)
        )))
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Image(painterResource(id = R.drawable.add), contentDescription = "", colorFilter = ColorFilter.tint(Color(
                    0xFF033669
                )
                ),modifier = Modifier.size(30.dp))
                Text(text = "Create new Event", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                
            }
            
        }
    }


}

@Composable
fun LiveEventItem(item: LiveEventItem) {
    Box(modifier = Modifier
        .size(180.dp) //120 earlier
        .padding(end = 8.dp, top = 8.dp)
        .clip(shape = RoundedCornerShape(6.dp))) {
        Image(painterResource(id = item.image), contentDescription = "", contentScale = ContentScale.Crop)
        Row(modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(4.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = item.title, color = Color.White, fontWeight = FontWeight.SemiBold)
            Card(shape = CircleShape,backgroundColor = Color.Black.copy(alpha = 0.4f)) {
                Text(text=item.timeLeft.toString(), color = Color.White, modifier = Modifier.padding(2.dp))
            }

            
        }


        
    }
}

data class LiveEventItem(
    val image:Int,
    val title:String,
    val timeLeft:Int
)
val items= listOf(
    LiveEventItem(R.drawable.profile_image_1,"Clubbing",4),
    LiveEventItem(R.drawable.profile_image_2,"Trek",6),
    LiveEventItem(R.drawable.profile_image_3,"Party",8),
    LiveEventItem(R.drawable.femaleprofile,"Protest",12),
    LiveEventItem(R.drawable.profile_image_1,"Celebration",4),
    LiveEventItem(R.drawable.profile_image_2,"Results",6),
    LiveEventItem(R.drawable.profile_image_3,"Mobbing",8),
    LiveEventItem(R.drawable.femaleprofile,"News",12)
)
@Composable
fun RecentDrops(onDropProfileClicked:()->Unit) {
    Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Recent Profile Drops", color = Color.Black, fontWeight = FontWeight.Bold)
                    Text(text = "upto 1 week", color = Color.DarkGray, fontWeight = FontWeight.SemiBold, fontSize = 8.sp)

                }
                Image(painter = painterResource(id = R.drawable.showall), contentDescription ="", colorFilter = ColorFilter.tint(
                    Color(0xFF05407A)
                ) , modifier = Modifier.size(20.dp))
            }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp), color = Color.LightGray)
        LazyRow(modifier = Modifier.padding(top =8.dp)){
            items(itemsForRecentProfileDrop){item->
                RecentProfileDropItem(item)

            }
        }
        Card(modifier = Modifier
            .clickable { onDropProfileClicked() }
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(40.dp), border = BorderStroke(width = 1.dp, brush = Brush.linearGradient(colors = listOf(
            Color(0xFFF7B206), Color(0xFF540575)
        )))
        ) {
            Row(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Image(painterResource(id = R.drawable.drop_profile_filled_rounded), contentDescription = "", colorFilter = ColorFilter.tint(Color(
                    0xFF033669
                )
                ),modifier = Modifier.size(30.dp))
                Text(text = "Drop your profile", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

            }

        }


    }

}

@Composable
fun RecentProfileDropItem(item: RecentProfileDrop) {
        Box(
            modifier = Modifier
                .size(100.dp) //120 earlier
                .padding(end = 8.dp)
                .clip(shape = RoundedCornerShape(6.dp))
        ) {
            Column(modifier = Modifier.background(brush = Brush.verticalGradient(colors = listOf(Color(
                0xFF4D056B
            ),
                Color(0xFF9E0642)
            )))) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp),backgroundColor = Color(
                    0xFFF1EDE3
                )
                ) {
                    Text(text = item.location, fontWeight = FontWeight.SemiBold,color = Color(0xFF072747),
                        modifier = Modifier.padding(start=4.dp,top=2.dp), fontSize = 8.sp)
                }
                Image(
                    painterResource(id = item.image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

        }

    }
}
data class RecentProfileDrop(
    val image:Int,
    val location:String,
    val timeLeft:Int
)
val itemsForRecentProfileDrop= listOf(
    RecentProfileDrop(R.drawable.profile_image_3,"Delhi",4),
    RecentProfileDrop(R.drawable.profile_image_2,"C.P.",6),
    RecentProfileDrop(R.drawable.profile_image_1,"Noida",8),
    RecentProfileDrop(R.drawable.girl,"Greater noida",12),
    RecentProfileDrop(R.drawable.profile_image_1,"Celebration",4),
    RecentProfileDrop(R.drawable.profile_image_2,"Results",6),
    RecentProfileDrop(R.drawable.profile_image_3,"Mobbing",8),
    RecentProfileDrop(R.drawable.femaleprofile,"News",12)
)

@Composable
fun UserStats() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Your Stats", fontWeight = FontWeight.Bold,color= Color.Black, fontSize = 16.sp)
            
        }
        
    }
}