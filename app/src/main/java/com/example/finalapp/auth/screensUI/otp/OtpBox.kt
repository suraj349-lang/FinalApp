package com.example.finalapp.auth.screensUI.otp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import  androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalapp.auth.authViewModel.AuthViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun OtpBox(){
    val authViewModel= hiltViewModel<AuthViewModel>()
    var otp1 by remember {mutableStateOf("") }
    var otp2 by remember {mutableStateOf("") }
    var otp3 by remember {mutableStateOf("") }
    var otp4 by remember {mutableStateOf("") }
    var otp5 by remember {mutableStateOf("") }
    var otp6 by remember {mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,

    ) {
        OutlinedBox(otp = otp1){
            otp1=it
        }
        OutlinedBox(otp = otp2){
            otp2=it
        }
        OutlinedBox(otp = otp3){
            otp3=it
        }
        OutlinedBox(otp = otp4){
            otp4=it

        }
        OutlinedBox(otp = otp5){
            otp5=it

        }
        OutlinedBox(otp = otp6){
            otp6=it

        }
    }
    if(otp1!="" && otp2!="" && otp3!="" && otp4!="" && otp5!="" && otp6!=""  ) {
        authViewModel.otp = otp1 + otp2 + otp3 + otp4+otp5+otp6
        Log.d("OTP",authViewModel.otp)
    }






}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OutlinedBox(otp:String,onChange:(String)->Unit){
    val focusManager = LocalFocusManager.current
    var keyPressedCount by remember {
        mutableStateOf(0)
//
    }

    OutlinedTextField(
        value = otp,
        onValueChange = {
            if(it.length<=1 && it.isDigitsOnly()){
               onChange(it)
            }
            if(it.length==1) focusManager.moveFocus(FocusDirection.Right)

        },
        modifier = Modifier
            .height(80.dp)
            .width(50.dp)
            .onKeyEvent {
                if (it.key == Key.Backspace) {
                    if (keyPressedCount == 1) {
                        focusManager.moveFocus(FocusDirection.Left)
                        keyPressedCount = 0
                    } else {
                        keyPressedCount++
                    }

                    true
                } else {
                    false
                }
            },
        singleLine = true,
        maxLines = 1,
        textStyle=TextStyle(fontSize = 30.sp, textAlign = TextAlign.Center),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Right)
            }
        )
    )
    Spacer(modifier = Modifier.width(3.dp))
}