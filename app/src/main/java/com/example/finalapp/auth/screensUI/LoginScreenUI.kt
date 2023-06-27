package com.example.finalapp.auth.screensUI

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.finalapp.auth.CameroonNumberVisualTransformation
import com.example.finalapp.auth.authViewModel.ApiState
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.model.LoginModel
import com.example.finalapp.navigation.SCREENS
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreenUI(navController: NavController= NavController(LocalContext.current)) {
    val scope= rememberCoroutineScope()
    val authViewModel = AuthViewModel();
    var loginNumberText by rememberSaveable { mutableStateOf("") }
    var loginPasswordText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxLength = 10;
    val mContext = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (passwordVisibility) painterResource(id = R.drawable.round_visibility_24)
    else painterResource(id = R.drawable.round_visibility_off_24)
    var key by remember {
        mutableStateOf(0)
    }

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
            Text(
                text = "CHAT RADAR",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = loginNumberText,
                    onValueChange = {
                        if (it.length <= maxLength) loginNumberText = it
                        else Toast.makeText(mContext, "Can be 10 digits only !", Toast.LENGTH_SHORT)
                            .show()
                    },
                    label = { Text(text = "Number") },
                    leadingIcon = {
                        Text(
                            text = "+91",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF035206)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ), visualTransformation = CameroonNumberVisualTransformation()

                )
                OutlinedTextField(
                    value = loginPasswordText,
                    onValueChange = { loginPasswordText = it },
                    label = { Text(text = "Password") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(painter = icon, contentDescription = "Password visibility icon")
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    visualTransformation = if (passwordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        key=1;
                        scope.launch {
                            authViewModel.loginUser(LoginModel(loginNumberText, loginPasswordText))
                        }
                    },
                    modifier = Modifier.width(120.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(text = "Get OTP")
                }
                if(key==1){
                    LoginResponseDataAndAction(authViewModel,navController)
                }
               // MediaPlayerSuraj()

                TextButton(onClick = { navController.navigate(SCREENS.SIGNUP.route) }) {
                    Text(text = "New member?, SIGN-UP", color = Color.Black)

                }


            }

        }

    }
}
@Composable
fun LoginResponseDataAndAction(authViewModel: AuthViewModel, navController: NavController){
    val context= LocalContext.current
    when (val result=authViewModel.myResponse.value){
        is ApiState.Success->{
//            navController.navigate(SCREENS.HOME.route)
            Log.d("Data Received",result.data.toString())

        }
        is ApiState.Failure->{
            Toast.makeText(context,"${result.msg}", Toast.LENGTH_SHORT).show()
        }
        ApiState.Loading->{
            CircularProgressIndicator(color = Color(0xFF1289BE))
        }
        ApiState.Empty->{
            Toast.makeText(context,"Empty Data", Toast.LENGTH_SHORT).show()
        }

    }


}
@Composable
fun MediaPlayerSuraj(){
    val context= LocalContext.current;
    DisposableEffect(key1 = true ){
        val media= MediaPlayer.create(context,R.raw.music)

        media.start()
        onDispose {
            media.stop();
            media.release();
        }
    }
}