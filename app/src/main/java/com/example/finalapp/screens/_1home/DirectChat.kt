package com.example.finalapp.screens._1home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.viewmodels.EventsViewModel

@Composable
fun DirectChat(viewModel: EventsViewModel,navController: NavHostController) {
    var checked by remember {
        mutableStateOf(false)
    }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Main UI content
                DirectChatUI(paddingValues,checked){
                    checked=!checked
                    // TODO call the api to share the profile
                }

                // Floating Action Button at Top-End
                FloatingActionButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(80.dp)
                        .padding(4.dp) // Add padding from edges,
                ,
                    containerColor = Color.Transparent, // Set transparent background
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)

                ) {
                    Column(modifier = Modifier
                        .padding(4.dp)
                        .fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                        SwitchWithIcon(checked) { checked = it }
                        Text(text = "Direct chat", fontSize = 8.sp, fontFamily = FontFamily(Font(R.font.oreganoregular)))
                    }
                }
            }
        }
    )
}

@Composable
fun DirectChatUI(paddingValues: PaddingValues, checked: Boolean,onShareProfileClicked: () -> Unit) {

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        Column(modifier = Modifier
            .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if(!checked) {
                Text(
                    text = "Share your profile nearby",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.oreganoregular)),
                    color = Color.DarkGray
                )
                //if user want to share the profile , it will automatically switch on the button for direct chat
                ShareProfileForDirectChat(){onShareProfileClicked()}
            } else{
                DirectChatProfiles()
            }

            
        }
        
    }
    
}

@Composable
fun DirectChatProfiles() {
    val profileImage= listOf(R.drawable.profile_image_1,R.drawable.profile_image_2,R.drawable.profile_image_3)
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            LazyColumn{
                items(profileImage){
                    DirectChatItem(it){}

                }
            }
        }
    }
}

@Composable
fun DirectChatItem(image: Int,onDirectChatItemClicked:()->Unit) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Divider(color = Color.LightGray, thickness = 0.5.dp)
                Card(modifier = Modifier
                    .size(150.dp)
                    .padding(top = 4.dp),shape= CircleShape, border = BorderStroke(width = 1.dp, color = Color.LightGray)) {
                    Image(painter = painterResource(id = image), contentDescription = "", contentScale = ContentScale.Crop)
                }
                Row(modifier =Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically ) {
                    Text(text = "Marilyn Munroe", fontSize = 25.sp, fontFamily = FontFamily(Font(R.font.oreganoregular)))
                    Text(text = " ,100m.", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.oreganoregular)))
                }

                Button(onClick = { onDirectChatItemClicked()}, shape = RoundedCornerShape(6.dp),modifier = Modifier
                    .fillMaxWidth(0.5f) //.wrapContentHeight().fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.White)) {
                    Text(text = "Send Message", color = Color.Black)
                }

            }
        }
}



@Composable
fun SwitchWithIcon(checked: Boolean,onClick:(value:Boolean)->Unit) {


        Switch(
            checked = checked,
            onCheckedChange = {
                onClick(it)
            },
            modifier = Modifier.padding(0.dp),
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(SwitchDefaults.IconSize)
                            .padding(0.dp),
                        tint = Color.White
                    )
                }
            } else {
                null
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF022E04),// MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        )


}

@Composable
fun ShareProfileForDirectChat(onShareProfileClicked:()->Unit) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(1f)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Card(modifier = Modifier.size(150.dp), shape = CircleShape, border = BorderStroke(width = 1.dp, color = Color.LightGray)) {
                Image(painter = painterResource(id = R.drawable.profile_image_3), contentDescription = "", contentScale = ContentScale.Crop)
            }
            Text(text = "Marilyn Munroe", fontSize = 30.sp, fontFamily = FontFamily(Font(R.font.oreganoregular)))
            Text(text = "Your profile will be active on this location for 30 minutes", fontSize = 8.sp,  color = Color.Gray,fontFamily = FontFamily(Font(R.font.oreganoregular)))
            
            Button(onClick = { onShareProfileClicked()}, shape = RoundedCornerShape(6.dp),modifier = Modifier
                .fillMaxWidth(0.8f) //.wrapContentHeight().fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally),colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)) {
                Image(painter = painterResource(id = R.drawable.up_arrow), contentDescription = "", modifier = Modifier.size(40.dp))
                Text(text = "Share Profile")
                Image(painter = painterResource(id = R.drawable.up_arrow), contentDescription = "", modifier = Modifier.size(40.dp))
                
            }
            Text(text = "Sharing your profile will switch on direct chat", fontSize = 8.sp, color = Color.Gray,fontFamily = FontFamily(Font(R.font.oreganoregular)))

            
        }

    }

}