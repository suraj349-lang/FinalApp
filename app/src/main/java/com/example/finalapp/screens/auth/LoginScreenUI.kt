package com.example.finalapp.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
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
import com.example.finalapp.login.PhoneLogin
import com.example.finalapp.model.User
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens.onboarding.util.OnBoardingPage
import com.example.finalapp.utils.LoginState
import com.example.finalapp.utils.UserLocation
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.APP_NAME_FONT
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@SuppressLint("UnrememberedMutableState")
@Composable
fun LoginScreenUI(navController: NavController,authViewModel: AuthViewModel) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val addString = "+91";
    val maxLength = 10;
    val loginMethod = PhoneLogin()
    var loginNumberText by rememberSaveable { mutableStateOf("7250260100") }
    var loginPasswordText by remember { mutableStateOf("1234567") }

    val loginState by authViewModel.loginState.collectAsState()
    var user by remember {
        mutableStateOf(User())
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    var passwordVisibility by remember { mutableStateOf(false) }
    val icon =
        if (passwordVisibility) painterResource(id = R.drawable.round_visibility_24) else painterResource(
            id = R.drawable.round_visibility_off_24
        )
    when (loginState) {
        is LoginState.Loading -> { /* DialogLoading() */
        }

        is LoginState.Success -> {
            authViewModel.userData.value = (loginState as LoginState.Success).data
            navController.navigate(SCREENS.HOME.route)
        }

        is LoginState.Error -> {
            Log.e("Login Error", "LoginScreenUI: ${(loginState as LoginState.Error).error} ",)
            Toast.makeText(context, "Unable to login.", Toast.LENGTH_SHORT).show()
        }

        LoginState.Idle -> {

        }

    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Constants.LOGIN_BACKGROUND),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome back!",
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, bottom = 32.dp)
                    .fillMaxWidth(),
                color = Constants.LOGIN_SURFACE,
                fontFamily = Constants.FONT_MEDIUM
            )
            Text(
                text = Constants.APP_NAME,
                fontSize = 45.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                color = Constants.LOGIN_SURFACE,
                fontFamily = APP_NAME_FONT
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = loginNumberText,
                    onValueChange = {
                        if (it.length <= maxLength) loginNumberText = it
                        else Toast.makeText(context, "Can be 10 digits only !", Toast.LENGTH_SHORT)
                            .show()
                    },
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM
                    ),
                    label = {
                        Text(
                            text = if (loginNumberText.isEmpty()) "Enter number..." else "Number",
                            fontFamily = Constants.FONT_MEDIUM
                        )
                    },
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
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp, textAlign = TextAlign.Justify,
                            color = Color.Black
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
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM
                    ),
                    label = {
                        Text(
                            text = if (loginPasswordText.isEmpty()) "Enter password..." else "Password",
                            fontFamily = Constants.FONT_MEDIUM
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedLabelColor = Color.LightGray,
                        focusedLabelColor = Color.LightGray
                    ),
                    trailingIcon = {
                        if (loginPasswordText.isNotEmpty()) {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(
                                    painter = icon,
                                    contentDescription = "Password visibility icon",
                                    tint = Color.Black
                                )
                            }
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
                            if (loginMethod.validate(loginNumberText)) {
                                authViewModel.loginUser(
                                    "$addString$loginNumberText",
                                    loginPasswordText
                                )
                            } else {
                                authViewModel.setValidationError("Invalid phone")
                            }
                        }
                    },
                    modifier = Modifier.width(120.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.DarkGray
                    )
                ) {
                    Text(text = "LOGIN", fontFamily = Constants.FONT_MEDIUM, fontSize = 20.sp)
                }

//                if(authViewModel.key.value==1) {
//                    LoginResponseDataAndAction(authViewModel, navController)
//                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "New member?  ",
                    color = Color.White,
                    fontFamily = Constants.FONT_MEDIUM
                )
                Text(
                    text = "Create Account", color = Color.White,
                    fontFamily = Constants.FONT_MEDIUM,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable {
                        navController.navigate(SCREENS.SIGNUP.route)
                    })

            }
//            LoginHorizontalPager()
        }

    }

}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun LoginHorizontalPager() {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third,
        OnBoardingPage.Fourth
    )
    val pagerState = rememberPagerState()
    LaunchedEffect(pagerState) {
        while (true) {
            delay(2000L)
            val nextPage = (pagerState.currentPage + 1) % pages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    Column(modifier = Modifier
        .padding(top = 24.dp)
        .wrapContentHeight()
        .fillMaxWidth()) {
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(
            modifier = Modifier,
            count = 4,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            LoginPagerScreen(onBoardingPage = pages[position])
        }

    }
}


@Composable
fun LoginPagerScreen(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier.size(160.dp),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = "Pager Image"
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = onBoardingPage.title,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp),
            text = onBoardingPage.description,
            fontFamily=Constants.FONT_MEDIUM,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}
