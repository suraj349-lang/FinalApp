package com.spint.app.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.model.RegisterUserModel
import com.spint.app.navigation.SCREENS
import com.spint.app.ui.theme.statusAndTopAppBarColor
import com.spint.app.ui.theme.topAppBarTextColor
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.constants.Constants.TAG
import com.spint.app.utils.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun FinalUserCreation(authViewModel: AuthViewModel, navController: NavHostController){
    val firebaseAuth= FirebaseAuth.getInstance();
    var name by authViewModel.profileName
    var number by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    val scope= rememberCoroutineScope()
    val address by authViewModel.address.collectAsState()



    LaunchedEffect(key1 =true){
        scope.launch(Dispatchers.IO) {
            token = Firebase.messaging.token.await()
            number=firebaseAuth.currentUser?.phoneNumber.toString()
        }
    }
    Log.d(TAG, "FinalUserCreation: called")


    FinalUserCreationUI(
        navController,
        authViewModel,
        name,
        number,
        username,
        password,
        onNameChange = {name=it},
        onUsernameChange = {username=it},
        onPasswordChange = {password=it},
        onClick =  {

            scope.launch(Dispatchers.IO) {
                // TODO("the code to save data in SQLITE should be once the response have been returned from api")TODO("the code to save data in SQLITE should be once the response have been returned from api")
                try {
                    authViewModel.registerUser(RegisterUserModel(name.trim(), number,username,password,token,address))
                }catch (e:Exception){
                    Log.d("FinalUserCreation",e.message.toString())
                }
            }
//            scope.launch(Dispatchers.IO) {
//                authViewModel.saveProfileData(Profile(name=name, username = username, number=number,token=token, address = address))
        //                }
            }


    )
}
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FinalUserCreationUI (
    navController:NavController,
    authViewModel: AuthViewModel,
    name:String,
    number:String,
    username:String,
    password:String,
    onNameChange:(String)->Unit,
    onUsernameChange:(String)->Unit,
    onPasswordChange:(String)->Unit,
    onClick:()-> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var checked = true
    Surface(modifier = Modifier.fillMaxSize()) {
        Log.d(TAG, "FinalUserCreationUI: called")
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = Constants.APP_NAME,
                fontSize = 45.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                color = statusAndTopAppBarColor,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "date your way...",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 0.dp, start = 120.dp),
                color = Color(
                    0xFFE71708
                ),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Creating account for number $number")
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text(text = "Name", style = MaterialTheme.typography.bodyMedium) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { keyboardController?.hide() }
                ),
                modifier = Modifier.height(60.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text(text = "Username", style = MaterialTheme.typography.bodyMedium) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { keyboardController?.hide() }
                ),
                modifier = Modifier.height(60.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text(text = "Password", style = MaterialTheme.typography.bodyMedium) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { keyboardController?.hide() }
                ),
                modifier = Modifier.height(60.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp, end = 4.dp)
                    .width(280.dp)
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF023304),
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    text = "By clicking on create account you agree to our user policy.Click here to know Our USER POLICY.",
                    maxLines = 2,
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    onClick()
                    keyboardController?.hide()
                    authViewModel.keyForFinalUserCreation.value = RESPONSE.KEY_ON;

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = statusAndTopAppBarColor,
                    contentColor = topAppBarTextColor
                ),
                modifier = Modifier.padding(top = 8.dp)

            ) {
                Text(text = "Create Account", style = MaterialTheme.typography.bodyMedium)
            }
            val context = LocalContext.current
          //  LaunchedEffect(key1 = authViewModel.keyForFinalUserCreation.value) {
                if (authViewModel.keyForFinalUserCreation.value == RESPONSE.KEY_ON) {
                    when (val result = authViewModel.mySignupResponse.value) {
                        is RequestState.Success -> {
                             authViewModel.keyForFinalUserCreation.value= RESPONSE.KEY_OFF;
                            Toast.makeText(context, "Welcome to Active Dating", Toast.LENGTH_SHORT).show()
                            navController.navigate(SCREENS.HOME.route) {
                                popUpTo(0);
                            }
                        }

                        is RequestState.Error -> {

                            val msg =
                                if (result.error.message.toString() == "HTTP 400 Bad Request") "Number already exists" else result.error.message
                            Log.d("signupResponseDataAndAction", msg.toString())
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }

                        RequestState.Loading -> {
                            //  CircularProgressIndicator(color = Color(0xFF1289BE))
                        }

                        RequestState.Idle -> {
                            Toast.makeText(context, "Registering...", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
            }

        }

    }



enum class RESPONSE(){
    KEY_ON,
    KEY_OFF
}
