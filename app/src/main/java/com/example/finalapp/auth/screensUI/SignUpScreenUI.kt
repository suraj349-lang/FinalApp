package com.example.finalapp.auth.screensUI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.example.finalapp.auth.authViewModel.AuthViewModel
import com.example.finalapp.auth.authViewModel.SignupApiState
import com.example.finalapp.auth.repository.FirebaseRepository
import com.example.finalapp.navigation.SCREENS
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SignupScreenUI(navController: NavController = NavController(LocalContext.current)) {

    val firebaseRepository=FirebaseRepository()
    val focusManager = LocalFocusManager.current
    val passwordVisibility by rememberSaveable{mutableStateOf(false)}
    val icon=if (passwordVisibility)
        painterResource(id =R.drawable.round_visibility_24 )
    else
        painterResource(id = R.drawable.round_visibility_off_24)
    val phoneNumber = remember {
        mutableStateOf("")
    }

    val otp = remember {
        mutableStateOf("")
    }

    val verificationID = remember {
        mutableStateOf("")
    }

    val message = remember {
        mutableStateOf("")
    }
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val context= LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
           Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                           painter = painterResource(id = R.drawable.tree),
                           contentDescription = "",
                           modifier = Modifier.size(100.dp))
                Text(text = "CHAT RADAR", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                OutlinedTextField(
                   value = phoneNumber.value,
                   onValueChange = { phoneNumber.value = it },
                   label = { Text(text = "Phone Number") },
                   singleLine = true,
                   keyboardOptions = KeyboardOptions(
                       keyboardType = KeyboardType.Number,
                       imeAction = ImeAction.Next
                   ),
                   keyboardActions = KeyboardActions(
                       onNext = {focusManager.clearFocus()}
                   )

                )

                Spacer(modifier = Modifier.height(10.dp))
               Button(
                   onClick = {
                       // on below line we are validating user inputs
                       if (TextUtils.isEmpty(phoneNumber.value.toString())) {
                           Toast.makeText(context, "Please enter phone number..", Toast.LENGTH_SHORT)
                               .show()
                       } else {

                           val number = "+91${phoneNumber.value}"
                           // on below line calling method to generate verification code.
                           firebaseRepository.sendVerificationCode(number, mAuth, context as Activity, callbacks)
                       }
                   },
                   // on below line we are
                   // adding modifier to our button.
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(16.dp)
               ) {
                   // on below line we are adding text for our button
                   Text(text = "Generate OTP", modifier = Modifier.padding(8.dp))
               }
               // adding spacer on below line.
               Spacer(modifier = Modifier.height(10.dp))

               // on below line creating text field for otp
               TextField(
                   // on below line we are specifying
                   // value for our course duration text field.
                   value = otp.value,
                   //specifying key board on below line.
                   keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                   // on below line we are adding on
                   // value change for text field.
                   onValueChange = { otp.value = it },

                   // on below line we are adding place holder
                   // as text as "Enter your course duration"
                   placeholder = { Text(text = "Enter your otp") },

                   // on below line we are adding modifier to it
                   // and adding padding to it and filling max width
                   modifier = Modifier
                       .padding(16.dp)
                       .fillMaxWidth(),

                   // on below line we are adding text style
                   // specifying color and font size to it.
                   textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

                   // on below line we are adding
                   // single line to it.
                   singleLine = true,
               )

               // adding spacer on below line.
               Spacer(modifier = Modifier.height(10.dp))

               // on below line creating button to add
               // data to firebase firestore database.
               Button(
                   onClick = {
                       // on below line we are validating
                       // user input parameters.
                       if (TextUtils.isEmpty(otp.value.toString())) {
                           // displaying toast message on below line.
                           Toast.makeText(context, "Please enter otp..", Toast.LENGTH_SHORT)
                               .show()
                       } else {
                           // on below line generating phone credentials.
                           val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
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
                   // on below line we are
                   // adding modifier to our button.
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(16.dp)
               ) {
                   // on below line we are adding text for our button
                   Text(text = "Verify OTP", modifier = Modifier.padding(8.dp))
               }

               // on below line adding spacer.
               Spacer(modifier = Modifier.height(5.dp))

               Text(
                   // on below line displaying message for verification status.
                   text = message.value,
                   style = TextStyle(color = Color.Green, fontSize = 20.sp, fontWeight = FontWeight.Bold)
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

            override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                // this method is called when code is send
                super.onCodeSent(verificationId, p1)
                verificationID.value = verificationId
            }
        }

        }

    }


