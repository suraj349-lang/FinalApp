package com.spint.app.screens._1home._1FlashPosts.presentation.util


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.spint.app.R
import com.spint.app.model.flashPost.CommentRequest
import com.spint.app.model.flashPost.CommentResponse
import com.spint.app.screens._4profile.privateUsername.dynamicText
import com.spint.app.ui.imagePrefix
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.RequestState
import com.spint.app.utils.UserObject
import com.spint.app.utils.constants.Constants
import com.spint.app.viewmodels.HomeViewModel
import kotlin.collections.List

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FlashPostCommentScreen(
    postId: String,
    viewModel: HomeViewModel
) {
    val comments by viewModel.flashPostComments.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getFlashPostComments(postId)
    }

    val user by UserObject.user.collectAsState()
    var comment by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            when (val state = comments) {
                is RequestState.Success -> {
                    CommentScreenUI(state.data,viewModel,postId,user.user)
                }

                is RequestState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black)
                    ) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text("Error getting comments")
                            Button(
                                onClick = { viewModel.getFlashPostComments(postId) }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                is RequestState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                else -> {}
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .padding(10.dp)
                    .fillMaxWidth(),
                placeholder = { dynamicText("Add comment", fontFamily = Constants.FONT_LIGHT, fontSize = 14, color =Color.LightGray ) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.Black),
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.send_24),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .rotate(-35f)
                            .clickable {
                                viewModel.addFlashPostComments(
                                    CommentRequest(
                                        userId = user.user,
                                        comment = comment,
                                        flashPostId = postId
                                    )
                                )
                                comment = ""
                            })
                }
            )
        }

    }
}

