package com.spint.app.screens._1home._1FlashPosts.presentation.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.spint.app.R
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.screens._1home.commonUI.sharePingDeepLink
import com.spint.app.ui.imagePrefix
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.getFormattedTimeAndFlag
import kotlinx.coroutines.delay


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FlashPostWithImageScreen2(item:FlashPostResponse, onFlashPostClicked:()->Unit, onUserProfileClicked:()->Unit,onPingOfFlashPostClicked:(String, String)->Unit, onCommentButtonClicked: () -> Unit) {
    val context= LocalContext.current
    var showJoinComment  by remember { mutableStateOf(false) }
    var joinPingText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(4.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Black
                    )
                )
            )
            .fillMaxWidth()
    ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
                Box(modifier = Modifier
                    .clip(shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .wrapContentSize()
                    .wrapContentHeight()
                    .background(Color.Black)){
                    Row(modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(start = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier
                            .clickable { onUserProfileClicked() }
                            .size(40.dp)
                            .clip(CircleShape)
                            .shadow(
                                elevation = 20.dp,
                                spotColor = Color.White,
                                ambientColor = Color.White
                            )) {
                            AsyncImage(model = if(item.user?.profileImage.isNullOrEmpty()) R.drawable.profile_colored else imagePrefix+item.user.profileImage, contentDescription ="", modifier = Modifier
                                .fillMaxSize(), contentScale = ContentScale.Crop , filterQuality = FilterQuality.High)
                        }

                        Column(modifier = Modifier.wrapContentSize()) {
                            Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                item.user?.name?.let { Text(text = it  , modifier=Modifier.clickable{onUserProfileClicked()},fontWeight = FontWeight.SemiBold, fontFamily = Constants.USER_NAME_FONT,fontSize = 16.sp, color = Color.LightGray, lineHeight = 12.sp) }
                                Text(text = "  :: " +item.category +" /public" , fontWeight = FontWeight.SemiBold, fontFamily = Constants.FONT_LIGHT,fontSize = 16.sp, color = Color(
                                    0xFF689F38
                                ).copy(alpha = 0.9f), lineHeight = 12.sp)
                            }
                            Row(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth(0.8f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = "0.4 km.",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 9.sp,
                                    fontFamily = Constants.FONT_LIGHT,
                                    color = Color.White.copy(alpha = 0.9f),
                                    lineHeight = 1.sp,
                                    modifier = Modifier.padding( vertical = 1.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Indiranagar",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 9.sp,
                                    fontFamily = Constants.FONT_LIGHT,
                                    color = Color.White.copy(alpha = 0.9f),
                                    lineHeight = 1.sp,
                                    modifier = Modifier.padding( vertical = 1.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }


                    }

                    var showMenu by remember {
                        mutableStateOf(false)
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                            .wrapContentWidth()
                            .wrapContentHeight()
                    ) {

                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .clickable {
                                    showMenu = true
                                },

                            verticalAlignment = Alignment.CenterVertically,

                            horizontalArrangement =
                                Arrangement.spacedBy(30.dp)
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.menu),

                                contentDescription = "",

                                modifier = Modifier
                                    .rotate(90f)
                                    .size(15.dp),

                                colorFilter =
                                    ColorFilter.tint(Color.White)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(Color.DarkGray)
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "Report", color = Color.White) },
                                onClick = { showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Hide", color = Color.White) },
                                onClick = { showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Share", color = Color.White) },
                                onClick = { showMenu = false }
                            )
                        }
                    }

                }
                Box(modifier = Modifier
                    .clickable { onFlashPostClicked() }
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black, Color.Black
                            )
                        )
                    )
                    .fillMaxWidth()
                    .wrapContentHeight()
                    ){
                    AsyncImage(
                        model = imagePrefix + item.image,
                        contentDescription = "Ping Image",
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .height(400.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        contentScale = ContentScale.Fit
                    )

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment=Alignment.Bottom,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        // .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(color = Color.Transparent)
                ) {

                    ViewRoundUI(item.viewsCount)
                    CommentRoundUI(item.commentsCount,onCommentButtonClicked)
                    CountdownTimer(item.expirationTime)
                    AddPingOnFlashPost(item.peopleJoined,item.pingCount,{ showJoinComment=!showJoinComment})
                    ShareRoundUI(item.totalShared){
                        val deeplink="http://${Constants.APP_NAME}.com/ping/${item._id}"
                        sharePingDeepLink(context,deeplink)
                    }


                }
                if(showJoinComment) {
                    Box(modifier=Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth(0.8f)) {
                        OutlinedTextField(
                            value =joinPingText,
                            onValueChange = {joinPingText=it},
                            textStyle = TextStyle(fontFamily = Constants.FONT_MEDIUM, lineHeight = 12.sp, fontSize = 8.sp, color = Color.Black),
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .zIndex(2f)
                                .fillMaxWidth()
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White
                            ),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(R.drawable.chat_new),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .clickable {
                                            onPingOfFlashPostClicked(
                                                item._id,
                                                joinPingText
                                            ); showJoinComment = false
                                        }
                                        .size(20.dp),
                                    colorFilter = ColorFilter.tint(Color.Black)
                                )
                            }
                        )
                    }
                }
                Box(modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(shape = RoundedCornerShape(6.dp))
                ){
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(4.dp)) {
                        // annotated string with title and username
                        Text(
                            text = buildAnnotatedString {

                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                ) {
                                    append("${item.user?.userName} ")
                                }
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFFECE6E1).copy(alpha = 1f)
                                    )
                                ) {
                                    append(": ")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFFECE6E1).copy(alpha = 1f)
                                    )
                                ) {
                                    append(item.title.toString().capitalize())
                                }
                            },

                            fontFamily = Constants.FONT_LIGHT,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .padding(start = 0.dp)
                        )
                        if(item.description?.isNotEmpty() ==true) {
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = item.description.capitalize(),
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.sp,
                                fontFamily = Constants.FONT_LIGHT,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FlashPostWithImageScreen(item:FlashPostResponse, onFlashPostClicked:()->Unit, onUserProfileClicked:()->Unit,onPingOfFlashPostClicked:(String, String)->Unit, onCommentButtonClicked: () -> Unit) {
    val context=LocalContext.current
    Box(
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color.Black)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {

                    Box(modifier = Modifier
                        .clickable { onFlashPostClicked() }
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black, Color.Black
                                )
                            )
                        )
                        .fillMaxWidth()
                        .wrapContentHeight()
                    ){
                        AsyncImage(
                            model = imagePrefix + item.image,
                            contentDescription = "FlashPost Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            contentScale = ContentScale.Crop
                        )

                    }

            }
            Box(modifier = Modifier
                .padding(top = 6.dp)
                .clip(shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .wrapContentSize()
                .wrapContentHeight()
                .background(Color.Black)){
                Row(modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .clickable { onUserProfileClicked() }
                            .size(40.dp)
                            .clip(CircleShape)
                            .shadow(
                                elevation = 20.dp,
                                spotColor = Color.White,
                                ambientColor = Color.White
                            )) {
                        AsyncImage(
                            model = if (item.user?.profileImage.isNullOrEmpty()) R.drawable.profile_colored else imagePrefix + item.user.profileImage,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            filterQuality = FilterQuality.High
                        )
                    }

                    Column(modifier = Modifier.wrapContentSize()) {
                        Row(
                            modifier = Modifier.wrapContentSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            item.user?.name?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.clickable { onUserProfileClicked() },
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = Constants.USER_NAME_FONT,
                                    fontSize = 16.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    lineHeight = 12.sp
                                )
                            }
                            Box (modifier= Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF050B13))
                                .border(
                                    width = 0.5.dp,
                                    color = Color(0xFF050B13),
                                    shape = RoundedCornerShape(50)
                                )
                            ) {
                                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "real id",
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = Constants.FONT_LIGHT,
                                        fontSize = 11.sp,
                                        color = Color(0xFF0A81F3),
                                        lineHeight = 12.sp
                                    )
                                }

                            }
                            Box (modifier= Modifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(50))
                                .background(Color(0xFF210D0D))
                                .border(
                                    width = 0.5.dp,
                                    color = Color(0xFF210D0D),
                                    shape = RoundedCornerShape(50)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = item.category.lowercase(),
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = Constants.FONT_LIGHT,
                                        fontSize = 11.sp,
                                        color = Color(0xFFEC7F7F),
                                        lineHeight = 12.sp
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "0.4 km.",
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                fontFamily = Constants.FONT_LIGHT,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 1.sp,
                                modifier = Modifier.padding( vertical = 1.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = item.location,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                fontFamily = Constants.FONT_LIGHT,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 1.sp,
                                modifier = Modifier.padding( vertical = 1.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(shape = RoundedCornerShape(6.dp))
            ) {
                Text(
                    text = item.title?.capitalize() ?: "",
                    fontFamily = Constants.FONT_LIGHT,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color=Color.White,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(start = 0.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp)
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {
                Image(
                    painter = painterResource(R.drawable.clock_outlined),
                    contentDescription = "",
                    modifier=Modifier
                        .size(14.dp),
                    colorFilter = ColorFilter.tint(Color.LightGray)
                )
                Text(
                    text = "Expires in",
                    modifier = Modifier,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Constants.FONT_LIGHT,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    lineHeight = 12.sp
                )
                Text(
                    text = "5h 12m",
                    modifier = Modifier,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Constants.FONT_LIGHT,
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 12.sp
                )
                ExpiryTimerLine(67,100)

            }

            BottomUserActionsUI(
                onRespondClicked = {},
                onCommentButtonClicked = {},
                onShareClicked = {
                    val deeplink = "http://${Constants.APP_NAME}.com/flashPost/${item._id}"
                    sharePingDeepLink(context, deeplink)
                }
            )
        }
    }
}

@Composable
fun ExpiryTimerLine(
    remainingSeconds: Int,
    totalSeconds: Int
) {
    val progress = remainingSeconds.toFloat() / totalSeconds.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(Color(0xFF8D0707))
        )
    }
}

