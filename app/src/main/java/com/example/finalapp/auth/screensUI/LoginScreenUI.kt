package com.example.finalapp.auth.screensUI

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreenUI(navController: NavController= NavController(LocalContext.current)) {

    var loginEmailText by remember { mutableStateOf("") }
    var loginPasswordText by remember { mutableStateOf("") }
    var passwordVisibility by rememberSaveable{mutableStateOf(false)}
    val icon=if (passwordVisibility)
                 painterResource(id =R.drawable.round_visibility_24 )
             else
                 painterResource(id = R.drawable.round_visibility_off_24)
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.tree),
                contentDescription = "",
                modifier = Modifier.size(100.dp)

                )
            Text(text = "CHAT RADAR", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = loginEmailText,
                    onValueChange = { loginEmailText = it },
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "...") }

                )
                OutlinedTextField(
                    value = loginPasswordText,
                    onValueChange = { loginPasswordText = it },
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "...") },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility=!passwordVisibility }) {
                            Icon(painter = icon, contentDescription ="Password Visibility icon" )
                            
                        }
                    }

                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.width(100.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = "LOGIN")
                }
                TextButton(onClick = { navController.navigate(SCREENS.SIGNUP.route) }) {
                    Text(text = "New member?, SIGN-UP", color = Color.Black)

                }


            }

        }

    }
}
