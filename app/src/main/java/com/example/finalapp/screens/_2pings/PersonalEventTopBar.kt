package com.example.finalapp.screens._2pings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants

@Composable
fun PingsTopBar(backgroundColor: Color, showIcon: Boolean, onBackClicked:()->Unit={},onSearchIconClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Pings", fontSize = 24.sp, color = Color.White , fontFamily = Constants.FONT_EXTRA_LIGHT) },
        navigationIcon = {
                         Image(painter = painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription ="", colorFilter = ColorFilter.tint(
                             Color.White), modifier = Modifier.clickable { onBackClicked() }.size(28.dp).padding(end=4.dp) )
        },
        actions = {
            if(!showIcon) {
                Image(
                    painter = painterResource(id = R.drawable.search_new_filled),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(
                        Color.White),
                    modifier = Modifier.padding(end = 16.dp).size(28.dp).clickable { onSearchIconClicked() })
            }
        },
        backgroundColor = backgroundColor,
        modifier = Modifier.statusBarsPadding()
    )

}