@Composable
fun BottomUserActionsUI(onRespondClicked:()-> Unit,onCommentButtonClicked: () -> Unit,onShareClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment=Alignment.Bottom,
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent)
    ) {
        Row(modifier=Modifier.fillMaxWidth(0.8f),horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier.clickable{onRespondClicked()}
                    .wrapContentSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF033805))
//                    .border(
//                        width = 0.5.dp,
//                        color = Color.DarkGray,
//                        shape = RoundedCornerShape(6.dp)
//                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.send_24),
                        contentDescription = "",
                        modifier = Modifier
                            .rotate((-35f))
                            .size(16.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF0A9F10))
                    )
                    Text(
                        text = "Respond",
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 16.sp,
                        color = Color(0xFF0A9F10),
                        lineHeight = 12.sp
                    )
                }
            }
            Box(
                modifier = Modifier.clickable{onCommentButtonClicked()}
                    .wrapContentSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Transparent)
                    .border(
                        width = 0.5.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(6.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.comment_outlined),
                        contentDescription = "",
                        modifier = Modifier
                            .rotate((-45f))
                            .size(18.dp),
                        colorFilter = ColorFilter.tint(Color.LightGray)
                    )
                    Text(
                        text = "5",
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Constants.FONT_LIGHT,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 12.sp
                    )
                }
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Transparent)
                    .border(
                        width = 0.5.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(6.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.clickable{onShareClicked()}.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.share),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 3.dp)
                            .size(18.dp),
                        colorFilter = ColorFilter.tint(Color.LightGray)
                    )

                }
            }
        }
        Row(modifier=Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.view),
                contentDescription = "",
                modifier=Modifier
                    .size(14.dp),
                colorFilter = ColorFilter.tint(Color.LightGray)
            )
            Text(
                text = "5",
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                fontWeight = FontWeight.Normal,
                fontFamily = Constants.FONT_LIGHT,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 12.sp
            )
        }
    }
}

