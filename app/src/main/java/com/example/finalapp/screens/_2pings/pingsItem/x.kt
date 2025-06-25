package com.example.finalapp.screens._2pings.pingsItem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val creator: String,
    val mediaUrl: String
)

@Preview(showBackground = true)
@Composable
fun ClubVibeApp() {
    var showCreateEvent by remember { mutableStateOf(false) }
    val events = remember { mutableStateListOf(
        Event(1, "Epic Club Night Out", "2025-06-25", "8:00 PM", "Downtown Club", "JohnDoe", "https://via.placeholder.com/300x200?text=Club+Night"),
        Event(2, "Raging Dance Party", "2025-06-26", "9:00 PM", "City Hall", "JaneSmith", "https://via.placeholder.com/300x200?text=Dance+Party")
    ) }
    var newEvent by remember { mutableStateOf(Event(0, "", "", "", "", "", "")) }
    var selectedMedia by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1A237E)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A237E), Color(0xFF4A148C))
                    )
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 ClubVibe - Create & Join! 🎉",
                    color = Color(0xFFFFEB3B),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                )
                Button(
                    onClick = { showCreateEvent = true },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Create New Event 🚀", color = Color.White, fontSize = 16.sp)
                }
                LazyColumn(
                    modifier = Modifier.padding(12.dp)
                ) {
                    items(events.size) { index ->
                        EventCard(event = events[index])
                    }
                }
            }
        }

        if (showCreateEvent) {
            AlertDialog(
                onDismissRequest = { showCreateEvent = false },
                title = { Text("Create Event", color = Color(0xFFFFCA28)) },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newEvent.title,
                            onValueChange = { newEvent = newEvent.copy(title = it) },
                            label = { Text("Event Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newEvent.date,
                            onValueChange = { newEvent = newEvent.copy(date = it) },
                            label = { Text("Date") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newEvent.time,
                            onValueChange = { newEvent = newEvent.copy(time = it) },
                            label = { Text("Time") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newEvent.location,
                            onValueChange = { newEvent = newEvent.copy(location = it) },
                            label = { Text("Location") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newEvent.creator,
                            onValueChange = { newEvent = newEvent.copy(creator = it) },
                            label = { Text("Your Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                selectedMedia?.let {
                                    newEvent = newEvent.copy(mediaUrl = it)
                                    events.add(newEvent.copy(id = events.size + 1))
                                    showCreateEvent = false
                                    newEvent = Event(0, "", "", "", "", "", "")
                                    selectedMedia = null
                                   // Toast.makeText(, "Event Created!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.align(Alignment.End),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Save Event", color = Color.White)
                        }
                        Button(
                            onClick = { selectedMedia = "https://via.placeholder.com/300x200?text=Custom+Media" }, // Placeholder, replace with actual upload logic
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Upload Image/Video", color = Color.White)
                        }
                        if (selectedMedia != null) {
                            AsyncImage(
                                model = selectedMedia,
                                contentDescription = "Selected Media",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showCreateEvent = false }) {
                        Text("Cancel", color = Color(0xFFFF4444))
                    }
                }
            )
        }
    }
}

@Composable
fun EventCard(event: Event) {
    var isHovered by remember { mutableStateOf(false) }
    val animatedElevation by animateDpAsState(targetValue = if (isHovered) 16.dp else 8.dp)
    val animatedColor by animateColorAsState(targetValue = if (isHovered) Color(0xFFE91E63) else Color(0xFFF44336))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(animatedElevation, RoundedCornerShape(12.dp))
            .clickable { isHovered = !isHovered },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
        border = BorderStroke(2.dp, Color(0xFFFFCA28)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${event.title} 🎵",
                color = Color(0xFFFFCA28),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = "📅 ${event.date}", color = Color.LightGray, fontSize = 16.sp)
            Text(text = "⏰ ${event.time}", color = Color.LightGray, fontSize = 16.sp)
            Text(text = "📍 ${event.location}", color = Color.LightGray, fontSize = 16.sp)
            Text(text = "👤 ${event.creator}", color = Color.LightGray, fontSize = 16.sp)
            AsyncImage(
                model = event.mediaUrl,
                contentDescription = "${event.title} Preview",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(top = 12.dp)
            )
            Button(
                onClick = {
                    //Toast.makeText(LocalContext.current, "🎊 You're in for ${event.title}!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = animatedColor),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text("Join Now! 🚀", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
