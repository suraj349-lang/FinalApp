package com.example.finalapp.testingp


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.ui.theme.vectorScreenIcons
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.viewmodels.EventsViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun PublicLivePost(
    modifier: Modifier = Modifier,
    videos: List<String>,
    initialPage: Int? = 0,

    ) {
    val pagerState = rememberPagerState(initialPage = initialPage ?: 0, pageCount = { videos.size })
    val fling = PagerDefaults.flingBehavior(
        state = pagerState, lowVelocityAnimationSpec = tween(
            easing = LinearEasing, durationMillis = 300
        )
    )
    var index by remember {
        mutableStateOf(0)
    }
    var image by remember {
        mutableStateOf(imageUrls.get(5))
    }
    var height by remember {
        mutableStateOf(false)
    }
    VerticalPager(
        pageSize = PageSize.Fill,
        state = pagerState,
        flingBehavior = fling,
        beyondBoundsPageCount = 3,//todo set to 1 to save memory
        modifier = modifier
    ) {
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PublicActiveEvent(index:Int,height:Boolean,imageUrls: List<String>) {
    var height by remember {
        mutableStateOf(height)
    }
    var image by remember {
        mutableStateOf(imageUrls.get(0))
    }
    var index by remember {
        mutableStateOf(index)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp)
        .border(width = 1.dp, color = Color.LightGray)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .border(width = 1.dp, color = Color.LightGray)
            ) {
                GlideImage(
                    model = image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp)
                        .align(Alignment.TopStart),
                    backgroundColor = Color.Transparent, elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ActiveButton()
                        ReactionBar()
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 8.dp)
                        .wrapContentSize()
                        .align(Alignment.BottomEnd),
                    backgroundColor = Color(0xFF110107).copy(alpha = 0.2f),
                    shape = CircleShape, elevation = 0.dp
                ) {
                    ShareIcon()
                }
                Card(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(50.dp)
                        .fillMaxWidth(0.9f)
                        .align(Alignment.BottomStart),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {
                    UserData()

                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.18f)
                    // .background(brush = Brush.linearGradient(colors = listOf(Color(0xFF4E086D), Color(0xFF9B0842))))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF600885),
                                Color(0xFF97063F)
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = "FARMER'S Protest",
                            fontFamily = FontFamily(Font(R.font.dongle_bold)),
                            color = Color.White,
                            fontSize = 30.sp
                        )
                    }
                    Image(painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            Color.White
                        ),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { height = !height })

                    Button(
                        onClick = { image= imageUrls.get(++index)},
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text(
                            text = "Next",
                            fontFamily = FontFamily(Font(R.font.dongle_bold))
                        )

                    }
                }
            }
            if(height) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        // .background(brush = Brush.linearGradient(colors = listOf(Color(0xFF4E086D), Color(0xFF9B0842))))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF600885),
                                    Color(0xFF97063F)
                                )
                            )
                        )
                ) {
                    Column(modifier= Modifier
                        .padding(3.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())) {
                        Text(
                            text = "The 2020–2021 Indian farmers' protest was a significant movement against three agricultural laws enacted in September 2020." +
                                    " Farmers feared these laws would dismantle price supports for key crops, jeopardize their livelihoods, and facilitate a corporate " +
                                    "takeover of India's agrarian economy. The protests involved mass mobilizations, including a massive march to Delhi in November 2020, " +
                                    "where farmers set up a makeshift village" +
                                    " at the Singhu border after being barred from entering the capital." +
                                    " The sustained protests led to the repeal of the laws in November 2021. \n",
                            fontFamily = FontFamily(Font(R.font.dongle_bold)),
                            color = Color.White,
                            lineHeight=12.sp,
                            softWrap = true,
                            fontSize = 18.sp
                        )
                    }}
            }
            Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.23f)
                    .background(brush = Brush.linearGradient(colors = listOf(Color(0xFFE7A603),Color(0xFFF5EDF0)))))
            {
                Row(
                    modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(modifier = Modifier.size(40.dp), shape = CircleShape) {
                            Image(
                                painter = painterResource(id = R.drawable.add),
                                contentDescription = " ", modifier = Modifier.padding(1.dp)
                            )
                        }
                        Text(text = "Add Event", fontFamily = DONGLE_BOLD)
                    }
                }
            }
           Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.52f)
            ) {
                UserReactions(imageUrls) { image = it }
            }

        }
    }

}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserReactions(imageUrls:List<String>,onImageClicked:(String)->Unit) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 60.dp),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(imageUrls) {imageUrl->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clickable { onImageClicked(imageUrl) }
                        .fillMaxSize(), alpha = 0.8f
                )
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.BottomCenter),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Text(
                            text = "Suraj: ",
                            fontFamily = FontFamily(Font(R.font.dongle_regular)),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "8 hrs.",
                            fontFamily = FontFamily(Font(R.font.dongle_bold)),
                            fontSize = 10.sp,
                            color = Color(0xFF2B043C)
                        )
                    }
                }
            }
        }
    }
}
