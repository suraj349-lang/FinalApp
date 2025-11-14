package com.spint.app.screens._6chat


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.spint.app.utils.constants.Constants
import com.spint.app.utils.convertToIST
import com.spint.app.R


//
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun MessageItemUI(
//    msg: String,
//    sent:Int,
//    received: Boolean,
//    timestamp: String?,
//    isSentByLoggedInUser: Boolean
//) {
//    val backgroundColor = if (isSentByLoggedInUser) Color(0xFF797676) else Color(0xFFCF5630)
//    val textColor = Color.White
//    val alignment = if (isSentByLoggedInUser) Arrangement.End else Arrangement.Start
//    var time=""
//    if(timestamp !=null) time= convertToIST(timestamp)
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp, vertical = 4.dp),
//        horizontalArrangement = alignment
//    ) {
//        Column() {
//        Row(
//            modifier = Modifier
//                .clip(BubbleShape(isSentByUser = isSentByLoggedInUser))
//                .background(backgroundColor)
//                .padding(horizontal = 12.dp, vertical = 8.dp)
//                .widthIn(max = 280.dp)
//                .animateContentSize(
//                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
//                ),
//            verticalAlignment = Alignment.Bottom
//        ) {
//            Text(
//                buildAnnotatedString {
//                    append(msg)
//                    append("   ") // small gap
//                    withStyle(
//                        style = SpanStyle(
//                            fontSize = 14.sp,
//                            color = Color.LightGray
//                        )
//                    ) {
//                        append(time)
//                    }
//                },
//                fontSize = 22.sp,
//                color = textColor,
//                softWrap = true,
//                fontFamily=DONGLE_LIGHT,
//                lineHeight=16.sp,
//                maxLines = Int.MAX_VALUE,
//                modifier = Modifier.widthIn(max = 280.dp)
//            )
//            if (received && isSentByLoggedInUser) {
//                Icon(
//                    painter = painterResource(id = R.drawable.check_double),
//                    contentDescription = "",
//                    modifier = Modifier.size(10.dp)
//                )
//            } else if (isSentByLoggedInUser && sent == 1) {
//                Icon(
//                    painter = painterResource(id = R.drawable.check),
//                    contentDescription = "",
//                    modifier = Modifier.size(10.dp)
//                )
//
//            }
//        }
//
//        }
//
//    }
//}


@Composable
fun MessageItemUI(
    msg: String,
    sent: Int,
    received: Boolean,
    timestamp: String?,
    isSentByLoggedInUser: Boolean
) {
    val backgroundColor = if (isSentByLoggedInUser){
        Color(0xFF797676)
    } else{ Color(0xFFCF5630)
       }     ///    0xFF797676            0xFFCF5630
    val textColor = Color.White
    var time = ""
    //if (timestamp != null) time = convertToIST(timestamp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = if (isSentByLoggedInUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .clip(BubbleShape(isSentByUser = isSentByLoggedInUser))
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .widthIn(max = 280.dp) // limit width for long texts
        ) {
            Text(
                text = msg,
                fontSize = 16.sp,
                color = textColor,
                lineHeight = 16.sp,
                fontFamily = Constants.FONT_LIGHT
            )

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = time,
                    fontSize = 9.sp,
                    color = Color.LightGray, lineHeight = 2.sp, fontFamily = Constants.FONT_LIGHT
                )
                if (isSentByLoggedInUser) {
                    Spacer(modifier = Modifier.width(4.dp))
                    if (received) {
                        Icon(
                            painter = painterResource(id = R.drawable.check_double),
                            contentDescription = "Delivered",
                            tint = Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                    } else if (sent == 1) {
                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "Sent",
                            tint = Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}


class BubbleShape(private val isSentByUser: Boolean) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return with(density) {
            val cornerRadius = 16.dp.toPx()
            val tailWidth = 8.dp.toPx()
            val tailHeight = 12.dp.toPx()
            val tailYOffset = 10.dp.toPx()

            val path = Path()

            if (isSentByUser) {
                // Sent Message (tail on top-end, rotated)
                path.moveTo(0f + cornerRadius, 0f)

                path.lineTo(size.width - cornerRadius - tailWidth, 0f)
                path.quadraticBezierTo(
                    size.width - tailWidth, 0f,
                    size.width - tailWidth, cornerRadius
                )

//                // Tail
//                path.lineTo(size.width - tailWidth, tailYOffset)
//                path.lineTo(size.width, tailYOffset - tailHeight / 2)
//                path.lineTo(size.width - tailWidth, tailYOffset + tailHeight)

                // Right side
                path.lineTo(size.width - tailWidth, size.height - cornerRadius)
                path.quadraticBezierTo(
                    size.width - tailWidth, size.height,
                    size.width - tailWidth - cornerRadius, size.height
                )

                // Bottom side
                path.lineTo(cornerRadius, size.height)
                path.quadraticBezierTo(0f, size.height, 0f, size.height - cornerRadius)

                // Left side
                path.lineTo(0f, cornerRadius)
                path.quadraticBezierTo(0f, 0f, cornerRadius, 0f)
            } else {
                // Received Message (tail on top-start, rotated)
                path.moveTo(tailWidth + cornerRadius, 0f)

                path.lineTo(size.width - cornerRadius, 0f)
                path.quadraticBezierTo(size.width, 0f, size.width, cornerRadius)

                path.lineTo(size.width, size.height - cornerRadius)
                path.quadraticBezierTo(size.width, size.height, size.width - cornerRadius, size.height)

                path.lineTo(tailWidth + cornerRadius, size.height)
                path.quadraticBezierTo(tailWidth, size.height, tailWidth, size.height - cornerRadius)

//                // Tail
//                path.lineTo(tailWidth, tailYOffset + tailHeight)
//                path.lineTo(0f, tailYOffset - tailHeight / 2)
//                path.lineTo(tailWidth, tailYOffset)

                // Left side
                path.lineTo(tailWidth, cornerRadius)
                path.quadraticBezierTo(tailWidth, 0f, tailWidth + cornerRadius, 0f)
            }

            path.close()
            Outline.Generic(path)
        }
    }
}


