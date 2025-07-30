package com.example.finalapp.screens._1home.publicEvent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.ui.theme.vectorScreenIcons


@Composable
fun ReactionBar() {
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column{
            Image(
                painter = painterResource(id = R.drawable.view),
                contentDescription = "",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(vectorScreenIcons)
            )
            Text(
                text = "120k",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.dongle_bold))
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(bottom = 8.dp), color = Color.LightGray.copy(alpha = 0.2f))
        Column {
            Image(
                painter = painterResource(id = R.drawable.upvote),
                contentDescription = "",
                modifier = Modifier.size(28.dp),
                colorFilter = ColorFilter.tint(vectorScreenIcons)
            )
            Text(
                text = "12k",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.dongle_bold))
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(bottom = 8.dp), color = Color.LightGray.copy(alpha = 0.2f))
    }
}