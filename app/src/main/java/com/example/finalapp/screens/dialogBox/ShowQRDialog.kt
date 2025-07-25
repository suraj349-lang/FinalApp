package com.example.finalapp.screens.dialogBox

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.finalapp.R
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.screens._1home.commonUI.shareDeepLink
import com.example.finalapp.screens._1home.publicEvent.ShareIcon
import com.example.finalapp.screens.qrcode.QRCode
import com.example.finalapp.ui.imagePrefix
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.ProfileObject
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.testdata.DynamicText


@Composable
fun ShowQRDialog(navController: NavHostController, onDismiss: () -> Unit) {
    val context= LocalContext.current
    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        dismissOnBackPress = true,dismissOnClickOutside = true
    )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.DarkGray) //0xFF1C1B2F
        ) {
            Column(modifier = Modifier
                .wrapContentSize()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = Constants.APP_NAME,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    color = Constants.SPLASH_SCREEN_COLOR,
                    fontSize = 20.sp,
                    fontFamily = Constants.APP_NAME_FONT
                )

                QRCode(userIMAGE = ProfileObject.profile.profileImage,userId = ProfileObject.profile.userId)

                DynamicText(text = ProfileObject.profile.username, color = Color.White, fontSize = 16.sp, fontFamily = Constants.FONT_MEDIUM, fontWeight = FontWeight.Bold)

                Divider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp, color = Color(0xFF745509))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {

                    Image(painter = painterResource(id = R.drawable.color_camera), contentDescription ="", modifier = Modifier.size(40.dp), colorFilter = ColorFilter.tint(Color.Transparent) )

                    Column(modifier = Modifier
                        .clickable {
                            navController.navigate("qrcode")
                        }
                        .wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.color_camera), contentDescription ="", modifier = Modifier.size(40.dp) )

                        DynamicText(text = "Scan QR", color = Color.White)
                    }
                    Column(modifier = Modifier
                        .clickable {
                            shareDeepLink(context, ProfileObject.profile.userId)
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



enum class showDialog{
    OPEN,
    CLOSE
}