package com.example.finalapp.screens.auth

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.viewmodels.AuthViewModel
import com.example.finalapp.repository.SignUpAuthRepo
import com.example.finalapp.screens.auth.util.OtpBox
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.TAG
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun SignupScreenUI(navController: NavController = NavController(LocalContext.current)) {

    val signUpAuthRepo = SignUpAuthRepo()
    val authViewModel= hiltViewModel<AuthViewModel>()
    val focusManager = LocalFocusManager.current
    val passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.round_visibility_24)
    else
        painterResource(id = R.drawable.round_visibility_off_24)
    val keyboardController = LocalSoftwareKeyboardController.current
    val phoneNumber = remember { mutableStateOf("") }
    val otp = remember { mutableStateOf("") }
    val verificationID = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    val context = LocalContext.current
    val key = remember { mutableStateOf(0) }
    val scope=rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFFFFFF)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Constants.APP_NAME,
                fontSize = 45.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp),
                color = Color.Black,
                fontFamily = Constants.APP_NAME_FONT
            )
            Text(
                text = "Dynamic,Live,Real",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 0.dp, start = 120.dp),
                color = Color(0xFF000000),
                fontFamily =Constants.FONT_MEDIUM
            )
            if (key.value==0) {
                OutlinedTextField(
                    value = phoneNumber.value,
                    onValueChange = {
                        if (it.length <= 10) phoneNumber.value = it
                        else Toast.makeText(context, "Can be 10 digits only !", Toast.LENGTH_SHORT)
                            .show()
                    },
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM
                    ),
                    label = {
                        Text(
                            text = if (phoneNumber.value.isEmpty()) "Enter number..." else "Number",
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
                        imeAction = ImeAction.Next
                    ), keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus();keyboardController?.hide() })
                    //visualTransformation = CameroonNumberVisualTransformation(),


                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        // on below line we are validating user inputs
                        if (TextUtils.isEmpty(phoneNumber.value)) {
                            Toast.makeText(context, "Please enter phone number..", Toast.LENGTH_SHORT).show()
                        } else {

                            val number = "+91${phoneNumber.value}"
                            key.value = 1;
                            // on below line calling method to generate verification code.
                            signUpAuthRepo.sendVerificationCode(
                                number,
                                mAuth,
                                context as Activity,
                                callbacks
                            )
                        }
                    },
                    modifier = Modifier.width(160.dp), colors = ButtonDefaults.buttonColors(containerColor = if(phoneNumber.toString().trim().length==10) Color.White else Color(
                        0xFF112402
                    )
                    )) {
                    Text(
                        text = "Generate OTP",
                        modifier = Modifier.padding(8.dp),
                        fontFamily = Constants.FONT_MEDIUM,
                        color = if (phoneNumber.toString().trim().length == 10){
                            Color.LightGray
                        } else {
                            Color.White
                        }
                    )
                }
            }
            // adding spacer on below line.
            Spacer(modifier = Modifier.height(10.dp))

            if (key.value==1) {
                OtpBox()
                Button(
                    onClick = {
                        if (TextUtils.isEmpty(authViewModel.otp.value)) {
                            Toast.makeText(context, "Please enter otp..", Toast.LENGTH_SHORT).show()
                            scope.launch {
                                keyboardController?.hide()
                            }


                        } else {
                            // on below line generating phone credentials.
                            scope.launch {
                                keyboardController?.hide()
                            }

                            val credential: PhoneAuthCredential =
                                PhoneAuthProvider.getCredential(verificationID.value, authViewModel.otp.value)

                            // on below line signing within credentials.
                            signUpAuthRepo.signInWithPhoneAuthCredential(
                                credential,
                                mAuth,
                                context as Activity,
                                context,
                                message,
                                navController
                            )
                        }
                    },
                    modifier = Modifier.wrapContentWidth(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = if(otp.value.length==6) Color(0xFFFFFFFF) else Color.Black,
                        containerColor = if(otp.value.length==6) Color(0xFF112202) else Color.LightGray
                    )
                ) {
                    Text(text = "Submit", fontFamily = Constants.FONT_MEDIUM)
                }
            }

          //  if(message.value!="" && message.value !="Verification successful") Toast.makeText(context,message.value,Toast.LENGTH_SHORT).show()
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
                Log.e(TAG, "onVerificationFailed: ${p0.message}", p0)
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


