package com.example.finalapp.screens._1home.publicEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.model.CommentData
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants
import kotlinx.coroutines.delay


@Composable
fun EventComments(commentList:List<CommentData>) {
    val listState = rememberLazyListState()

    // Auto-scroll slowly in a loop
    LaunchedEffect(Unit) {
        while (true) {
            // Scroll by a small offset every few milliseconds
            listState.scrollBy(1f) // Try changing this to adjust speed
            delay(30L)             // Lower = faster scroll
        }
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(1f)
        .background(Color(0xFFFFFDFE))
        .padding(start = 8.dp, end = 8.dp))
    {
        Column() {
            Text(text = "Event Comments", fontFamily = Constants.DONGLE_NORMAL, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = floatingActionBtnColor)
            LazyColumn(state = listState,modifier = Modifier.fillMaxSize()) {
                items(commentList) {
                    Card(modifier = Modifier.padding(bottom = 3.dp)
                        .fillMaxWidth()
                        .height(30.dp), backgroundColor = Color.LightGray, shape = RoundedCornerShape(2.dp), elevation = 20.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.userName,
                                fontFamily = Constants.FONT_MEDIUM,
                                fontSize = 12.sp,
                                color= Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = it.commentText,
                                fontFamily = Constants.FONT_MEDIUM,
                                fontSize = 12.sp,
                                color= Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

    }
}
