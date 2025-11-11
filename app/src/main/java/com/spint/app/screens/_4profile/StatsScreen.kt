package com.spint.app.screens._4profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spint.app.R

@Composable
fun UserStatsScreen(
    pingsCount: Int,
    eventsCount: Int,
    dropsCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Your Activity Stats",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("Pings", pingsCount, R.drawable.ping, Color(0xFF00D26A))
            StatCard("Events", eventsCount, R.drawable.event, Color(0xFF3F51B5))
            StatCard("Drops", dropsCount, R.drawable.drop_profile_new, Color(0xFFFF6F61))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recent Activity",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ActivityItem("Joined \"Beach Party\" Event", "2 days ago", R.drawable.event)
            ActivityItem("Created Ping: \"Who’s down for a movie?\"", "3 days ago", R.drawable.ping)
            ActivityItem("Dropped profile at \"Live Gig\"", "5 days ago", R.drawable.drop_profile_new)
        }
    }
}

@Composable
fun StatCard(title: String, count: Int, iconRes: Int, color: Color) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
               // tint = color,
                modifier = Modifier.size(28.dp)
            )
            Text(text = count.toString(), fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
            Text(text = title, color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun ActivityItem(title: String, time: String, iconRes: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = Color(0xFF888888),
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(text = title, color = Color.White, fontSize = 14.sp)
            Text(text = time, color = Color.LightGray, fontSize = 12.sp)
        }
    }
}
