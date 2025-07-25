package com.example.finalapp.utils.testdata

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.finalapp.R

@Composable
fun FullEventScreen(event: Event, childPosts: List<ChildPost>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                            startY = 100f
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = event.creatorImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = event.creatorName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = getTimeLeft(event.expiryTime),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { /* Join logic */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier
                        .height(36.dp)
                ) {
                    Text("Join")
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${event.totalReactions}")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.share), contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${event.totalChildPosts} posts")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${event.views} views")
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(childPosts) { post ->
                ChildPostCard(post)
            }
        }
    }
}

@Composable
fun ChildPostCard(post: ChildPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = post.userImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(post.username, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.content, style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun getTimeLeft(expiryTime: Long): String {
    val now = System.currentTimeMillis()
    val diff = expiryTime - now
    val hours = diff / (1000 * 60 * 60)
    val minutes = (diff / (1000 * 60)) % 60
    return "$hours h $minutes min left"
}

// Data models
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val creatorImageUrl: String,
    val creatorName: String,
    val totalReactions: Int,
    val views: Int,
    val totalChildPosts: Int,
    val expiryTime: Long
)

data class ChildPost(
    val id: String,
    val username: String,
    val userImage: String,
    val content: String
)

@Preview(showBackground = true)
@Composable
fun PreviewFullEventScreen() {
    val sampleEvent = Event(
        id = "1",
        title = "Beach Clean-up Drive",
        description = "Join us for a community beach clean-up. Bags and gloves will be provided. Let's make a difference!",
        imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e", // Replace with your image
        creatorImageUrl = "https://randomuser.me/api/portraits/men/1.jpg",
        creatorName = "Alex Green",
        totalReactions = 120,
        views = 856,
        totalChildPosts = 14,
        expiryTime = System.currentTimeMillis() + (2 * 60 * 60 * 1000) // 2 hours from now
    )

    val samplePosts = listOf(
        ChildPost("1", "Nina", "https://randomuser.me/api/portraits/women/2.jpg", "I'll be there at 4 PM. Bringing my own trash picker!"),
        ChildPost("2", "Ravi", "https://randomuser.me/api/portraits/men/3.jpg", "Shared this with my local group. Hope many join!"),
        ChildPost("3", "Emily", "https://randomuser.me/api/portraits/women/4.jpg", "Is there parking available nearby?")
    )

    FullEventScreen(event = sampleEvent, childPosts = samplePosts)
}
