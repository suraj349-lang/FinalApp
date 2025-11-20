package com.spint.app.screens.duel

import android.R.attr.rotationY
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.utils.constants.Constants

@Composable
fun FlippingIconLoader(
    modifier: Modifier = Modifier,
    icon: Int, // drawable ID
    text: String = "Searching for a match..."
) {
    // Infinite flip animation
    val rotation = rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 180f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = 12f * density
                }
        ) {
            // Single icon that flips
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = Constants.FONT_LIGHT,
            color = Color.White
        )
    }
}
