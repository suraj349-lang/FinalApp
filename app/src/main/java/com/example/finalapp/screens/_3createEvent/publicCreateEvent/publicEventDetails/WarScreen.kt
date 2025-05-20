package com.example.finalapp.screens._3createEvent.publicCreateEvent.publicEventDetails


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalapp.R
import com.example.finalapp.utils.constants.Constants

@Preview(showBackground = true)
@Composable
fun WarScreen() {
    LazyColumn(modifier=Modifier.fillMaxSize()) {
        items(comments){comment->
            CommentItem(comment)
        }
    }

}
//
//@Composable
//fun CommentItem() {
//    Card(modifier = Modifier.padding(bottom = 2.dp)
//        .fillMaxWidth()
//        .height(30.dp), shape = RoundedCornerShape(0.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.primarySurface)) {
//        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//            Card(modifier = Modifier
//                .size(30.dp)
//                .padding(end = 8.dp), shape = CircleShape) {
//                Image(painter = painterResource(id = R.drawable.profile_image_1), contentDescription ="", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize())
//            }
//            Text(text = "@suraj3494", fontSize = 12.sp, fontFamily =Constants.FONT_MEDIUM, fontWeight = FontWeight.Bold,color = Color.Black, modifier = Modifier.padding(end=4.dp))
//            Text(text = "This is the best ig network . ...i.g..", fontSize = 12.sp, fontFamily =Constants.FONT_MEDIUM, color = Color.White)
//        }
//    }
//}
@Composable
fun CommentItem(comment: Comment, indentLevel: Int = 0) {
    Column(modifier = Modifier.padding(start = (indentLevel * 16).dp)) {
        Card(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth()
                .height(40.dp),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.primarySurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 8.dp),
                    shape = CircleShape
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_image_1),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Text(
                    text = "@${comment.username}",
                    fontSize = 12.sp,
                    fontFamily = Constants.FONT_MEDIUM,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = comment.comment,
                    fontSize = 12.sp,
                    fontFamily = Constants.FONT_MEDIUM,
                    color = Color.White
                )
            }
        }

        // Recursively show replies (if any)
        comment.replies.forEach {
            CommentItem(it, indentLevel = indentLevel + 1)
        }
    }
}


data class Comment(
    val username: String,
    val comment: String,
    val hasReplies: Boolean = false,
    val replies: List<Comment> = emptyList()
)

val comments = listOf(
    Comment(username = "suraj_3494", comment = "This is the best app!", hasReplies = true,
        replies = listOf(
            Comment(username = "sakshi7687", comment = "I know, it's my daily driver now!", hasReplies = true,
                replies = listOf(
                    Comment(username = "code_buddy", comment = "Same! The UI is insane.", hasReplies = true,
                        replies = listOf(
                            Comment(username = "dev_monster",comment = "Give props to the design team 💯", hasReplies = false)
                        )
                    ),
                    Comment(username = "kritika_k", comment = "I deleted 3 apps after this one!", hasReplies = false
                    )
                )
            ),
            Comment(
                username = "tech_guru",
                comment = "Dark mode is 🔥🔥🔥",
                hasReplies = false
            )
        )
    ),
    Comment(
        username = "anupam_mittal",
        comment = "Saves me so much time!",
        hasReplies = true,
        replies = listOf(
            Comment(
                username = "priya_xoxo",
                comment = "Exactly, feels like it's made for me.",
                hasReplies = true,
                replies = listOf(
                    Comment(
                        username = "lazy_dev",
                        comment = "Real productivity boost 💪",
                        hasReplies = false
                    )
                )
            )
        )
    ),
    Comment(
        username = "positron_piecerer",
        comment = "Is it open source? Just curious.",
        hasReplies = true,
        replies = listOf(
            Comment(
                username = "opensource_nerd",
                comment = "Not yet. Maybe soon?",
                hasReplies = true,
                replies = listOf(
                    Comment(
                        username = "foss_lover",
                        comment = "Would be great for community input!",
                        hasReplies = false
                    )
                )
            )
        )
    ),
    Comment(
        username = "anshuman",
        comment = "Best onboarding experience I've had.",
        hasReplies = true,
        replies = listOf(
            Comment(
                username = "product_designer_7",
                comment = "We're working to make it even better 🚀",
                hasReplies = false
            )
        )
    )
)
