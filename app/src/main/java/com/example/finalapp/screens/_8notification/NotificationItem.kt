package com.example.finalapp.screens._8notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.finalapp.R
import com.example.finalapp.database.tables.NotificationEntity
import com.example.finalapp.enums.Notifications
import com.example.finalapp.navigation.SCREENS
import com.example.finalapp.utils.constants.Constants
import com.example.finalapp.utils.formatDateTime
import com.example.finalapp.utils.toRelativeTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationItem(
    notification: NotificationEntity,
    onMarkRead: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = if (notification.isRead) Color.White.copy(alpha = 0.9f) else Color(0xFFCBD9E6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .clickable { onMarkRead() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                ) {
                    Image(
                        painter  =  painterResource(id = R.drawable.app_icon_dynamic),
                        contentDescription = "",
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Crop
                    )
                    //AsyncImage(
////                        model  = if(notification.type?.lowercase() == Notifications.CHAT.value  ) notification.body else painterResource(id = R.drawable.app_icon_dynamic),
//
//                        contentDescription = "",
//                        modifier = Modifier.size(36.dp),
//                        contentScale = ContentScale.Crop
//                    )
                    Column(modifier = Modifier.padding(start = 4.dp)) {
                        Text(
                            text = notification.body,
                            fontFamily = Constants.FONT_MEDIUM,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = notification.timeStamp.toRelativeTime(),
                            fontFamily = Constants.FONT_EXTRA_LIGHT,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                // Menu Icon with dropdown
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "Options",
                            tint = Color.Gray,
                            modifier = Modifier.size(12.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                expanded = false
                                onDelete()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mark as Read") },
                            onClick = {
                                expanded = false
                                onMarkRead()
                            }
                        )
                        // Add more menu items as needed
                        DropdownMenuItem(text = { Text("Mute") }, onClick = { expanded = false })
                        DropdownMenuItem(text = { Text("Pin") }, onClick = { expanded = false })
                        DropdownMenuItem(text = { Text("Report") }, onClick = { expanded = false })
                    }
                }
            }
        }
    }
}
