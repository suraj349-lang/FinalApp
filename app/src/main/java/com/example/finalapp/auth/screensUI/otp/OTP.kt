package com.example.finalapp.auth.screensUI.otp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpInputField(
    otpLength: Int,
    onOtpChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    // 0998779
    var otpValue by remember { mutableStateOf("") }

    val keyboardState = keyboardAsState(KeyboardStatus.Closed)

    val isShowWarning by remember(keyboardState) {
        derivedStateOf {
            if (keyboardState.value == KeyboardStatus.Closed) {
                if (otpValue.length != otpLength) {
                    return@derivedStateOf true
                }
            }
            false

        }
    }

    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        modifier = Modifier.focusRequester(focusRequester),
        value = otpValue, onValueChange = { value ->
            if (value.length <= otpLength) {
                otpValue = value
                onOtpChanged(otpValue)
            }
        },
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.width(200.dp)) {
                repeat(otpLength) { index ->
                    // 6
                    // 4 5 2 <<< otpValue = "452
                    val char = when {
                        index >= otpValue.length -> ""
                        else -> otpValue[index].toString()
                    }

                    val isFocus = index == otpValue.length
                    OtpCell(
                        char = char,
                        isFocus = isFocus,
                        isShowWarning = isShowWarning,
                        modifier = Modifier.weight(
                            1f
                        )
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()

        }
        )
    )

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

}

@Composable
fun OtpCell(
    char: String,
    isFocus: Boolean,
    isShowWarning: Boolean,
    modifier: Modifier = Modifier
) {

    val borderColor = if (isShowWarning) {
        Color(0xFFB30707)
       // MaterialTheme.colorScheme.error
    } else if (isFocus) {
        Color.Black

       // MaterialTheme.colorScheme.primary
    } else {
        Color.LightGray
       // MaterialTheme.colorScheme.secondary
    }

    Surface(
        modifier = modifier.width(10.dp).height(60.dp)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(6.dp))
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            ),
            fontSize=30.sp,
            modifier = Modifier.wrapContentSize(align = Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OtpInputFieldPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            OtpInputField(otpLength = 6, onOtpChanged = {})
        }
    }
}

@Preview(name = "OptCell Focus", showBackground = true)
@Composable
fun OtpCellFocusPreview(
) {

    MaterialTheme {
        Box(modifier = Modifier.padding(24.dp)) {
            OtpCell(char = "6", isFocus = true, isShowWarning = false)
        }
    }

}