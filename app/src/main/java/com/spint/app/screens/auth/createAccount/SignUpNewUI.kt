package com.spint.app.screens.auth.createAccount



import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.spint.app.R
import com.spint.app.screens.auth.util.BirthdayPicker
import com.spint.app.utils.constants.Constants
import java.net.URLEncoder


@Composable
fun SignupScreenNewUI(
    name: String,
    onNameChange: (String) -> Unit,
    birthDay: String,
    onBirthDayChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    onBackClicked: () -> Unit,
    onTermsAndConditionsClicked:(String)->Unit,
    onSignInClicked: () -> Unit,
    onNextClicked: () -> Unit,
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
    val gender=listOf("Male","Female","Others")


    var checked by remember {
        mutableStateOf(true)
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black
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
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Image(
                        painter = painterResource(id = Constants.APP_ICON),
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
                    text="Create account",
                    fontFamily = Constants.FONT_MEDIUM,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp, color = Color.White, modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                )
                Text(
                    text="Be someone real or don't upto you",
                    fontFamily = Constants.FONT_LIGHT,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp, color = Color.Gray, modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(bottom = 10.dp))
                Text(
                    "FULL NAME",
                    fontFamily = Constants.FONT_LIGHT,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp, color = Color.Gray, modifier = Modifier
                        .fillMaxWidth()
                )
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
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Green,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.Gray,
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
                    value = gender.get(0),
                    onValueChange = {

                    },
                    placeholder = { Text(text = "Gender", fontFamily = Constants.FONT_LIGHT) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = Constants.FONT_MEDIUM
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Green,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.Gray,
                        unfocusedLabelColor = Color.LightGray,
                        focusedLabelColor = Color.LightGray,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ), keyboardActions = KeyboardActions(
                        onNext = { }
                    )
                )
                BirthdayPicker(birthDay,onBirthDayChange)
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
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Green,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.Gray,
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
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.Green,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.Gray,
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
                    TermsAndConditions(
                        onTermsAndConditionsClicked={encodedUrl ->onTermsAndConditionsClicked(encodedUrl)}
                    )
                  }

                Button(
                    onClick = {onNextClicked() },
                    shape = RoundedCornerShape(10.dp),
                    enabled = name.isNotEmpty() && password.isNotEmpty() && confirmPassword==password ,
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
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable { onSignInClicked() }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
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
            modifier = Modifier,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color(0xFFF7E7C2),
                fontWeight = FontWeight.SemiBold,
                fontFamily = Constants.FONT_MEDIUM
            )
        )
    }
}
