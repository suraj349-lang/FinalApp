package com.example.finalapp.screens._3createEventOrPing.createEvent.eventWarScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R

@Preview(showBackground = true)
@Composable
fun WarScreen() {
    // Use mutable state list for top-level comments
    val commentsState = remember { mutableStateListOf<Comment>().apply { addAll(comments) } }

    // Function to toggle expand/collapse for any comment (recursive update)
    fun toggleExpand(target: Comment) {
        val updated = updateCommentExpandState(commentsState, target)
        if (!updated) println("Comment not found")
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
fun updateCommentExpandState(comments: MutableList<Comment>, target: Comment): Boolean {
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
@Composable
fun CommentItem(
    comment: Comment,
    indentLevel: Int = 0,
    onToggleExpand: (Comment) -> Unit,
    onToggleReplyBox: (Comment) -> Unit,
    onReplyTextChange: (Comment, String) -> Unit,
    onSendReply: (Comment) -> Unit
) {
    var isExpanded by remember { mutableStateOf(comment.isExpanded) }

    val maxChars = 135
    val isLongComment = comment.comment.length > maxChars

    val displayCommentText = when {
        isExpanded || !isLongComment -> comment.comment
        else -> comment.comment.take(maxChars)
    }

    val displayText = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.Black)) {
            append("@${comment.username} ")
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
                append(" show less")
            }
            pop()
        }
    }

    Column(modifier = Modifier.padding(start = (indentLevel * 16).dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(
                    if (indentLevel == 0) Color(0xFF753CC5) else Color(0xFF1B70C4),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = comment.profileImageRes),
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
        Text(
            text = "Reply",
            fontSize = 12.sp,
            modifier = Modifier
                .clickable { onToggleReplyBox(comment) }
                .padding(start = 48.dp),
            color = Color.Gray
        )

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


data class Comment(
    val id: String,
    val username: String,
    val comment: String,
    val profileImageRes: Int,
    val replies: List<Comment> = emptyList(),
    val isExpanded: Boolean = false,
    val showReplyBox: Boolean = false,
    val replyText: String = ""
)


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
    )
)
