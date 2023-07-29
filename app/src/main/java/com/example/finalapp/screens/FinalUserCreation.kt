package com.example.finalapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.navigation.NavController
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.authViewModel.SignupApiState
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.navigation.SCREENS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FinalUserCreation (navController:NavController,authViewModel: AuthViewModel){
    val firebaseAuth= FirebaseAuth.getInstance();
    val number by remember {
        mutableStateOf(firebaseAuth.currentUser?.phoneNumber.toString())
    }
    var key by remember {
        mutableStateOf(0)
    }
    val scope= rememberCoroutineScope()
    var nameTextFieldData by remember {
        mutableStateOf("")
    }
    var usernameTextFieldData by remember {
        mutableStateOf("")
    }
    var passwordTextFieldData by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text(text = "Bingo", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color(0xFF12609E))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value =nameTextFieldData ,
                onValueChange ={nameTextFieldData=it},
                label = { Text(text = "Name")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide()}
                )
            )
            OutlinedTextField(
                value =usernameTextFieldData ,
                onValueChange ={usernameTextFieldData=it},
                label = { Text(text = "Username")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide()}
                )
            )
            OutlinedTextField(
                value =passwordTextFieldData ,
                onValueChange ={passwordTextFieldData=it},
                label = { Text(text = "Password")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {keyboardController?.hide()}
                )
            )
            Button(onClick = {
                key=1;
                scope.launch {
                    authViewModel.RegisterUser(RegisterUserModel(nameTextFieldData, number,usernameTextFieldData,passwordTextFieldData))
                }
            }) {
                Text(text = "Create Account")
            }
            if(key==1){
                SignupResponseDataAndAction(authViewModel, navController )
            }

        }

    }
}

@Composable
fun SignupResponseDataAndAction(authViewModel: AuthViewModel, navController: NavController){
    val context= LocalContext.current
    when (val result=authViewModel.mySignupResponse.value){
        is SignupApiState.Success->{
            Log.d("Data Received2",result.data.toString())
            navController.navigate(SCREENS.HOME.route)

        }
        is SignupApiState.Failure->{
            Toast.makeText(context,"${result.msg}", Toast.LENGTH_SHORT).show()
        }
        SignupApiState.Loading->{
            CircularProgressIndicator(color = Color(0xFF1289BE))
        }
        SignupApiState.Empty->{
            Toast.makeText(context,"Empty Data", Toast.LENGTH_SHORT).show()
        }

    }


}