/*


        Row(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (isSentByLoggedInUser) 16.dp else 0.dp,
                        topEnd = if (isSentByLoggedInUser) 0.dp else 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp
                    )
                )
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(max = 280.dp) // Limit width to ~80% of screen
                .animateContentSize(
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                ),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = msg,
                fontSize = 15.sp,
                color = textColor,
                softWrap = true,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = timestamp,
                fontSize = 10.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .offset(y = 4.dp)
            )
        }
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MessageItemUI(
    msg: String,
    profileImage:String,
    isSentByLoggedInUser: Boolean,
    timestamp: String = "08:38"
) {
    val backgroundColor = if (isSentByLoggedInUser) Color(0xFFF7F2F2) else Color(0xFFD58FE7)
    //val textColor = if (isSentByLoggedInUser) Color.White else Color.Black
    val textColor=Color.Black
    val alignment = if (isSentByLoggedInUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = alignment
    ) {
//        if(!isSentByLoggedInUser){
//            GlideImage(model = if(profileImage.isNotEmpty()) imagePrefix+profileImage else R.drawable.profile_new, contentDescription = "", modifier = Modifier.size(30.dp).clip(shape = CircleShape), contentScale = ContentScale.Crop)
//        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .wrapContentWidth()
                .wrapContentHeight()
                .animateContentSize(
                    animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                ),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = msg,
                fontSize = 15.sp,
                color = textColor,
                softWrap = true
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = timestamp,
                fontSize = 10.sp,
                color = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(top = 2.dp)
                    .offset(y = 4.dp)
            )
        }
    }
}
*/
@Composable
fun MessageItem(
    msg: String,
    isSentByLoggedInUser: Boolean,
    timestamp: String = "08:38",
    senderName: String = if (isSentByLoggedInUser) "Me" else "User"
) {
    val bubbleColor = if (isSentByLoggedInUser) Color(0xFFEF6C00) else Color(0xFFEEEEEE)
    val textColor = if (isSentByLoggedInUser) Color.White else Color.Black
    val contentAlignment = if (isSentByLoggedInUser) Alignment.CenterEnd else Alignment.CenterStart
    val horizontalAlignment = if (isSentByLoggedInUser) Alignment.End else Alignment.Start

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = contentAlignment // ✅ fixed: full Alignment not just horizontal
    ) {
        Column(horizontalAlignment = horizontalAlignment) {
            Text(
                text = senderName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Surface(
                color = bubbleColor,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                modifier = Modifier.widthIn(min = 60.dp, max = 280.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = msg,
                        fontSize = 15.sp,
                        color = textColor,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = timestamp,
                        fontSize = 10.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun MessageItem2(
    msg: String,
    isSentByLoggedInUser: Boolean,
    timestamp: String = "08:38"
) {
    val messageColor = if (isSentByLoggedInUser) Color(0xFF8E24AA) else Color(0xFF2E2E2E)
    val textColor = if (isSentByLoggedInUser) Color.White else Color.White.copy(alpha = 0.85f)
    val alignment = if (isSentByLoggedInUser) Alignment.CenterEnd else Alignment.CenterStart
    val textAlign = if (isSentByLoggedInUser) TextAlign.End else TextAlign.Start

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (isSentByLoggedInUser) Alignment.End else Alignment.Start,
            modifier = Modifier
                .background(
                    color = messageColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .widthIn(min = 60.dp, max = 260.dp)
        ) {
            Text(
                text = msg,
                fontSize = 14.sp,
                color = textColor,
                textAlign = textAlign
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timestamp,
                fontSize = 10.sp,
                color = Color.LightGray,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Preview
@Composable
fun PreviewSnapchatMessageItem() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        MessageItem2("Hey, did you check my snap?", isSentByLoggedInUser = false)
        MessageItem2("Yeah haha, that was wild 😄", isSentByLoggedInUser = true)
    }
}
