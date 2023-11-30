package com.example.finalapp.screens.DialogBOX

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.finalapp.R
import com.example.finalapp.ui.theme.DarkBlue
import com.example.finalapp.ui.theme.statusAndTopAppBarColor
import com.example.finalapp.ui.theme.topAppBarTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAlertDialog(onDismiss: () -> Unit) {
    var offerTextField:String by remember{ mutableStateOf("") }

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
                    .heightIn(min=300.dp,max=400.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min=120.dp,max=180.dp)
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                        .background(Color.White, RoundedCornerShape(5.dp)),
                    shape = RoundedCornerShape(5.dp),
                    value = offerTextField,
                    onValueChange = { offerTextField = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    maxLines = 12,
                    textStyle = MaterialTheme.typography.displayMedium
                )
                Text(text = "Suggestions : ", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium, color = DarkBlue, modifier = Modifier.padding(start = 8.dp))
                Row(modifier = Modifier.padding(start=8.dp, bottom = 4.dp).fillMaxWidth().height(24.dp).clickable {
                    offerTextField="Coffee Date"
                }, verticalAlignment = Alignment.Bottom)
                {
                    Icon(painter = painterResource(id = R.drawable.coffee), contentDescription ="coffee", modifier = Modifier.size(21.dp), tint = Color(
                        0xFFD1830F
                    )
                    )
                    Text(text = "Coffee Date", style = MaterialTheme.typography.bodySmall, fontSize = 22.sp, modifier = Modifier.padding(top=3.dp, start = 4.dp), color = statusAndTopAppBarColor)
                    
                }
                Row(modifier = Modifier.padding(start=8.dp, bottom = 4.dp).fillMaxWidth().height(24.dp).clickable {
                    offerTextField="Movie Date"
                }, verticalAlignment = Alignment.Bottom)
                {
                    Icon(painter = painterResource(id = R.drawable.popcorn_24), contentDescription ="MOvie", modifier = Modifier.size(21.dp), tint = Color(
                        0xFFDB2261
                    )
                    )
                    Text(text = "Movie Date", style = MaterialTheme.typography.bodySmall, fontSize = 22.sp, modifier = Modifier.padding(top=3.dp, start = 4.dp), color = statusAndTopAppBarColor)

                }
                Row(modifier = Modifier.padding(start=8.dp, bottom = 4.dp).fillMaxWidth().height(24.dp).clickable {
                    offerTextField="Trip Date"
                }, verticalAlignment = Alignment.Bottom)
                {
                    Icon(painter = painterResource(id = R.drawable.luggage), contentDescription ="Trip", modifier = Modifier.size(21.dp), tint = Color(
                        0xFF0683E6
                    )
                    )
                    Text(text = "Trip Date", style = MaterialTheme.typography.bodySmall, fontSize = 22.sp, modifier = Modifier.padding(top=3.dp, start = 4.dp), color = statusAndTopAppBarColor)

                }
                Row(modifier = Modifier.padding(start=8.dp, bottom = 4.dp).fillMaxWidth().height(24.dp).clickable {
                    offerTextField=" Figure Out Date"
                }, verticalAlignment = Alignment.Bottom)
                {
                    Icon(painter = painterResource(id = R.drawable.meeting), contentDescription ="Figure Out Date", modifier = Modifier.size(21.dp), tint = Color(
                        0xFFE71808
                    )
                    )
                    Text(text = "Figure Out Date", style = MaterialTheme.typography.bodySmall, fontSize = 22.sp, modifier = Modifier.padding(top=3.dp, start = 4.dp), color = statusAndTopAppBarColor)

                }
                Row(Modifier.padding(top = 10.dp)) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancel", color = Color.Red)
                    }


                    Button(
                        onClick = { },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = statusAndTopAppBarColor,
                            contentColor = topAppBarTextColor,
                            disabledContainerColor=statusAndTopAppBarColor,
                            disabledContentColor=topAppBarTextColor
                        )
                    ) {
                        Text(text = "Upload")
                    }
                }


            }
        }
    }
}