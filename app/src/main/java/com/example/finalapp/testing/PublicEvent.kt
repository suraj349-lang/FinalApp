package com.example.finalapp.testing

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.finalapp.R


@Preview(showBackground = true)
@Composable
fun PostScreen(
    eventImage: String="",
    topic: String="State Election",
    location: String="Barh, Patna",
    dateTime: String="05 jun, 2025",
    peopleJoined: List<String> = listOf("",""),
    viewCount: Int=100,
    commentCount: Int=100,
    shareCount: Int=200,
    childPostCount: Int=400,
    isUserJoined: Boolean=false,
    onJoinClick: () -> Unit={},
    onReportClick: () -> Unit={}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Event Image
        Box {
            Image(
                painter = painterResource(id = R.drawable.profile_image_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
            IconButton(
                onClick = { onReportClick() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
            ) {
                Image(painter = painterResource(id = R.drawable.arrow_down), contentDescription = "More", colorFilter = ColorFilter.tint(Color.White))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Event Info
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(topic, style = MaterialTheme.typography.headlineSmall)
            Text("📍 $location", color = Color.Gray)
            Text("🕒 $dateTime", color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            // Stats Row
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                StatIconText(image =R.drawable.people, text = "${peopleJoined.size} Joined")
                StatIconText(image = R.drawable.view, text = "$viewCount Views")
                StatIconText(image =R.drawable.comment_filled, text = "$commentCount Comments")
                StatIconText(image =R.drawable.share, text = "$shareCount Shares")
                StatIconText(image = R.drawable.war_room, text = "$childPostCount Posts")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Join Button
            Button(
                onClick = { onJoinClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isUserJoined) "Go to War" else "Join Event")
            }
        }
    }
}

@Composable
fun StatIconText(image: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        Image(painter = painterResource(id =image) , contentDescription = null, colorFilter = ColorFilter.tint(
            Color.DarkGray), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
