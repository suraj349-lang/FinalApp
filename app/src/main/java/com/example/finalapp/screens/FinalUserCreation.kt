package com.example.finalapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.pm.ShortcutInfoCompat.Surface
import androidx.navigation.NavController
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.authViewModel.SignupApiState
import com.example.finalapp.model.LoginModel
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
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
    var checked= remember { mutableStateOf(true) };

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Text(text="FRISBEE", fontSize = 45.sp, modifier = Modifier.padding(top=8.dp, bottom = 0.dp), color = statusAndTopAppBarColor, style = MaterialTheme.typography.titleMedium)
            Text(text="date your way...", fontSize = 18.sp, modifier = Modifier.padding(top=0.dp, start = 120.dp), color = Color(
                0xFFE71708),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value =nameTextFieldData ,
                onValueChange ={nameTextFieldData=it},
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
                value =usernameTextFieldData ,
                onValueChange ={usernameTextFieldData=it},
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
                value =passwordTextFieldData ,
                onValueChange ={passwordTextFieldData=it},
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
                        key=1;
                        scope.launch {
                               authViewModel.RegisterUser(RegisterUserModel(nameTextFieldData, number,usernameTextFieldData,passwordTextFieldData)) }
               },
                colors = ButtonDefaults.buttonColors(containerColor = statusAndTopAppBarColor, contentColor = topAppBarTextColor),
                modifier = Modifier.padding(top = 8.dp)

            ) {
                Text(text = "Create Account",style = MaterialTheme.typography.bodyMedium)
            }
            if(key==1){
                SignupResponseDataAndAction(authViewModel, navController );
                key=0;
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
            navController.navigate(SCREENS.HOME.route){
                popUpTo(0);
            }
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