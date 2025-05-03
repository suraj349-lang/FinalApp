package com.example.finalapp.screens._3createEvent


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.utils.AppIcons.CAMERA_ICON
import com.example.finalapp.utils.AppIcons.GALLERY_ICON
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventBottomSheet(  showSheet: Boolean,
                             onDismiss: () -> Unit,
                             navHostController: NavHostController) {
    if (showSheet){
        ModalBottomSheet(onDismissRequest = { onDismiss()}) {
            Column(modifier = Modifier.padding(start = 16.dp,end=16.dp, bottom = 60.dp)) {
                Text("Create Event", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Constants.FONT_MEDIUM)
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { navHostController.navigate(SCREENS.CREATE_EVENT_PUBLIC.route) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("📢 Public Event", fontFamily = Constants.DONGLE_BOLD, color = Color.White ,fontSize = 20.sp)
                }
                Spacer(Modifier.height(18.dp))

                Button(onClick = {
                  //  navHostController.navigate(SCREENS.CREATE_EVENT_PRIVATE.route)
                    navHostController.navigate(SCREENS.CREATE_EVENT_PRIVATE.route)
                    onDismiss()
                }, modifier = Modifier
                    .fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("🔒 Personal Event", fontFamily = Constants.DONGLE_BOLD,color= Color.White, fontSize = 20.sp)
                }
            }
        }
    }}
