package com.spint.app.screens._2Events.events.templates.debate


import android.net.Uri
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.spint.app.R



@OptIn(UnstableApi::class)
@Composable
fun AutoPlayVideo(
    @RawRes videoRes: Int,
    modifier: Modifier = Modifier,
    isMuted: Boolean = true,
    isLooping: Boolean = true
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/$videoRes")
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            volume = if (isMuted) 0f else 1f
            repeatMode =
                if (isLooping) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        modifier = modifier.fillMaxSize()
    )
}


@Composable
fun ReelOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                "Entertainment",
                modifier = Modifier,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
//            Text(
//                text = "This is a reels style static screen 🔥",
//                color = Color.White,
//                fontSize = 16.sp
//            )
        }

        Box(Modifier.padding(16.dp).align(Alignment.BottomStart)
            .wrapContentSize()
        ) {
            Column(modifier = Modifier.wrapContentSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
                Row (modifier = Modifier
                    .wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "MaisieWilliams",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Image(
                        painter = painterResource(R.drawable.verified_new_white),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
//                    Row (modifier = Modifier
//                        .wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)
//                    ) {
//                        Image(
//                            painter = painterResource(R.drawable.entertainment),
//                            contentDescription = null,
//                            modifier = Modifier.size(24.dp)
//                        )
//
//                    Text(
//                        "s::entertainment",
//                        modifier = Modifier,
//                        fontSize = 14.sp,
//                        color = Color.White,
//                        fontWeight = FontWeight.SemiBold
//                    )
//
//                }

            }

        }

        Column(
            modifier = Modifier
                .padding(bottom = 30.dp)
                .padding(4.dp)
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally)  {
                Box(Modifier
                    .wrapContentSize()
                    .clip(CircleShape)
                    .size(60.dp)) {
                    Image(
                        painter = painterResource(R.drawable.profile_colored),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Image(painter = painterResource(R.drawable.add),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.BottomEnd)
                    )
                }
                Text("Follow", modifier = Modifier, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(10.dp))
            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(R.drawable.heart), contentDescription = null, modifier = Modifier.size(30.dp))
                Text("12.6 K", modifier = Modifier, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(10.dp))
            //see the content of people to this content , like used this reel as a base to create their reel
            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(R.drawable.add_link), contentDescription = null, modifier = Modifier.size(30.dp))
                Text("512", modifier = Modifier, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(10.dp))
            // people who dropped their profile at this reel location
            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(R.drawable.location), contentDescription = null, modifier = Modifier.size(30.dp))
                Text("512", modifier = Modifier, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(10.dp))

            Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(R.drawable.share), contentDescription = null, modifier = Modifier.size(30.dp))
                Text("512", modifier = Modifier, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }

        }
    }
}