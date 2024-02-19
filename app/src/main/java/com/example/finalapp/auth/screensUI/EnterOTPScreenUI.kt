package com.example.finalapp.auth.screensUI

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.auth.screensUI.otp.OtpInputField
import com.example.finalapp.navigation.SCREENS
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EnterOTPScreenUI(navController: NavController= NavController(LocalContext.current)) {
    var otpValue by remember { mutableStateOf("") }
//    val verificationID = remember { mutableStateOf("") }
//    val message = remember { mutableStateOf("") }
//    val context= LocalContext.current
//    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
//    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Image(
                    painter = painterResource(id = R.drawable.baseline_verified_user_24),
                    contentDescription = "",
                    modifier = Modifier.size(60.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFB8350C))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Enter OTP",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(
                        0xFF0A5C81
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OtpInputField(otpLength = 6, onOtpChanged = { otp -> otpValue = otp })
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Didn't Receive OTP?")
                    TextButton(onClick = { }) {
                        Text(
                            text = "Resend SMS",
                            fontSize = 16.sp,
                            style = TextStyle(textDecoration = TextDecoration.Underline)
                        )
                    }
                }

                Button(
                    onClick = {
                        keyboardController?.hide()
                              },
                    shape = RoundedCornerShape(6.dp)
                    // colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
                ) {
                    Text(text = "Verify OTP")
                }


            }

        }
    }

