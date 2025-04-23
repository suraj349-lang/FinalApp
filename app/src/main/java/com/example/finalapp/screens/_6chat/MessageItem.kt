package com.example.finalapp.screens._6chat

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageItemUI(
    msg: String,
    isSentByLoggedInUser: Boolean,
    timestamp: String = "08:38"
) {
    val backgroundColor = if (isSentByLoggedInUser) Color(0xFF8E24AA) else Color(0xFF2E2E2E)
    val textColor = Color.White
    val alignment = if (isSentByLoggedInUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = alignment
    ) {
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
                modifier = Modifier.align(Alignment.Bottom).padding(top = 2.dp)
                    .offset(y = 4.dp)
            )
        }
    }
}

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
