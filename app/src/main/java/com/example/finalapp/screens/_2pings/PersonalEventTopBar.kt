package com.example.finalapp.screens._2pings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants

@Composable
fun PingsTopBar(showIcon: Boolean, onSearchIconClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Pings", fontFamily = Constants.FONT_MEDIUM, fontSize = 24.sp, color = floatingActionBtnColor ) },
        navigationIcon = {
                         Image(painter = painterResource(id = R.drawable.back), contentDescription ="", modifier = Modifier.size(28.dp).padding(end=4.dp) )
        },
        actions = {
            if(!showIcon) {
                Image(
                    painter = painterResource(id = R.drawable.search_new),
                    contentDescription = "",
                    modifier = Modifier.padding(end = 16.dp).size(28.dp).clickable { onSearchIconClicked() })
            }
        },
        backgroundColor = Color.White
    )

}
