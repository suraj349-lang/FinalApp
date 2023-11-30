package com.example.finalapp.screens.DialogBOX

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.finalapp.R
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor

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
                Image(painterResource(id =image), contentDescription = "", modifier = Modifier.height(300.dp).fillMaxWidth().padding(8.dp), contentScale = ContentScale.Crop)

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