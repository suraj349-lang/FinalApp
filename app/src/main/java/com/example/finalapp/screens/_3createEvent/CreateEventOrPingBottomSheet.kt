package com.example.finalapp.screens._3createEvent


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.utils.constants.Constants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventOrPingBottomSheet(showSheet: Boolean, onDismiss: () -> Unit, navHostController: NavHostController) {
    if (showSheet){
        ModalBottomSheet(onDismissRequest = { onDismiss()}) {
            Column(modifier = Modifier.padding(start = 16.dp,end=16.dp, bottom = 60.dp)) {
                Text("Click to add event / ping.", fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = Constants.FONT_MEDIUM)
                Spacer(Modifier.height(12.dp))
                Row() {
                    Button(
                        onClick = {
                            navHostController.navigate(SCREENS.CREATE_EVENT.route)
                            onDismiss()
                                  },
                        modifier = Modifier.wrapContentWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Image(painter = painterResource(id = R.drawable.event), contentDescription = "", modifier = Modifier.size(20.dp))
                            Text("Event", fontFamily = Constants.DONGLE_BOLD, color = Color.White, fontSize = 20.sp)
                        }
                    }
                    Spacer(Modifier.width(40.dp))

                    Button(
                        onClick = {
                            navHostController.navigate(SCREENS.CREATE_PING.route)
                            onDismiss()
                        },
                        modifier = Modifier.wrapContentWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Image(painter = painterResource(id = R.drawable.ping), contentDescription = "", modifier = Modifier.size(20.dp))
                            Text("Ping", fontFamily = Constants.DONGLE_BOLD, color = Color.White, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }}
