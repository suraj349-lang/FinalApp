package com.example.finalapp.screens._5settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL
import com.example.finalapp.viewmodels.SettingsViewModel

@Composable
fun EditName(navController: NavHostController,settingsViewModel: SettingsViewModel) {
    val context= LocalContext.current
    var name by remember {
        mutableStateOf(UserObject.user.value.name)
    }
    val updateName by settingsViewModel.updateName.collectAsState()
    when(updateName){
        is RequestState.Loading->{
            LinearProgressIndicator()
        }
        is RequestState.Error ->{
            settingsViewModel.resetNameStateToIdle()
            Toast.makeText(context,"Error updating name, Try again!",Toast.LENGTH_SHORT).show()
        }
        is RequestState.Success ->{
            settingsViewModel.resetNameStateToIdle()
            Toast.makeText(context,"Name updated successfully.",Toast.LENGTH_SHORT).show()
        }
        else ->{

        }
    }
    Scaffold(topBar = {
        CommonTopBar(title = "Your Display Name") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Update Your Name", fontSize = 24.sp, fontFamily = Constants.FONT_MEDIUM)
                OutlinedTextField(value = name, onValueChange = { name = it })
                Button(
                    onClick = {if(name.trim().isNotEmpty()) settingsViewModel.updateName(name)},
                    colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor)
                ) {
                    Text(
                        text = "Update Name",
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 14.sp,
                        color = Color.White
                    )

                }


            }
        }
    )

}

@Composable
fun EditUserName(navController: NavHostController,settingsViewModel: SettingsViewModel) {
    val savedUsername=UserObject.user.collectAsState()
    var userName by remember {
        mutableStateOf("")
    }
    val context= LocalContext.current
    val updateUserName by settingsViewModel.updateUserName.collectAsState()
    when(updateUserName){
        is RequestState.Loading->{
            LinearProgressIndicator()
        }
        is RequestState.Error ->{
            Toast.makeText(context,"Error updating name, Try again!",Toast.LENGTH_SHORT).show()
            settingsViewModel.resetUserNameStateToIdle()
        }
        is RequestState.Success ->{
            userName=savedUsername.value.name
            Toast.makeText(context,"Name updated successfully.",Toast.LENGTH_SHORT).show()
            settingsViewModel.resetUserNameStateToIdle()
        }
        else ->{

        }
    }
    Scaffold(topBar = {
        CommonTopBar(title = "Your UserName") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Update Your UserName", fontSize = 30.sp, fontFamily = DONGLE_BOLD)
                OutlinedTextField(value = userName.ifEmpty { savedUsername.value.userName }, onValueChange = { userName = it })
                Button(
                    onClick = {if(userName.trim().isNotEmpty()) { settingsViewModel.updateUserName(userName) }  },
                    colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor)
                ) {
                    Text(
                        text = "Update UserName",
                        fontFamily = DONGLE_NORMAL,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                }


            }
        }
    )

}

@Composable
fun EditPhoneNumber(navController: NavHostController) {
    var phoneNumber by remember {
        mutableStateOf(UserObject.user.value.number)
    }
    Scaffold(topBar = {
        CommonTopBar(title = "Your Contact Number") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Update Your number", fontSize = 30.sp, fontFamily = DONGLE_BOLD)
                OutlinedTextField(value = phoneNumber ?: "", onValueChange = { phoneNumber = it })
                Button(
                    onClick = { /*TODO make and api call and then make a db call */ },
                    colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor)
                ) {
                    Text(
                        text = "Update phone number",
                        fontFamily = DONGLE_NORMAL,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                }


            }
        }
    )

}


@Composable
fun EditPassword(navController: NavHostController) {
    var password by remember {
        mutableStateOf("")
    }
    Scaffold(topBar = {
        CommonTopBar(title = "Your Password ") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Update Your password", fontSize = 30.sp, fontFamily = DONGLE_BOLD)
                OutlinedTextField(value = password, onValueChange = { password = it })
                Button(
                    onClick = { /*TODO make and api call and then make a db call */ },
                    colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor)
                ) {
                    Text(
                        text = "Update password",
                        fontFamily = DONGLE_NORMAL,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                }


            }
        }
    )

}


@Composable
fun DeleteAccount(navController: NavHostController) {
    Scaffold(topBar = {
        CommonTopBar(title = "Delete Account ") {
            navController.navigateUp()
        }
    },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Delete Account", fontSize = 30.sp, fontFamily = DONGLE_BOLD)
                Button(
                    onClick = { /*TODO make and api call and then make a db call */ },
                    colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor)
                ) {
                    Text(
                        text = "Delete Account",
                        fontFamily = DONGLE_NORMAL,
                        fontSize = 20.sp,
                        color = Color.White
                    )

                }


            }
        }
    )

}