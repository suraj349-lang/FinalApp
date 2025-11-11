package com.spint.app.screens.dialogBox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.spint.app.R
import com.spint.app.navigation.SCREENS
import com.spint.app.screens._4profile.ImageCaptureFromCamera
import com.spint.app.viewmodels.ImageUploadViewModel
import com.spint.app.ui.theme.statusAndTopAppBarColor
import com.spint.app.ui.theme.topAppBarTextColor

@Composable
fun DialogBoxForImageEdit(image: Int, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        dismissOnBackPress = true,dismissOnClickOutside = true
    )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 300.dp, max = 400.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
            ) {
                Image(painterResource(id =image), contentDescription = "", modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .padding(8.dp), contentScale = ContentScale.Crop)

                BtnForDialogBoxForImageEdit(onDismiss)
            }
        }
    }
}

@Composable
fun BtnForDialogBoxForImageEdit(onDismiss: () -> Unit){
    Row(Modifier.padding(top = 10.dp)) {
        OutlinedButton(onClick = { onDismiss() },
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1F)) {
            Text(text = "Cancel", color = Color.Red)
        }
        Button(onClick = { },
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1F),
            colors = ButtonDefaults.buttonColors(
                containerColor = statusAndTopAppBarColor,
                contentColor = topAppBarTextColor,
                disabledContainerColor= statusAndTopAppBarColor,
                disabledContentColor= topAppBarTextColor
            )
        ) {
            Text(text = "Upload")
        }
    }

}
//

@Composable
fun DialogBoxForCameraAndGallery(imageUploadViewModel: ImageUploadViewModel, navController:NavHostController, onDismiss: () -> Unit) {
    var key by remember { mutableStateOf(false) }
    if (key) {
        ImageCaptureFromCamera(imageUploadViewModel)
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .background(color = Color(0xFFFFFFFE))
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Choose from Camera / Gallery.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }

                Row(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.size(100.dp)) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(painterResource(id = R.drawable.camera),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable { key = !key }
                            )
                            Text(text = "Camera", modifier = Modifier)

                        }
                    }
                    Box(modifier = Modifier.size(100.dp)) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(painterResource(id = R.drawable.gallery),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        navController.navigate(SCREENS.GALLERY.route)
                                    })
                            Text(text = "Gallery", modifier = Modifier)

                        }
                    }


                }
            }
        }
    }
}