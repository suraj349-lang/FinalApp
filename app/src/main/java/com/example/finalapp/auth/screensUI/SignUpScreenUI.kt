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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SignupScreenUI(navController: NavController = NavController(LocalContext.current)) {

    var SignupNumberText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val passwordVisibility by rememberSaveable{mutableStateOf(false)}
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
                           modifier = Modifier.size(100.dp))
                Text(text = "CHAT RADAR", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                OutlinedTextField(
                   value = SignupNumberText,
                   onValueChange = { SignupNumberText = it },
                   label = { Text(text = "First Name") },
                   singleLine = true,
                   keyboardOptions = KeyboardOptions(
                       keyboardType = KeyboardType.Number,
                       imeAction = ImeAction.Next
                   ),
                   keyboardActions = KeyboardActions(
                       onNext = {focusManager.clearFocus()}
                   )

                )
                OutlinedTextField(
                   value = SignupNumberText,
                   onValueChange = { SignupNumberText = it },
                   label = { Text(text = "Last Name") },
                   singleLine = true,
                   keyboardOptions = KeyboardOptions(
                       keyboardType = KeyboardType.Number,
                       imeAction = ImeAction.Next
                   ),
                   keyboardActions = KeyboardActions(
                       onNext = {focusManager.clearFocus()}
                   )

                )

                OutlinedTextField(
                    value = SignupNumberText,
                    onValueChange = { SignupNumberText = it },
                    label = { Text(text = "Number") },
                    placeholder = { Text(text = "...") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {focusManager.clearFocus()}
                    )

                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.width(120.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = "SIGN-UP")
                }
                TextButton(onClick = { navController.navigate(SCREENS.LOGIN.route) }) {
                    Text(text = "Already a member?, LOGIN", color = Color.Black)

                }


            }

        }

    }

