package com.example.finalapp.screens

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.database.Profile
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.example.finalapp.utils.Constants.Constants
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
    val address=authViewModel.address.value



    LaunchedEffect(key1 =true){
        scope.launch(Dispatchers.IO) {
            token = Firebase.messaging.token.await()
            number=firebaseAuth.currentUser?.phoneNumber.toString()
        }
    }


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
                try {
                    authViewModel.saveProfileData(Profile(name=name, username = username, number=number,token=token, address = address))
                }catch (e:Exception){
                    Log.d("FinalUserCreation",e.message.toString())
                }
            }
            scope.launch(Dispatchers.IO) {
                authViewModel.RegisterUser(RegisterUserModel(name.trim(), number,username,password,token,address))  }
            }


    )
}
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
    onClick:()-> Unit)
     {
      val keyboardController = LocalSoftwareKeyboardController.current
      var checked= remember { mutableStateOf(true) };

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text(text=Constants.APP_NAME, fontSize = 45.sp, modifier = Modifier.padding(top=8.dp, bottom = 0.dp), color = statusAndTopAppBarColor, style = MaterialTheme.typography.titleMedium)
            Text(text="date your way...", fontSize = 18.sp, modifier = Modifier.padding(top=0.dp, start = 120.dp), color = Color(
                0xFFE71708),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Creating account for number $number")
            OutlinedTextField(
                value =name ,
                onValueChange =onNameChange,
                label = { Text(text = "Name", style = MaterialTheme.typography.bodyMedium)},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide()}
                ),
                modifier = Modifier.height(60.dp)
            )
            OutlinedTextField(
                value =username ,
                onValueChange =onUsernameChange,
                label = { Text(text = "Username", style = MaterialTheme.typography.bodyMedium)},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide()}
                ),
                modifier = Modifier.height(60.dp)
            )
            OutlinedTextField(
                value =password ,
                onValueChange =onPasswordChange,
                label = { Text(text = "Password", style = MaterialTheme.typography.bodyMedium)},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide()}
                ),
                modifier = Modifier.height(60.dp)
            )

            Row(modifier = Modifier
                .padding(top = 8.dp, end = 4.dp)
                .width(280.dp)
                .height(30.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Checkbox(checked = checked.value, onCheckedChange ={ checked.value=it } , colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF023304),
                    checkmarkColor = Color.White                )
                )
                Text(text = "By clicking on create account you agree to our user policy.Click here to know Our USER POLICY.", maxLines = 2, overflow = TextOverflow.Visible, style = MaterialTheme.typography.bodySmall)
            }
           
            Button(
                onClick = {
                        keyboardController?.hide()
                        authViewModel.keyForFinalUserCreation.value=1;
                        onClick();
                      },
                colors = ButtonDefaults.buttonColors(containerColor = statusAndTopAppBarColor, contentColor = topAppBarTextColor),
                modifier = Modifier.padding(top = 8.dp)

            ) {
                Text(text = "Create Account",style = MaterialTheme.typography.bodyMedium)
            }
            if(authViewModel.keyForFinalUserCreation.value==1){
                authViewModel.signupResponseDataAndAction(navController )
                }
            }

        }

    }


