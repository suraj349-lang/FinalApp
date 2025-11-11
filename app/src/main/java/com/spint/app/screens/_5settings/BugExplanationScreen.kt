package com.spint.app.screens._5settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.screens.common.BackImage
import com.spint.app.utils.constants.Constants
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.spint.app.ui.theme.floatingActionBtnColor


@Composable
fun BugExplanationScreen(onBackClicked:()->Unit) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val isFocused = remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 16.dp, vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.CenterVertically){
                    BackImage {
                        onBackClicked()
                    }
                    Text(text = "Bug", fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.SemiBold, fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isFocused.value) Color.White else Color.Gray.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .heightIn(min = 150.dp) // 6 lines in height (each line around 16dp)
                            .padding(16.dp)
                            .onFocusChanged { focusState ->
                                isFocused.value = focusState.isFocused
                            },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = if (isFocused.value) Color.Black else Color.Gray
                        ),
                        singleLine = false,
                        maxLines = 10, // Ensures the text field has a max of 6 lines
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (text.isEmpty() && !isFocused.value) {
                                    Text(
                                        text = "What issue did you have?How did it happen?The more details the better!", // Placeholder text
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            color = Color.Gray.copy(alpha = 0.6f),
                                            fontFamily=Constants.FONT_MEDIUM// Lighter gray color for placeholder
                                        ),
                                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                                    )
                                }
                                innerTextField() // This is where the actual user input happens
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
//                Text(text = "This is related to ...", fontFamily = Constants.FONT_MEDIUM, fontSize = 12.sp, color = Color(0xFF121212),modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp))

            }
            Column( modifier = Modifier.align(Alignment.BottomCenter)) {
                PrivacyPolicyText()
                Button(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),enabled = text.trim().isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(text.trim().isNotEmpty()) floatingActionBtnColor else Color.LightGray,
                        contentColor = if(text.trim().isNotEmpty()) Color.White else Color.Gray,
                    )
                ) {
                    Text(text = "Submit",fontSize = 14.sp, color = Color.LightGray, fontFamily = Constants.FONT_MEDIUM)

                }
            }

        }

    }
}

@Composable
fun PrivacyPolicyText() {
    val context = LocalContext.current
    // Create the AnnotatedString with clickable part and styled text
    val annotatedString = buildAnnotatedString {
        append("To give us more context about your report, it'll include some information about your device and ${Constants.APP_NAME} account. To learn what info we use and how, ")

        // Mark the "Privacy policy" part as clickable with a tag
        pushStringAnnotation(tag = "privacy_policy", annotation = "") /*TODO*/
        pushStyle(style = SpanStyle(color = floatingActionBtnColor, fontWeight = FontWeight.Bold))
        // Style the clickable "Privacy policy" text as blue and bold
        append("Privacy policy")

        // Pop the annotation tag and leave the rest of the text normal
        pop()

        append(".")
    }

    // Wrap ClickableText in a Box to center it
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // ClickableText will listen for click on the "Privacy policy" part
        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                // Check if the click is on the "privacy_policy" part
                annotatedString.getStringAnnotations(tag = "privacy_policy", start = offset, end = offset)
                    .firstOrNull()?.let { annotation ->
                        // Handle the click and open the URL, navigate, or perform an action
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                        context.startActivity(intent)
                    }
            },
            style = TextStyle(
                fontSize = 10.sp,
                color = Color.Gray.copy(alpha = 0.7f),
                fontFamily = Constants.FONT_LIGHT,
                lineHeight = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}
