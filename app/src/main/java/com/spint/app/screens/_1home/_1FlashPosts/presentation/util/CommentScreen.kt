package com.spint.app.screens._1home._1FlashPosts.presentation.util


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.spint.app.viewmodels.HomeViewModel
import kotlin.collections.List

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun  FlashPostCommentScreen(postId: String,viewModel: HomeViewModel) { // com.spint.app.screens._1home._1FlashPosts.presentation.util.comments
    val comments by viewModel.flashPostComments.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getFlashPostComments(postId)
    }
    val user by UserObject.user.collectAsState()
    var comment by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        when (val data = comments) {
            is RequestState.Success -> {
                CommentScreenUI(data.data)
            }

            is RequestState.Error -> {
                Box(modifier = Modifier.fillMaxSize().background(color = Color.Black)) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text("Error getting comments")
                        Button(
                            onClick = { viewModel.getFlashPostComments(postId) },
                            colors = ButtonDefaults.buttonColors(containerColor = floatingActionBtnColor),
                            modifier=Modifier.clip(RoundedCornerShape(20.dp))
                        ) {
                            Text("Retry", modifier = Modifier.padding(horizontal = 10.dp))
                        }
                    }
                }
            }

            is RequestState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().background(color = Color.Black)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            else -> {}

        }
        Row(modifier = Modifier.fillMaxWidth().height(80.dp).align(Alignment.BottomCenter)) {
            OutlinedTextField(
                value = comment,
                onValueChange={comment=it},
                modifier=Modifier.clip(RoundedCornerShape(50)).padding(10.dp).fillMaxWidth(),
                placeholder = { dynamicText("add comment") },
                trailingIcon = {Image(painter = painterResource(R.drawable.send_24), contentDescription = "", colorFilter = ColorFilter.tint(Color.White), modifier = Modifier.rotate(-45f).clickable{viewModel.addFlashPostComments(
                    CommentRequest(
                        userId = user.user,
                        comment=comment,
                        flashPostId = postId
                    )
                )})}
            )
        }
    }

}

@Composable
fun CommentScreenUI(comments: List<CommentResponse>) {
    // Use mutable state list for top-level comments
    val commentsState = remember { mutableStateListOf<CommentResponse>().apply { addAll(comments) } }

    // Function to toggle expand/collapse for any comment (recursive update)
    fun toggleExpand(target: CommentResponse) {
        val updated = updateCommentExpandState(commentsState, target)
        if (!updated) println("Comment not found")
    }

    LazyColumn(modifier = Modifier.fillMaxWidth().height(800.dp)) {
        items(commentsState) { comment ->
            CommentItem(
                comment = comment,
                indentLevel = 0,
                onToggleExpand = ::toggleExpand,
                onToggleReplyBox = {},
                onReplyTextChange = {_,_->},
                onSendReply = {}
            )
        }
    }
}

