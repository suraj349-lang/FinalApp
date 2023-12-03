package com.example.finalapp.auth.screensUI

import android.annotation.SuppressLint
import android.app.Activity
import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.repository.AuthRepository
import com.example.finalapp.auth.repository.FirebaseRepository
import com.example.finalapp.model.RegisterUserModel
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SignupScreenUI(navController: NavController = NavController(LocalContext.current)) {

    val firebaseRepository = FirebaseRepository()
    val authViewModel= hiltViewModel<AuthViewModel>()
    val focusManager = LocalFocusManager.current
    val passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.round_visibility_24)
    else
        painterResource(id = R.drawable.round_visibility_off_24)
    val phoneNumber = remember { mutableStateOf("") }
    val otp = remember { mutableStateOf("") }
    val verificationID = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val context = LocalContext.current
    var key = remember { mutableStateOf(0) }
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
                text = "FRISBEE",
                fontSize = 45.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                color = statusAndTopAppBarColor,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "date your way...",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 0.dp, start = 120.dp),
                color = Color(
                    0xFFE71708
                ),
                style = MaterialTheme.typography.titleMedium
            )
            if (key.value==0) {
                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = { phoneNumber.value = it },
                    label = {
                        Text(
                            text = "Enter number",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    )

                )

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        // on below line we are validating user inputs
                        if (TextUtils.isEmpty(phoneNumber.value.toString())) {
                            Toast.makeText(
                                context,
                                "Please enter phone number..",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            val number = "+91${phoneNumber.value}"
                            key.value = 1;
                            // on below line calling method to generate verification code.
                            firebaseRepository.sendVerificationCode(
                                number,
                                mAuth,
                                context as Activity,
                                callbacks
                            )
                        }
                    },
                    modifier = Modifier.width(160.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = statusAndTopAppBarColor,
                        contentColor = topAppBarTextColor
                    )
                ) {
                    // on below line we are adding text for our button
                    Text(text = "Generate OTP", modifier = Modifier.padding(8.dp))
                }
            }
            // adding spacer on below line.
            Spacer(modifier = Modifier.height(10.dp))

            if (key.value==1) {
                OutlinedTextField(
                    value = otp.value,
                    onValueChange = { otp.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(150.dp),
                    textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                    singleLine = true,
                )
                Button(
                    onClick = {
                        if (TextUtils.isEmpty(otp.value)) {
                            Toast.makeText(context, "Please enter otp..", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            // on below line generating phone credentials.
                            val credential: PhoneAuthCredential =
                                PhoneAuthProvider.getCredential(
                                    verificationID.value, otp.value
                                )

                            // on below line signing within credentials.
                            firebaseRepository.signInWithPhoneAuthCredential(
                                credential,
                                mAuth,
                                context as Activity,
                                context,
                                message,
                                navController
                            )
                        }
                    },
                    modifier = Modifier.width(95.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = topAppBarTextColor,
                        containerColor = statusAndTopAppBarColor
                    )
                ) {
                    Text(text = "Submit")
                }
            }

            Spacer(modifier = Modifier.height(5.dp))


            Text(
                // on below line displaying message for verification status.
                text = message.value,
                style = TextStyle(
                    color = Color.Green,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // on below line creating callback
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                // on below line updating message
                // and displaying toast message
                message.value = "Verification successful"
                Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                // on below line displaying error as toast message.
                message.value = "Fail to verify user : \n" + p0.message
                Toast.makeText(context, "Verification failed..", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                // this method is called when code is send
                super.onCodeSent(verificationId, p1)
                verificationID.value = verificationId
            }
        }

    }

}