//@Composable
//fun CommentScreenUI(comments: List<CommentResponse>) {
////    // Use mutable state list for top-level comments
////    val commentsState =
////        remember { mutableStateListOf<CommentResponse>().apply { addAll(comments) } }
////
////    // Function to toggle expand/collapse for any comment (recursive update)
////    fun toggleExpand(target: CommentResponse) {
////        val updated = updateCommentExpandState(commentsState, target)
////        if (!updated) println("Comment not found")
////    }
//
//    LazyColumn(modifier = Modifier.padding(top=16.dp, start = 8.dp)
//        .fillMaxSize()) {
//        items(comments) { comment ->
//            CommentItem(
//                comment = comment,
//                indentLevel = 0,
//                onToggleExpand = {  },
//                onToggleReplyBox = {},
//                onReplyTextChange = { _, _ -> },
//                onSendReply = {}
//            )
//        }
//    }
//}
@Composable
fun CommentScreenUI(
    comments: List<CommentResponse>,
    viewModel: HomeViewModel,
    postId: String,
    userId: String
) {

    val listState = rememberLazyListState()

    // PAGINATION TRIGGER
    LaunchedEffect(listState) {

        snapshotFlow {

            listState.layoutInfo
                .visibleItemsInfo
                .lastOrNull()
                ?.index

        }.collect { lastVisibleIndex ->

            val totalItems = listState.layoutInfo.totalItemsCount

            // Near bottom
            if (lastVisibleIndex != null && lastVisibleIndex >= totalItems - 2 && viewModel.hasMore && !viewModel.isLoading) {
                viewModel.getFlashPostComments(postId)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(top = 16.dp, start = 8.dp)
            .fillMaxSize()
    ) {

        items(
            items = comments,
            key = { it.id }
        ) { comment ->

            CommentItem(
                comment = comment,
                indentLevel = 0,

                onToggleExpand = {
                    viewModel.toggleCommentExpand(it)
                },

                onToggleReplyBox = {
                    viewModel.toggleReplyBox(it)
                },

                onReplyTextChange = { c, text ->
                    viewModel.updateReplyText(c, text)
                },

                onSendReply = { parent ->

                    viewModel.addFlashPostComments(

                        CommentRequest(
                            userId = userId,
                            comment = parent.replyText ?: "",
                            flashPostId = postId,
                            parentCommentId = parent.id
                        )
                    )
                }
            )
        }

        // PAGINATION LOADER
        if (viewModel.isLoading) {

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }
        }
    }
}


// Recursive function to update expansion state
fun updateCommentExpandState(
    comments: MutableList<CommentResponse>,
    target: CommentResponse
): Boolean {
    for (i in comments.indices) {
        val current = comments[i]
        if (current.id == target.id) {
            comments[i] = current.copy(isExpanded = !current.isExpanded)
            return true
        } else {
            val childReplies = (current.replies ?: emptyList()).toMutableList()
            if (updateCommentExpandState(childReplies, target)) {
                comments[i] = current.copy(replies = childReplies)
                return true
            }
        }
    }
    return false
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CommentItem(
    comment: CommentResponse,
    indentLevel: Int = 0,
    onToggleExpand: (CommentResponse) -> Unit,
    onToggleReplyBox: (CommentResponse) -> Unit,
    onReplyTextChange: (CommentResponse, String) -> Unit,
    onSendReply: (CommentResponse) -> Unit
) {
    var isExpanded = comment.isExpanded


    val maxChars = 135
    val isLongComment = comment.comment.length > maxChars

    val displayCommentText = when {
        isExpanded || !isLongComment -> comment.comment
        else -> comment.comment.take(maxChars)
    }

    val displayText = buildAnnotatedString {
        //username
        withStyle(
            SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFFA6C8EA)
            )
        ) {
            append("@${comment.userId.userName} ")
        }
        withStyle(SpanStyle(fontSize = 13.sp, color = Color.White)) {
            append(displayCommentText)
        }

        if (!isExpanded && isLongComment) {
            pushStringAnnotation(tag = "MORE", annotation = "more")
            withStyle(SpanStyle(color = Color.Yellow)) {
                append("... more")
            }
            pop()
        } else if (isExpanded && isLongComment) {
            pushStringAnnotation(tag = "LESS", annotation = "less")
            withStyle(SpanStyle(color = Color.Yellow)) {
                append(" ...Show less")
            }
            pop()
        }
    }

    Column(modifier = Modifier.padding(start = (indentLevel * 16).dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
//                .background(
//                    if (indentLevel == 0) Color(0xFF20202D) else Color(0xFF182533), //0xFF753CC5  if (indentLevel == 0) Color(0xFF753CC5) else Color(0xFF0970CB),
//                    shape = RoundedCornerShape(4.dp)
//                )
                .padding(4.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = imagePrefix + comment.userId.profileImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .weight(1f)
            ) {
                ClickableText(
                    text = displayText,
                    style = TextStyle(fontSize = 13.sp),
                    onClick = { offset ->
                        displayText.getStringAnnotations(tag = "MORE", start = offset, end = offset)
                            .firstOrNull()?.let {
                                isExpanded = true
                            }

                        displayText.getStringAnnotations(tag = "LESS", start = offset, end = offset)
                            .firstOrNull()?.let {
                                isExpanded = false
                            }
                    }
                )
            }

            if (!comment.replies.isNullOrEmpty()) {
                Image(
                    painter = painterResource(
                        id = if (comment.isExpanded)
                            R.drawable.baseline_expand_less_24
                        else
                            R.drawable.baseline_expand_more_24
                    ),
                    contentDescription = "Toggle replies",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { onToggleExpand(comment) }
                )
            }

        }

        // Reply Text
        Row(
            modifier = Modifier
                .padding(start = 48.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.like),
                contentDescription = "",
                modifier = Modifier.size(12.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
            if (comment.repliesCount > 0) {
                Text(
                    text = if (comment.isExpanded)
                        "Hide replies"
                    else
                        "View replies (${comment.repliesCount})",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickable {
                        onToggleExpand(comment)
                    }
                )
            }
            Text(
                text = "Reply",
                fontSize = 12.sp,
                lineHeight = 8.sp,
                modifier = Modifier.clickable {
                    onToggleReplyBox(comment)
                },
                color = Color.Gray
            )



            Text(
                text = "Report",
                fontSize = 12.sp,
                lineHeight = 8.sp,
                modifier = Modifier,
                color = Color.Gray
            )
        }


        // Reply TextField
        if (comment.showReplyBox) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp).padding(start = 32.dp)
            ) {
                OutlinedTextField(
                    value = comment.replyText ?: "",
                    onValueChange = { onReplyTextChange(comment, it) },
                    placeholder = { dynamicText("Write a reply...", fontSize = 14) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    trailingIcon = {Image(painter = painterResource(R.drawable.send_24), contentDescription = "", modifier = Modifier.size(20.dp).rotate(-35f).clickable{onSendReply(comment)})}
                )

            }
        }

        // Recursively show replies
        if (comment.isExpanded) {
            (comment.replies ?: emptyList()).forEach { reply ->
                CommentItem(
                    comment = reply,
                    indentLevel = indentLevel + 1,
                    onToggleExpand = onToggleExpand,
                    onToggleReplyBox = onToggleReplyBox,
                    onReplyTextChange = onReplyTextChange,
                    onSendReply = onSendReply
                )
            }
        }
    }
}