// Recursive function to update expansion state
fun updateCommentExpandState(comments: MutableList<CommentResponse>, target: CommentResponse): Boolean {
    for (i in comments.indices) {
        val current = comments[i]
        if (current.id == target.id) {
            comments[i] = current.copy(isExpanded = !current.isExpanded)
            return true
        } else {
            val childReplies = current.replies.toMutableList()
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
    var isExpanded by remember { mutableStateOf(comment.isExpanded) }

    val maxChars = 135
    val isLongComment = comment.comment.length > maxChars

    val displayCommentText = when {
        isExpanded || !isLongComment -> comment.comment
        else -> comment.comment.take(maxChars)
    }

    val displayText = buildAnnotatedString {
        //username
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFA6C8EA))) {
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
                model = imagePrefix+comment.userId.profileImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier
                .padding(top = 4.dp)
                .weight(1f)) {
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

            if (comment.replies.isNotEmpty()) {
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
        Row(modifier = Modifier
            .padding(start = 48.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Image(painter = painterResource(R.drawable.like), contentDescription = "",modifier= Modifier.size(12.dp), colorFilter = ColorFilter.tint(Color.Gray))
            Text(
                text = "Reply",
                fontSize = 12.sp,
                lineHeight = 8.sp,
                modifier = Modifier
                    .clickable { onToggleReplyBox(comment) }
                    ,
                color = Color.Gray
            )
//            Text(
//                text = "Report",
//                fontSize = 12.sp,
//                lineHeight = 8.sp,
//                modifier = Modifier,
//                color = Color.Gray
//            )
        }


        // Reply TextField
        if (comment.showReplyBox) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = comment.replyText,
                    onValueChange = { onReplyTextChange(comment, it) },
                    placeholder = { Text("Write a reply...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onSendReply(comment) }) {
                    Text("Send")
                }
            }
        }

        // Recursively show replies
        if (comment.isExpanded) {
            comment.replies.forEach { reply ->
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



/*
val comments = listOf(
    Comment(
        id = "1",
        username = "suraj_3494",
        comment = "This app is amazing!Best app in this genre!, this si the only thing that iw ant iin life to have and this is how i amg innna",
        profileImageRes = R.drawable.profile_image_1,
        replies = listOf(
            Comment(
                id = "1-1",
                username = "sakshi7687",
                comment = "Absolutely agree!",
                profileImageRes = R.drawable.profile_image_2,
                replies = listOf(
                    Comment(
                        id = "1-1-1",
                        username = "jayesh_123",
                        comment = "Same here. UI is top-notch!",
                        profileImageRes = R.drawable.profile_image_3
                    )
                )
            ),
            Comment(
                id = "1-2",
                username = "anupam_mittal",
                comment = "10/10 design 👌",
                profileImageRes = R.drawable.profile_image_2
            )
        )
    ),
    Comment(
        id = "2",
        username = "positron_piecerer",
        comment = "Best app in this genre!, this si the only thing that iw ant iin life to have and this is how i amg innna di tiin life iiiresoisretive of what will happend in the life withme. afjhdsj asoghouasd asghosaudhg agohdguohauhguoasd gaohgua",
        profileImageRes = R.drawable.profile_image_3
    ),

    Comment(
        id = "3",
        username = "techie_rohan",
        comment = "UI feels super smooth. Loving the animations 🔥",
        profileImageRes = R.drawable.profile_image_1,
        replies = listOf(
            Comment(
                id = "3-1",
                username = "ui_queen",
                comment = "Yes! The transitions are clean.",
                profileImageRes = R.drawable.profile_image_2
            )
        )
    ),

    Comment(
        id = "4",
        username = "wanderlust_avi",
        comment = "Finally something different from regular social apps.",
        profileImageRes = R.drawable.profile_image_2
    ),

    Comment(
        id = "5",
        username = "dev_suraj",
        comment = "Bro this concept has potential to scale big time 🚀",
        profileImageRes = R.drawable.profile_image_3,
        replies = listOf(
            Comment(
                id = "5-1",
                username = "startup_girl",
                comment = "If executed properly, 100% yes.",
                profileImageRes = R.drawable.profile_image_1,
                replies = listOf(
                    Comment(
                        id = "5-1-1",
                        username = "angel_investor",
                        comment = "Monetization model?",
                        profileImageRes = R.drawable.profile_image_2
                    )
                )
            )
        )
    ),

    Comment(
        id = "6",
        username = "random_user_77",
        comment = "The debate dashboard idea is actually interesting.",
        profileImageRes = R.drawable.profile_image_1
    ),

    Comment(
        id = "7",
        username = "night_coder",
        comment = "Dark mode looks premium 💎",
        profileImageRes = R.drawable.profile_image_3
    ),

    Comment(
        id = "8",
        username = "kritika_designs",
        comment = "Spacing and typography are very clean. Good job!",
        profileImageRes = R.drawable.profile_image_2,
        replies = listOf(
            Comment(
                id = "8-1",
                username = "font_nerd",
                comment = "Which font are you using?",
                profileImageRes = R.drawable.profile_image_3
            )
        )
    ),

    Comment(
        id = "9",
        username = "debate_master",
        comment = "This could replace traditional comment sections.",
        profileImageRes = R.drawable.profile_image_1
    ),

    Comment(
        id = "10",
        username = "akash_live",
        comment = "Performance seems smooth even with nested replies 👌",
        profileImageRes = R.drawable.profile_image_2
    ),

    Comment(
        id = "11",
        username = "future_ceo",
        comment = "Add live polls and this becomes unstoppable.",
        profileImageRes = R.drawable.profile_image_3,
        replies = listOf(
            Comment(
                id = "11-1",
                username = "product_thinker",
                comment = "Yes, real-time engagement will boost retention.",
                profileImageRes = R.drawable.profile_image_1
            )
        )
    ),

    Comment(
        id = "12",
        username = "minimalist_raj",
        comment = "Clean. Focused. No unnecessary clutter.",
        profileImageRes = R.drawable.profile_image_2
    )
)
*/