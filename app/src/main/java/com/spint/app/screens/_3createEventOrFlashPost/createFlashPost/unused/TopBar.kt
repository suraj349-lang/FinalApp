package com.spint.app.screens._3createEventOrFlashPost.createFlashPost.unused

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.spint.app.R
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePingTopBar(showButton:Boolean, onButtonClicked:()->Unit) {
    TopAppBar(
        title = { Text(text = "Create Ping", fontFamily = Constants.FONT_MEDIUM, color = Color.White) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = floatingActionBtnColor),
        actions = {
            if(showButton) {
                Button(onClick = { onButtonClicked()}) {
                    Text(text = "Done", fontFamily = Constants.FONT_MEDIUM)
                }
            } },
        navigationIcon = {
            Image(painter = painterResource(id = R.drawable.back), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(Color.White)) }
    )
}
