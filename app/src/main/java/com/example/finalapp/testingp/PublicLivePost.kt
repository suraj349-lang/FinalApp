package com.example.finalapp.testingp


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.finalapp.R
import com.example.finalapp.screens._1home.utils.shareDeepLink
import com.example.finalapp.ui.theme.PURPLE
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants.DONGLE_BOLD
import com.example.finalapp.utils.constants.Constants.DONGLE_NORMAL


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
    val context= LocalContext.current
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
            modifier = Modifier.fillMaxSize().imePadding(),
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
                    ShareIcon(){
                        shareDeepLink(context,"0808-dfbsdj-sddgfj3") // TODO postId to be mended again
                    }
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
                    .background(
                        color = Color(0xFFF5F5F5)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(0.dp), horizontalAlignment = Alignment.Start) {
                        Row(modifier = Modifier.fillMaxHeight(0.4f), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Image(painter = painterResource(id = R.drawable.location_new), contentDescription ="" )
                            Text(text = "India", fontSize = 18.sp, fontFamily = DONGLE_BOLD)
                        }
                        Text(
                            text = "FARMER'S Protest",
                            Modifier.fillMaxHeight(1f),
                            fontFamily = FontFamily(Font(R.font.dongle_bold)),
                            color =PURPLE,
                            fontSize = 30.sp
                        )
                    }
                    Image(painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(
                            //Color.White
                        Color(0xFF095985)),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { height = !height })

                    Button(
                        onClick = { image= imageUrls.get(++index)},
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        border = BorderStroke(width = 0.5.dp, color = floatingActionBtnColor.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "Next",
                            fontFamily = FontFamily(Font(R.font.dongle_bold)), fontSize = 22.sp
                        )

                    }
                }
            }
            if(height) {
               EventDescription()
            }

           Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp).background(Color.White)
            ) {
                UserReactions(imageUrls) { image = it }
            }
            AddCommentOnPost(){}
            EventComments()

        }
    }

}

@Composable
fun AddCommentOnPost(onCommentClicked: (String) -> Unit) {
    val comment by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.padding(top=8.dp)
        .fillMaxWidth()
        .height(50.dp)
        .background(
//            brush = Brush.linearGradient(
//                colors = listOf(
//                    Color(0xFFF5F3EE),
//                    Color(0xFFF5EDF0)
//                )
//            )
        Color.White
        ))
    {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
//            OutlinedTextField(
//                value = comment,
//                onValueChange ={comment=it},
//                modifier = Modifier
//                    .fillMaxWidth(0.73f),
//                placeholder = {Text("Add comment")},
//                shape = RoundedCornerShape(8.dp)
//            )
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.people), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text = "300k" , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.comment_filled), contentDescription ="", modifier = Modifier.size(30.dp), colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text = "144k" , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.war_room), contentDescription ="", modifier = Modifier.size(30.dp),colorFilter = ColorFilter.tint(Color.DarkGray) )
                Text(text = "100" , fontSize = 12.sp, fontFamily = DONGLE_NORMAL, color = Color.DarkGray)
            }
            Button(onClick = { onCommentClicked(comment) }, modifier = Modifier.height(40.dp),shape= RoundedCornerShape(8.dp),colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray)) {
                Text(text = "+JOIN", fontFamily = DONGLE_BOLD, fontSize = 20.sp,color=Color.White)
            }
        }
    }
}

@Composable
fun EventDescription() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            // .background(brush = Brush.linearGradient(colors = listOf(Color(0xFF4E086D), Color(0xFF9B0842))))
            .background(
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        Color(0xFF600885),
//                        Color(0xFF97063F)
//                    )
//                )
                color = Color.DarkGray
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
                        " at the Sindhu border after being barred from entering the capital." +
                        " The sustained protests led to the repeal of the laws in November 2021. \n",
                fontFamily = FontFamily(Font(R.font.dongle_bold)),
                color = Color.White,
                lineHeight=12.sp,
                softWrap = true,
                fontSize = 18.sp
            )
        }}
}
/*
@Composable
fun EventComments() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.LightGray,
                    Color(0xFFF5EDF0)
                )
            )
        ))
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
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
}
*/
@Composable
fun EventComments() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(1f)
        .background(Color(0xFFFFFDFE))
        .padding(start = 8.dp, end = 8.dp))
    {
            Column() {
                Text(text = "Event Comments", fontFamily = DONGLE_NORMAL, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = floatingActionBtnColor)
                LazyColumn {
                    items(commentList) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.username,
                                fontFamily = DONGLE_NORMAL,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = it.comment,
                                fontFamily = DONGLE_NORMAL,
                                fontSize = 20.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }
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
data class Comment(val username: String, val comment: String)

val commentList = listOf(
    Comment("ravi_123", "Farmers deserve fair prices!"),
    Comment("priya09", "Hope the government listens this time."),
    Comment("amit_k", "Blocking roads is not the solution."),
    Comment("sneha_x", "Support the farmers, they feed us!"),
    Comment("vikram_99", "Why is the govt ignoring their demands?"),
    Comment("meera87", "Protests should be peaceful."),
    Comment("arjun_b", "MSP should be guaranteed!"),
    Comment("neha_22", "Media needs to cover this properly."),
    Comment("raj_theking", "Repealing farm laws was just the first step."),
    Comment("karan.m", "Dialogue is the only way forward."),
    Comment("sunita_d", "Farmers' rights are human rights."),
    Comment("manoj_cool", "Blocking internet is not the solution."),
    Comment("deepa_sharma", "Hope this ends in a fair resolution."),
    Comment("ramesh_74", "Agriculture is the backbone of India."),
    Comment("tina_rose", "Farmers need security and support."),
    Comment("akash_y", "Why aren't politicians solving this?"),
    Comment("komal_s", "Government should act fast!"),
    Comment("rahul.m", "This affects the whole country."),
    Comment("nidhi_star", "We stand with our farmers!"),
    Comment("dev_x", "A strong agriculture sector means a strong India.")
)
