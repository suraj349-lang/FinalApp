package com.example.finalapp.screens

import BottomBar
import android.annotation.SuppressLint
import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.repository.FirebaseRepository
import com.example.finalapp.model.SignupAPIResponse
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.DialogBOX.DialogBoxForImageEdit
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ProfileScreenUI(navController: NavHostController ) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val buttonsVisible = remember { mutableStateOf(false) }
    val profileImage=true;

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { HomeTopBar("Profile", navController,true,R.drawable.settings ) },
        bottomBar = {
            BottomBar(navController = navController, state = buttonsVisible, modifier = Modifier.height(45.dp))
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    ,horizontalAlignment = Alignment.CenterHorizontally) {
                    ProfileIcon(profileImage)
                    ProfileName()
                    ProfileImages()
                    EditableProfileScreen()
                    FrisbeeInfo()
                    PersonalInfo()
                    ProfileBio()

                    LogOut(navController)
                }
        }
    }
}

@Composable
fun EditableProfileScreen() {

}

@Composable
fun ProfileImages() {
    val width= LocalConfiguration.current.screenWidthDp.dp
    var showCustomDialog by remember { mutableStateOf(false) }
    var key by remember { mutableStateOf(-1) }
    var image:Int by remember { mutableStateOf(R.drawable.profile_image_1) }
    if(key==1) image=R.drawable.profile_image_1
    if(key==2) image=R.drawable.profile_image_2
    if(key==3) image=R.drawable.profile_image_3
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(4.dp)) {
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="" , contentScale = ContentScale.Crop, modifier = Modifier.clickable {
                key=1;
                showCustomDialog=!showCustomDialog
                key=-1;
            })

        }
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(
                painter = painterResource(id = R.drawable.profile_image_2),
                contentDescription ="" , contentScale = ContentScale.Crop,
                modifier = Modifier.clickable {
                    key=2;
                    showCustomDialog = !showCustomDialog
                    key=-1;
                })
        }
        Surface(modifier = Modifier
            .fillMaxHeight()
            .width(width / 3)
            .padding(2.dp), color = topAppBarTextColor) {
            Image(painter = painterResource(id = R.drawable.profile_image_3), contentDescription ="" , contentScale = ContentScale.Crop, modifier = Modifier.clickable {
                key=3;
                showCustomDialog=!showCustomDialog;
                key=-1;
            })

        }
    }


    if(showCustomDialog) DialogBoxForImageEdit(image ) { showCustomDialog=!showCustomDialog }


}

var str="About Me:\n" + "Hey there! I'm Jiya, a 24-year-old actor who's passionate about movies and acting." +
        " By day, you'll find me 15 july, but by night, I'm sleepy bug."

@Composable
fun ProfileBio() {
    Surface(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(), color = topAppBarTextColor) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "BIO:", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 24.sp)
                Icon(imageVector = Icons.Default.Edit, contentDescription ="Edit personal info" ,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { }
                )

            }
            Text(text = str, maxLines = 100, overflow = TextOverflow.Ellipsis, color = statusAndTopAppBarColor)

        }

    }
}

@Composable
fun LogOut(navController:NavHostController) {
    val authViewModel= hiltViewModel<AuthViewModel>()
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
                authViewModel.LogoutUser();
                navController.navigate(SCREENS.LOGIN.route);
            }) {
            Text(text = "Log Out", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 28.sp)
        }

    }
}
@Composable
fun PersonalInfo() {
    Surface(
        Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(), color = topAppBarTextColor) {
        Column(modifier = Modifier.padding(top=4.dp, start = 4.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Personal info :", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 28.sp)
                Icon(imageVector = Icons.Default.Edit, contentDescription ="Edit personal info" ,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {}
                )

            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(text = "D.O.B.: ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
                Text(text = "15/07/1999", color = DarkBlue, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(text = "Email: ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
                Text(text = "suraj34and94@gmail.com", color = DarkBlue, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(text = "Contact: ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
                Text(text = "7250260100", color = DarkBlue, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
            }


        }

    }
}

@Composable
fun FrisbeeInfo() {
    Surface(
        Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(), color = topAppBarTextColor) {
        Column(modifier = Modifier.padding(top=4.dp, start = 4.dp, end = 4.dp)) {
            Text(text = "Frisbee info :", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 28.sp)
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(text = "Requests: ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
                Text(text = "102", color = DarkBlue, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(text = "Offers raised: ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
                Text(text = "12", color = DarkBlue, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Text(text = "Thumbs up: ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
                Text(text = "5", color = DarkBlue, style = MaterialTheme.typography.displayMedium, fontSize = 22.sp)
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
                text ="Jiya Shankar",
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


