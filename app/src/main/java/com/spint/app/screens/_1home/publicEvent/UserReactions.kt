package com.spint.app.screens._1home.publicEvent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.utils.constants.Constants


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserReactions(imageUrls:List<String>,onImageClicked:(String)->Unit) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 60.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(color = Color(0xFFFBC02D))) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.add), contentDescription ="", colorFilter = ColorFilter.tint(
                        Color.Black), modifier = Modifier
                        .size(30.dp) )
                    Text(text = "Contribute", fontFamily = Constants.FONT_MEDIUM, fontSize = 10.sp)
                    
                }
                

            }
        }
        items(imageUrls) {imageUrl->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clickable { onImageClicked(imageUrl) }
                        .fillMaxSize(), alpha = 0.8f
                )
//                Card(
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .align(Alignment.BottomCenter),
//                    backgroundColor = Color.Transparent,
//                    elevation = 0.dp
//                ) {
//                    Row(modifier = Modifier.padding(4.dp)) {
//                        Text(
//                            text = "Suraj: ",
//                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
//                            fontSize = 12.sp
//                        )
//                        Spacer(modifier = Modifier.width(2.dp))
//                        Text(
//                            text = "8 hrs.",
//                            fontFamily = FontFamily(Font(R.font.dongle_bold)),
//                            fontSize = 10.sp,
//                            color = Color(0xFF2B043C)
//                        )
//                    }
//                }
            }
        }
    }
}

