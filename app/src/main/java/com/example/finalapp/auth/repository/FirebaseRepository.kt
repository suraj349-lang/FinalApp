package com.example.finalapp.auth.repository

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.finalapp.navigation.SCREENS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit



class FirebaseRepository {
    // on below line creating method to
// sign in with phone credentials.
     fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        auth: FirebaseAuth,
        activity: Activity,
        context: Context,
        message: MutableState<String>,
        navController: NavController
    ) {
        // on below line signing with credentials.
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                // displaying toast message when
                // verification is successful
                if (task.isSuccessful) {
                    message.value = "Verification successful"
                    Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
                    navController.navigate(SCREENS.HOME.route)
                } else {
                    // Sign in failed, display a message
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code
                        // entered was invalid
                        Toast.makeText(
                            context,
                            "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    // below method is use to send
// verification code to user phone number.
     fun sendVerificationCode(
        number: String,
        auth: FirebaseAuth,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        // on below line generating options for verification code
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}