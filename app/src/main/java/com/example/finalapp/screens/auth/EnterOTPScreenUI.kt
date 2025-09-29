package com.example.finalapp.screens.auth

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.screens.auth.util.OtpInputField
import com.example.finalapp.utils.constants.Constants



@Composable
fun EnterOTPScreenUI(
    navController: NavController,
    userName:String,
    onUserNameChange:(String)->Unit,
    phoneNumber:String,
    onPhoneNumberChange:(String)->Unit,
    otp:String,
    onOtpChange:(String)->Unit,
    onSignUpClicked:()->Unit) {
    var otpValue by remember { mutableStateOf("") }
//    val verificationID = remember { mutableStateOf("") }
//    val message = remember { mutableStateOf("") }
//    val context= LocalContext.current
//    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
//    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
//                            Color(0xFF1976D2), Color(0xFFE9D8AE)
//                            Color(0xFFFBC02D), Color(0xFFDCDEF0)
                        Color(0xFFFAF8F3), Color(0xFFF5F3F0)
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = "",
            modifier = Modifier
                .clickable { }
                .padding(16.dp)
                .size(24.dp)
                .align(Alignment.TopStart),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Color.Black)

        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
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
                    fontSize = 20.sp, color = Color.Black

                )

            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily =
                                Constants.FONT_MEDIUM,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp, color = Color.DarkGray
                            )
                        ) {
                            append("Choose an username!")
                        }
                    }, modifier = Modifier
                        .fillMaxWidth(), style = TextStyle(lineHeight = 12.sp)
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = Constants.FONT_LIGHT,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp, color = Color.Gray
                            )
                        ) {
                            append("Verify phone number to continue")
                        }


                    }, modifier = Modifier.fillMaxWidth(), style = TextStyle(lineHeight = 12.sp)
                )
            }


            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            OutlinedTextField(
                value = userName,
                onValueChange = {
                    onUserNameChange(it)
                },
                placeholder = { Text(text = "Username", fontFamily = Constants.FONT_LIGHT) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp
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
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                ), keyboardActions = KeyboardActions(
                    onNext = { })


            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    onPhoneNumberChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = Constants.FONT_MEDIUM, fontSize = 16.sp
                ),
                leadingIcon = {
                    Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Image(painter = painterResource(id = R.drawable.india), contentDescription ="country flag", contentScale = ContentScale.Crop, modifier = Modifier.size(30.dp) )
                        Divider(modifier = Modifier.width(1.dp), thickness = 30.dp, color = Color.LightGray)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                },
                placeholder = { Text(text = "Phone Number", fontFamily = Constants.FONT_LIGHT) },
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
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            OtpInputField(otpLength = 6, onOtpChanged = { value -> onOtpChange(value) })

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Didn't Receive OTP?",
                    fontFamily = Constants.FONT_LIGHT,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                TextButton(onClick = { }) {
                    Text(
                        text = "Resend SMS",
                        fontSize = 14.sp,
                        fontFamily = Constants.FONT_LIGHT,
                        color = Color.Black,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
            }

            Button(
                onClick = {
                    onSignUpClicked()
                },
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC2185B),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Verify OTP",
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

        }

    }
}
