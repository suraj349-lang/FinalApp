package com.example.finalapp.screens.dialogBox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.screens._1home.commonUI.shareDeepLink
import com.example.finalapp.screens.qrcode.QRCode
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.UserObject
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.testdata.DynamicText


@Composable
fun ShowQRDialog(navController: NavHostController, onDismiss: () -> Unit) {
    val context= LocalContext.current
    val user by UserObject.user.collectAsState()
    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
//                    brush = Brush.sweepGradient(
//                        colors = listOf(
//                            Color(0xFF689F38), Color(0xFFAFB42B)
//                        )
//                    )
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0288D1), Color(0xFF310842)//0xFF7B1FA2
                          //  Color(0xFFFBC02D), Color(0xFF4559DA)
                           // Color(0xFFFBC02D), Color(0xFFFFA000)
                        )
                    )
                )
        ) {
            Column(modifier = Modifier
                .wrapContentSize()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = Constants.APP_NAME,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = Constants.APP_NAME_FONT
                )
                Text(
                    text = "Get the qr code scanned to connect to you.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    color = Color.White,
                    fontSize =11.sp,
                    fontFamily = Constants.FONT_EXTRA_LIGHT
                )

                QRCode(userIMAGE = user.profileImage,userId = user.user)

                DynamicText(text = user.userName, color = Color.White, fontSize = 16.sp, fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.Bold)

                Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color(0xFF745509))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {

                    Image(painter = painterResource(id = R.drawable.color_camera), contentDescription ="", modifier = Modifier.size(40.dp), colorFilter = ColorFilter.tint(Color.Transparent) )

                    Column(
                        modifier = Modifier
                            .clickable { navController.navigate("qrcode") }
                            .wrapContentSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.color_camera),
//                            contentDescription = "",
//                            modifier = Modifier.size(50.dp)
//                        )
                        Card(modifier = Modifier.size(50.dp), shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                             Card(modifier = Modifier
                                 .padding(4.dp)
                                 .size(50.dp)
                                 .clip(CircleShape), colors = CardDefaults.cardColors(containerColor = floatingActionBtnColor)) {

                             }
                        }

                        DynamicText(text = "Scan QR", color = Color.White)
                    }
                    Column(modifier = Modifier
                        .clickable {
                            shareDeepLink(context, user.user)
                        }
                        .wrapContentSize(), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.share), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(Color.White) )

                        DynamicText(text = "Share", color = Color.White, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}



enum class ShowDialog{
    OPEN,
    CLOSE
}