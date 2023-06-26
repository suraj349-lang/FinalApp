package com.example.finalapp.auth.screensUI

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalapp.R
import com.example.finalapp.auth.screensUI.otp.OtpInputField


@Composable
fun EnterOTPScreenUI(navController: NavController= NavController(LocalContext.current)){
    var otpValue by remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.baseline_verified_user_24),
                contentDescription = "",
                modifier = Modifier.size(60.dp),
                colorFilter = ColorFilter.tint(Color(0xFFB8350C))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Enter OTP", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(
                0xFF0A5C81
            )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OtpInputField(otpLength = 4, onOtpChanged ={otp->otpValue=otp} )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Text(text = "Didn't Receive OTP?")
                TextButton(onClick = {  }) {
                    Text(
                        text = "Resend SMS",
                        fontSize = 16.sp,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
            }

            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(6.dp)
               // colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
                ) {
                Text(text = "Verify OTP")
            }


        }

    }
}