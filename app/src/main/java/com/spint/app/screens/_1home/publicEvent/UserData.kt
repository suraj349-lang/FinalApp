package com.spint.app.screens._1home.publicEvent

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.constants.Constants


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserData(username:String,userImage:String) {
    Row(modifier = Modifier
        .fillMaxWidth(0.85f)
        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Card(modifier = Modifier.size(40.dp), shape = CircleShape) {
            GlideImage(model =  imagePrefix+userImage, contentDescription = "", contentScale = ContentScale.FillBounds, modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / 1f))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = username,
            fontFamily = Constants.USER_NAME_FONT,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.8f), // Light glow effect
                    offset = Offset(0f, 0f),
                    blurRadius = 8f
                )
            )
        )
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserDataNewUI(username:String,userImage:String) {
    Row(modifier = Modifier
        .wrapContentSize(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Start) {
        Card(modifier = Modifier.size(40.dp), shape = CircleShape) {
            Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription = "", contentScale = ContentScale.Fit, modifier = Modifier)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = username,
            fontFamily = FontFamily(Font(R.font.dongle_bold)),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.White.copy(alpha = 0.8f), // Light glow effect
                    offset = Offset(0f, 0f),
                    blurRadius = 8f
                )
            )
        )
    }

}
