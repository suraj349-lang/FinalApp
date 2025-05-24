package com.example.finalapp.screens.dialogBox

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.finalapp.R
import com.example.finalapp.screens.qrcode.QRCode
import com.example.finalapp.utils.ProfileObject


@Composable
fun ShowQRDialog(image:Int, navController: NavHostController, onDismiss: () -> Unit) {


    val scope= rememberCoroutineScope()
    var enabled=true;


    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        dismissOnBackPress = true,dismissOnClickOutside = true
    )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.wrapContentSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//               Image(painter = painterResource(id = image), contentDescription ="" )
                QRCode(userId = ProfileObject.profile?.userId!!)
                Column(modifier = Modifier.clickable {
                    navController.navigate("qrcode")
                }.wrapContentSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.camera), contentDescription ="", modifier = Modifier.size(40.dp) )

                    Text("Scan QR")
                }

            }
        }
    }
}



enum class showDialog{
    OPEN,
    CLOSE
}