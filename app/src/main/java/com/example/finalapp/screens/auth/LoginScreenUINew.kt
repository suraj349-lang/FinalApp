package com.example.finalapp.screens.auth

import android.annotation.SuppressLint
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.datastore.StoreUserData
import com.example.finalapp.login.EmailLogin
import com.example.finalapp.login.PhoneLogin
import com.example.finalapp.model.User
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.example.finalapp.utils.LoginState
import com.example.finalapp.utils.constants.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreenUINew(navController: NavController,authViewModel: AuthViewModel) {
    val scope= rememberCoroutineScope()
    val context= LocalContext.current

    val addString="+91";
    val maxLength = 10;
    val loginMethod=PhoneLogin()
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var loginPasswordText by remember { mutableStateOf("") }

    val loginState by authViewModel.loginState.collectAsState()
    var user by remember {
        mutableStateOf(User())
    }

    val datastore=StoreUserData(context )
    val keyboardController = LocalSoftwareKeyboardController.current

    var passwordVisibility by remember { mutableStateOf(false) }
    val icon = if (passwordVisibility) painterResource(id = R.drawable.round_visibility_24) else painterResource(id = R.drawable.round_visibility_off_24)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "",
                modifier = Modifier.size(100.dp)

            )
            Text(text=Constants.APP_NAME, fontSize = 45.sp, modifier = Modifier.padding(top=8.dp, bottom = 0.dp), color = statusAndTopAppBarColor, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.length <= maxLength) phoneNumber = it
                        else Toast.makeText(context, "Can be 10 digits only !", Toast.LENGTH_SHORT).show()
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
                        scope.launch {
                            datastore.saveUserNumber("+91$phoneNumber")
                            //if(loginMethod.validate(phoneNumber)){
                                authViewModel.loginUser(loginMethod,"$addString$phoneNumber", loginPasswordText)
                           // }else{
                              ////  authViewModel.setValidationError("Invalid phone  number")
                           // }
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

//                if(authViewModel.key.value==1) {
//                    LoginResponseDataAndAction(authViewModel, navController)
//                }
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
                    })

            }
        }

        when (loginState){
            is LoginState.Loading -> DialogLoading()
            is LoginState.Success->{
                scope.launch(Dispatchers.IO) {  user= (loginState as LoginState.Success).data  }
                scope.launch(Dispatchers.Main) {  navController.navigate(SCREENS.HOME.route)}
            }
            is LoginState.Error->{
                Toast.makeText(context,"Error in Login", Toast.LENGTH_SHORT).show()
            }
            LoginState.Idle->{

            }

        }
    }

}
