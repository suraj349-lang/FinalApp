package com.spint.app.screens.auth



import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R
import com.spint.app.screens.auth.util.OtpInputField
import com.spint.app.utils.constants.Constants


@Composable
fun SignupScreenNewUI(
    name:String,
    onNameChange: (String) -> Unit,
    password:String,
    onPasswordChange:(String)->Unit,
    confirmPassword:String,
    onConfirmPasswordChange:(String)->Unit,
    onBackClicked: () -> Unit,
    onSignInClicked: () -> Unit,
    onNextClicked: () -> Unit
) {

    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val icon = if (passwordVisibility)
        painterResource(id = R.drawable.round_visibility_24)
    else
        painterResource(id = R.drawable.round_visibility_off_24)

    var confirmPasswordVisibility by rememberSaveable { mutableStateOf(false) }

    val confirmPasswordShowIcon = if (confirmPasswordVisibility)
        painterResource(id = R.drawable.round_visibility_24)
    else
        painterResource(id = R.drawable.round_visibility_off_24)

    val keyboardController = LocalSoftwareKeyboardController.current


    var checked by remember {
        mutableStateOf(true)
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
//                            Color(0xFF1976D2), Color(0xFFE9D8AE)
//                            Color(0xFFFBC02D), Color(0xFFDCDEF0)
                            Color(0xFF0288D1), Color(0xFF7B1FA2)
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
                        fontSize = 20.sp, color = Color.White
                    )

                }
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily =
                                Constants.FONT_LIGHT,
                                fontWeight = FontWeight.Normal,
                                fontSize = 30.sp, color = Color.White
                            )
                        ) {
                            append("Sign up to")
                        }

                        withStyle(
                            style = SpanStyle(
                                fontFamily =
                                Constants.FONT_MEDIUM,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp, color = Color.White
                            )
                        ) {
                            append(" continue!")
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 30.dp)
                )

                Spacer(modifier = Modifier.padding(bottom = 10.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        onNameChange(it)
                    },
                    placeholder = { Text(text = "Full Name", fontFamily = Constants.FONT_LIGHT) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM
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
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ), keyboardActions = KeyboardActions(
                        onNext = { })


                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        onPasswordChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM
                    ),
                    placeholder = { Text(text = "Password", fontFamily = Constants.FONT_LIGHT) },
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
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        if (password.isNotEmpty()) {
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
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        onConfirmPasswordChange(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM
                    ),
                    placeholder = { Text(text = "Confirm Password", fontFamily = Constants.FONT_LIGHT) },
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
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        if (confirmPassword.isNotEmpty()) {
                            IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                                Icon(
                                    painter = confirmPasswordShowIcon,
                                    contentDescription = "Password visibility icon",
                                    tint = Color.Black
                                )
                            }
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None
                    else PasswordVisualTransformation()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Checkbox(checked = checked, onCheckedChange = { checked = !checked })
                    TermsAndConditions()
                  }

                Button(
                    onClick = {onNextClicked() },
                    shape = RoundedCornerShape(10.dp),
                    elevation=ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                       , colors = ButtonDefaults.buttonColors(
                        containerColor = if (name
                                .trim().length == 10
                        ) Color.White else Color(0xFFC2185B)//Color(0xFF285703)
                    )
                ) {
                    Text(
                        text =  "Next",
                        modifier = Modifier.padding(8.dp),
                        fontSize =16.sp,
                        fontFamily = Constants.FONT_MEDIUM,
                        color = if (name.trim().length == 10) {
                            Color.White
                        } else {
                            Color.Gray
                        }
                    )
                }
                SignInText(onSignInClicked)
            }
        }
}




@Composable
fun SignInText(onSignInClicked: () -> Unit) {
    Row {
        Text(
            text = "Have an account? ",
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.LightGray,
                fontFamily = Constants.FONT_LIGHT
            )
        )
        Text(
            text = "Sign in",
            modifier = Modifier.clickable { onSignInClicked() },
            style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFFF7E7C2),
                fontWeight = FontWeight.SemiBold,
                fontFamily = Constants.FONT_MEDIUM
            )
        )
    }
}

@Composable
fun TermsAndConditions() {
    val context= LocalContext.current
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.7f), fontSize = 8.sp,fontWeight = FontWeight.Normal, fontFamily = Constants.FONT_LIGHT)){
            append("By proceeding to create your account, you are agreeing to our ")
        }

        pushStringAnnotation(tag = "URL", annotation = "https://finalapp-3494.web.app/")
        withStyle(style = SpanStyle(color = Color.Yellow, fontWeight = FontWeight.SemiBold,fontSize = 8.sp, fontFamily = Constants.FONT_LIGHT)) {
            append("Terms of Services & Privacy Policy")
        }
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                    context.startActivity(intent)
                }
        }
    )


}


@Composable
fun UserNameAndPhone(phoneNumber:String,onPhoneNumberChange:(String)->Unit,userName:String,onUserNameChange:(String)->Unit,otp:String,onOtpChange:(String)->Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        OutlinedTextField(
            value = userName,
            onValueChange = {
                onUserNameChange(it)
            },
            placeholder = { Text(text = "Username", fontFamily = Constants.FONT_LIGHT) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontFamily = Constants.FONT_MEDIUM
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
                keyboardType = KeyboardType.Number,
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
                fontFamily = Constants.FONT_MEDIUM
            ),
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
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Didn't Receive OTP?", fontFamily = Constants.FONT_LIGHT, fontSize = 12.sp, color = Color.Gray)
            TextButton(onClick = { }) {
                Text(
                    text = "Resend SMS",
                    fontSize = 16.sp,
                    fontFamily = Constants.FONT_LIGHT,
                    color = Color.Black,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }

        Button(
            onClick = {

            },
            shape = RoundedCornerShape(6.dp)
            , colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC2185B), contentColor = Color.White)
        ) {
            Text(text = "Verify OTP",fontFamily = Constants.FONT_MEDIUM, fontSize = 14.sp, color = Color.Black)
        }

    }

}