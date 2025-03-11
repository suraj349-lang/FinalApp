package com.example.finalapp.screens._5settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL

@Composable
fun EditName(navController: NavHostController) {
    var name by remember {
        mutableStateOf(ProfileObject.profile?.name)
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
                Text(text = "Update Your Name", fontSize = 30.sp, fontFamily = DONGLE_BOLD)
                OutlinedTextField(value = name ?: "", onValueChange = { name = it })
                Button(
                    onClick = { /*TODO make and api call and then make a db call */ },
                    colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor)
                ) {
                    Text(
                        text = "Update Name",
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
fun EditUserName(navController: NavHostController) {
    var username by remember {
        mutableStateOf(ProfileObject.profile?.username)
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
                OutlinedTextField(value = username ?: "", onValueChange = { username = it })
                Button(
                    onClick = { /*TODO make and api call and then make a db call */ },
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
        mutableStateOf(ProfileObject.profile?.number)
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