@Composable
fun CountdownTimer(expirationIso: String) {
    Log.i("item", "CountdownTimer:$expirationIso ")
    var timeLeft by remember { mutableStateOf("") }
    var isLessThanHour by remember { mutableStateOf(false) }
    var isExpired by remember { mutableStateOf(false) }

    LaunchedEffect(expirationIso) {
        while (!isExpired) {
            val (formatted, lessThanHour,expired) = getFormattedTimeAndFlag(expirationIso)
            timeLeft = formatted
            isLessThanHour = lessThanHour
            isExpired=expired
            delay(1000)
        }
    }

     RoundUI(time = timeLeft, isLessThanHour = isLessThanHour,isExpired)
}


@Composable
fun RoundUI(time: String, isLessThanHour: Boolean,isExpired: Boolean) {
    val containerColor = when {
        isExpired -> Color.Black.copy(alpha = 0.5f)
        isLessThanHour -> Color(0xFF940707)
        else -> Color(0xFF214601)///0xFF545707 0xFF888D0B 0xFF689F38
    }

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(40.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(!isExpired) {
                Text(
                    text = "expecting response till:",
                    fontWeight = FontWeight.Bold,
                    fontFamily = Constants.ROBOTO_CONDENSED,
                    fontSize = 8.sp,
                    lineHeight = 12.sp,
                    maxLines = 1, color = Color.White
                )
                Text(
                    text = time,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 13.sp,
                    lineHeight = 12.sp,
                    maxLines = 2, color = Color.White
                )
            }else {
                Text(
                    text = "post expired",
                    fontWeight = FontWeight.Bold,
                    fontFamily = Constants.ROBOTO_CONDENSED,
                    fontSize = 8.sp,
                    lineHeight = 12.sp,
                    maxLines = 1, color = Color.White
                )
                Text(
                    text = "Ping user",
                    fontWeight = FontWeight.Bold,
                    fontFamily = Constants.FONT_MEDIUM,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    maxLines = 2, color = Color.White
                )
            }
        }
    }
}


