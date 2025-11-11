package com.spint.app.screens._1home.publicEvent

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.spint.app.R


@Composable
fun ShareIcon(onSharePostClicked:()->Unit) {
    Column(
        modifier = Modifier
            .clickable { onSharePostClicked() }
            .padding(10.dp)
            .wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.share),
            contentDescription = "",
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(color = Color.White)
        )

    }
}
