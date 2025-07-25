package com.example.finalapp.screens._8notification

data class TimeLineItem(
    val id: String,
    val type: String, // message, event_created, ping_upvoted, etc.
    val title: String,
    val description: String,
    val timestamp: Long,
    val isRead: Boolean = false
)
