package com.example.finalapp.auth.screensUI

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.auth.CameroonNumberVisualTransformation
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.authViewModel.LoginApiState
import com.example.finalapp.model.LoginModel
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreenUI(navController: NavController= NavController(LocalContext.current),authViewModel: AuthViewModel) {
    val scope= rememberCoroutineScope()

    var loginNumberText by rememberSaveable { mutableStateOf("") }
    val addString by remember {
        mutableStateOf("+91")
    }
    var loginPasswordText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxLength = 10;
    val mContext = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (passwordVisibility) painterResource(id = R.drawable.round_visibility_24)
    else painterResource(id = R.drawable.round_visibility_off_24)

    val lifecycleOwner= LocalLifecycleOwner.current
    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                authViewModel.key.value=0
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
            Text(text="FRISBEE", fontSize = 45.sp, modifier = Modifier.padding(top=8.dp, bottom = 0.dp), color = statusAndTopAppBarColor, style = MaterialTheme.typography.titleMedium)
            Text(text="date your way...", fontSize = 18.sp, modifier = Modifier.padding(top=0.dp, start = 120.dp), color = Color(
                0xFFE71708),
                style = MaterialTheme.typography.titleMedium
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
                        else Toast.makeText(mContext, "Can be 10 digits only !", Toast.LENGTH_SHORT).show()
                    },
//                    textStyle = LocalTextStyle.current.copy(fontSize = 22.sp),
                    label = { Text(text = "Number", style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = {
                        Text(
                            text = "+91",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,textAlign=TextAlign.Justify,
                            color = Color(0xFF035206)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done)
                    ,keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() })
                    //visualTransformation = CameroonNumberVisualTransformation(),


                )
                OutlinedTextField(
                    value = loginPasswordText,
                    onValueChange = { loginPasswordText = it },
                    label = { Text(text = "Password", style = MaterialTheme.typography.bodyMedium) },
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
                        authViewModel.key.value=1;
                        scope.launch {
                            authViewModel.loginUser(LoginModel("$addString$loginNumberText", loginPasswordText))
                        }
                    },
                    modifier = Modifier.width(120.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = statusAndTopAppBarColor,
                        contentColor = topAppBarTextColor
                    )
                ) {
                    Text(text = "LOGIN")
                }

                if(authViewModel.key.value==1) {
                    LoginResponseDataAndAction(authViewModel, navController)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp), horizontalArrangement = Arrangement.Center) {
                Text(text = "New member ? ", color = statusAndTopAppBarColor, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Create Account", color = DarkBlue,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable {
                        navController.navigate(SCREENS.SIGNUP.route)

                    }
                )

            }



            }

        }

    }



@Composable
fun LoginResponseDataAndAction(authViewModel: AuthViewModel, navController: NavController){
    val context= LocalContext.current
    when (val result=authViewModel.myLoginResponse.value){
        is LoginApiState.Success->{
            navController.navigate(SCREENS.HOME.route)
            Log.d("Data Received",result.data.toString())

        }
        is LoginApiState.Failure->{
            Toast.makeText(context,"${result.msg}", Toast.LENGTH_SHORT).show()
        }
        LoginApiState.Loading->{
            CircularProgressIndicator(color = Color(0xFF1289BE))
        }
        LoginApiState.Empty->{
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