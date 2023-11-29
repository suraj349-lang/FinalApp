package com.example.finalapp.screens

import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.repository.AuthRepository
import com.example.finalapp.auth.repository.FirebaseRepository
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ProfileScreenUI(navController: NavHostController = NavHostController(LocalContext.current)) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val buttonsVisible = remember { mutableStateOf(false) }
    val profileImage=true;
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HomeTopBar("Profile", navController )},
        bottomBar = {
            BottomBar(navController = navController, state = buttonsVisible, modifier = Modifier.height(45.dp))
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(it).fillMaxSize().verticalScroll(rememberScrollState())
                    ,horizontalAlignment = Alignment.CenterHorizontally) {
                    ProfileIcon(profileImage)
                    ProfileName()
                    ProfileImages()
                    DateTypes()
                    ProfileBio()

                    LogOut(navController)
                }
        }
    }
}

@Composable
fun ProfileImages() {
    val width= LocalConfiguration.current.screenWidthDp.dp
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(4.dp)) {
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="" , contentScale = ContentScale.Crop)

        }
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(painter = painterResource(id = R.drawable.profile_image_2), contentDescription ="" , contentScale = ContentScale.Crop)


        }
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(painter = painterResource(id = R.drawable.profile_image_3), contentDescription ="" , contentScale = ContentScale.Crop)

        }
    }

}

var str="\uD83C\uDF1F About Me:\n" +
        "\n" +
        "Hey there! I'm Jiya, a 24-year-old actor who's passionate about movies and acting. By day, you'll find me 15 july, but by night, I'm sleepy bug.\n"

@Composable
fun ProfileBio() {
    Surface(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(), color = topAppBarTextColor) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "Bio : ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp)
            Text(text = str, maxLines = 100, overflow = TextOverflow.Ellipsis, color = statusAndTopAppBarColor)

        }

    }
}

@Composable
fun LogOut(navController:NavHostController) {
    val repository=FirebaseRepository();
    val auth=FirebaseAuth.getInstance();
    Surface(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
            .height(45.dp), color = topAppBarTextColor) {
        Row(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                repository.LogoutUser(auth);
                navController.navigate(SCREENS.LOGIN.route);
            }) {
            Text(text = "Log Out", color = statusAndTopAppBarColor, style = MaterialTheme.typography.bodyMedium)
        }

    }
}

@Composable
fun DateTypes() {
    Surface(
        Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .height(100.dp), color = topAppBarTextColor) {
        Column(modifier = Modifier.padding(top=4.dp)) {
            Text(text = "My Date Types :", color = statusAndTopAppBarColor, style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp)
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {

            }

        }

    }
}

@Composable
fun ProfileName() {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .height(45.dp)){
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Jiya Shankar,",
                style = MaterialTheme.typography.displayMedium,
                fontSize = 38.sp,
                color = statusAndTopAppBarColor)
            Text(
                text = "24",
                style = MaterialTheme.typography.displayMedium,
                fontSize = 38.sp,
                color = statusAndTopAppBarColor)
        }
    }
}

@Composable
fun ProfileIcon(profileImage:Boolean) {
    Surface(
        Modifier
            .size(120.dp)
            .padding(top = 8.dp), shape = CircleShape, color = topAppBarTextColor, shadowElevation = 12.dp) {
        if(!profileImage)
            Icon(painter = painterResource(id = R.drawable.baseline_person_24), contentDescription ="profile icon", modifier = Modifier.clip(
                CircleShape), tint = statusAndTopAppBarColor )
        else
            Image(painter = painterResource(id = R.drawable.girl), contentDescription ="" , contentScale = ContentScale.Crop)
    }
}

