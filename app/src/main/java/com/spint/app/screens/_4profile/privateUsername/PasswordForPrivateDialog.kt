package com.spint.app.screens._4profile.privateUsername

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.spint.app.R
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Preview(showBackground = true)
@Composable
fun PasswordForPrivateUsername(onEnterClicked:(String)->Unit={},onDismiss:()->Unit={}) {
    var password by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    var passwordVisibility by remember { mutableStateOf(false) }
    val icon =
        if (passwordVisibility) painterResource(id = R.drawable.round_visibility_24) else painterResource(
            id = R.drawable.round_visibility_off_24
        )

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Card(
            shape = RoundedCornerShape((6.dp)),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F3F3))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                Card(
                    shape = RoundedCornerShape(topEnd = 6.dp, topStart = 6.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = CardDefaults.cardColors(containerColor = floatingActionBtnColor)
                ) {
                    Text(
                        text = "Enter password to access private profile",
                        fontFamily = Constants.FONT_MEDIUM,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        lineHeight=18.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        if (it.length <= 6) {
                            password = it
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp).padding(top=20.dp),
                    placeholder = {
                        Text(
                            text = "******",
                            fontFamily = Constants.FONT_MEDIUM,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 16.sp
                        )
                    },
                    textStyle = TextStyle(letterSpacing = 16.sp, color = Color.Black, fontSize = 20.sp, fontFamily = Constants.FONT_MEDIUM),
                    trailingIcon = {
                        if (password.isNotEmpty()) {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                androidx.compose.material3.Icon(
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
                Row(modifier = Modifier.padding(bottom = 20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { onEnterClicked(password) },
                        shape = RoundedCornerShape(6.dp),
                        modifier=Modifier.padding(top=16.dp),
                        enabled= password.length==6,
                        colors = ButtonDefaults
                            .buttonColors(
                                containerColor = Color(0xFF689F38),
                                contentColor = Color.White,
                                disabledContainerColor = Color.DarkGray,
                                disabledContentColor = Color.White
                            )
                    ) {
                        Text(
                            text = "Enter", fontFamily = Constants.FONT_LIGHT, fontSize = 14.sp,color=Color.White
                        )
                    }
                }
            }
        }
    }
}