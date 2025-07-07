package com.example.finalapp.screens._4profile.privateUsername

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.model.DropProfileModel
import com.example.finalapp.screens._1home.commonUI.OfferResponseDataAndAction
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.ui.imagePickerText
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.RequestState
import com.example.finalapp.utils.constants.Constants
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
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Enter password to access private username",
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        if (password.length <= 5) {
                            password = it
                        }
                    },
                    placeholder = {
                        Text(
                            text = "******",
                            fontFamily = Constants.FONT_MEDIUM,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 16.sp
                        )
                    },
                    textStyle = TextStyle(letterSpacing = 16.sp),
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { onEnterClicked(password) },
                        modifier=Modifier.clip(RoundedCornerShape(6.dp)),
                        enabled= password.length==6,
                        colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor,
                            contentColor = Color.White)
                    ) {
                        Text(
                            text = "Enter ->", fontFamily = Constants.FONT_MEDIUM,
                        )
                    }
                }
            }
        }
    }
}