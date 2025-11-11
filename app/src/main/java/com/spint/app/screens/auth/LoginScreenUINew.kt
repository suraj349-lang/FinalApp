package com.spint.app.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.spint.app.R
import com.spint.app.viewmodels.AuthViewModel
import com.spint.app.login.PhoneLogin
import com.spint.app.model.User
import com.spint.app.navigation.SCREENS
import com.spint.app.screens.dialogBox.DialogLoading
import com.spint.app.utils.LoginState
import com.spint.app.utils.constants.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginScreenWrapperNewUI(authViewModel: AuthViewModel,navController: NavController){
    val loginState by authViewModel.loginState.collectAsState()
    val scope= rememberCoroutineScope()
    val context= LocalContext.current
    var user by remember {
        mutableStateOf(User())
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
    LoginScreenUINew(
        onBackClicked={navController.navigateUp()},
        onSignUpClicked = {navController.navigate(SCREENS.SIGNUP.route)},
        onLoginClicked={number,password->authViewModel.loginUser(number,password)}
    )

}


@Composable
fun LoginScreenUINew(onBackClicked:()->Unit={},onSignUpClicked:()->Unit={},onLoginClicked:(credentials:String,password:String)->Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val addString = "+91";
    val maxLength = 10;
    val loginMethod = PhoneLogin()
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var loginPasswordText by remember { mutableStateOf("") }

    //


    val keyboardController = LocalSoftwareKeyboardController.current

    var passwordVisibility by remember { mutableStateOf(false) }
    val icon =
        if (passwordVisibility) painterResource(id = R.drawable.round_visibility_24) else painterResource(
            id = R.drawable.round_visibility_off_24
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7B1FA2),
                        Color(0xFF0288D1)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "",
            modifier = Modifier
                .clickable { onBackClicked() }
                .padding(16.dp)
                .size(24.dp)
                .align(Alignment.TopStart),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Color.White)

        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 30.dp)
        ) {

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon_dynamic),
                    contentDescription = "",
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = Constants.APP_NAME,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 20.sp, color = Color.White
                )

            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = Constants.FONT_MEDIUM,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp, color = Color.White
                            )
                        ) {
                            append("Welcome")
                        }


                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = Constants.FONT_LIGHT,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp, color = Color.White
                            )
                        ) {
                            append("Enter username or phone number to continue")
                        }


                    }, modifier = Modifier.fillMaxWidth()
                )

            }
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.length <= maxLength) phoneNumber = it
                        else Toast.makeText(context, "Can be 10 digits only !", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Enter number / username", fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp) },
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 16.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        focusedLabelColor = Color.LightGray,
                    ),
                    leadingIcon = {
                        Text(
                            text = "+91",
                            fontWeight = FontWeight.Normal,
                            fontFamily = Constants.FONT_LIGHT,
                            fontSize = 18.sp, textAlign = TextAlign.Justify,
                            color = Color(0xFF615C5C)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ), keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() })
                    //visualTransformation = CameroonNumberVisualTransformation(),


                )
                OutlinedTextField(
                    value = loginPasswordText,
                    onValueChange = { loginPasswordText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Password", fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp) },

                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 16.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        focusedLabelColor = Color.LightGray,
                    ),
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


                Button(
                    onClick = {
                        scope.launch {
                            //if(loginMethod.validate(phoneNumber)){
                            // TODO  authViewModel.loginUser("$addString$phoneNumber", loginPasswordText)
                            onLoginClicked("$addString$phoneNumber",loginPasswordText)
                            // }else{
                            ////  authViewModel.setValidationError("Invalid phone  number")
                            // }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC2185B),
                        contentColor = Color.White
                    ), elevation = ButtonDefaults.buttonElevation(defaultElevation = 20.dp)
                ) {
                    Text(text = "LOGIN", fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp)
                }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New member? ",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Create Account", color = Color.White,
                    fontFamily = Constants.FONT_MEDIUM,
                    fontWeight = FontWeight.Normal,
                    fontSize=13.sp,
                    modifier = Modifier.clickable {
                        onSignUpClicked()
                    })

            }
        }

    }

}