@Composable
fun ShareRoundUI(shareCount:Int,onShareClicked:()->Unit) {
    Card(
        onClick = {onShareClicked()},
        modifier=Modifier.size(40.dp),
        shape = CircleShape, colors = CardDefaults.cardColors(containerColor =  Constants.HOME_TOP_BAR_COLOR)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.share_event), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
                Color.White.copy(alpha = 0.954f)) )
            if(shareCount !=0) Text("2.3k", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)
        }}

}

@Composable
fun AddPingOnFlashPost(joinedCount: Int,pingCount:Int,onClick:()-> Unit={}) {
    Box(modifier= Modifier
        .height(40.dp)
        .width(80.dp)) {
        Box(modifier=Modifier
            .zIndex(2f)
            .size(14.dp)
            .clip(shape = CircleShape)
            .background(color = Color.Red)
            .align(Alignment.TopEnd)){
            Text(pingCount.toString(), fontSize = 10.sp, modifier = Modifier
                .padding(bottom = 2.dp)
                .fillMaxSize(), color = Color.White, textAlign = TextAlign.Center)
        }
    Box (
        modifier= Modifier
            .height(40.dp)
            .width(80.dp)
            .clickable { onClick() }
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = Constants.HOME_TOP_BAR_COLOR)
    ) {

        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.join_blue), contentDescription ="", modifier = Modifier.size(28.dp) )
            Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("JOIN", color = Color.White, fontFamily = Constants.FONT_LIGHT, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                if(joinedCount !=0)Text("122k", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)
            }

        }}}


}
@Composable
fun CommentRoundUI(commentCount: Int,onCommentButtonClicked:()-> Unit={}) {//50,30 earlier

    Card(
        onClick = {onCommentButtonClicked()},
        modifier=Modifier.size(40.dp),
        shape = CircleShape, colors = CardDefaults.cardColors(containerColor =  Constants.HOME_TOP_BAR_COLOR)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.comment), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
            Color.White.copy(alpha = 0.954f)) )
       if(commentCount !=0) Text("56k", color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)}

    }

}
@Composable
fun ViewRoundUI(viewCount: Int) {
    Card(
        onClick = {},
        modifier=Modifier.size(40.dp),
        shape = CircleShape, colors = CardDefaults.cardColors(containerColor = Constants.HOME_TOP_BAR_COLOR)
    ) {
//        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//
//            //  Text("Chat", color = Color.White, fontFamily = Constants.FONT_MEDIUM)
//        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(id = R.drawable.view), contentDescription ="", modifier = Modifier.size(20.dp), colorFilter = ColorFilter.tint(
                Color.White.copy(alpha = 0.954f)) )
          Text(viewCount.toString(), color = Color.White, fontFamily = Constants.FONT_EXTRA_LIGHT, fontSize = 9.sp)
    }
        }
}