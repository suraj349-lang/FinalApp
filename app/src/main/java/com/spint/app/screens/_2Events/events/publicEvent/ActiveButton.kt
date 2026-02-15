package com.spint.app.screens._2Events.events.publicEvent

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.utils.constants.Constants


@Composable
fun ActiveButton() {
    val scale by rememberInfiniteTransition(label = "scale")
        .animateFloat(
            initialValue = 1f,
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "scale"
        )
    val alpha by rememberInfiniteTransition(label = "alpha")
        .animateFloat(
            initialValue = 1f,
            targetValue = 0.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "alpha"
        )
    Card(
        modifier = Modifier
            .wrapContentSize()
           // .graphicsLayer(scaleX = scale, scaleY = scale)
            .alpha(alpha)
        ,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(0xFF6C6F1E).copy(alpha = 0.9f), elevation = 0.dp
    ) {
        Text(
            text = "Active",
            fontFamily = Constants.USER_NAME_FONT,
            fontSize = 10.sp,
            color = Color.White,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
        )
    }
}
