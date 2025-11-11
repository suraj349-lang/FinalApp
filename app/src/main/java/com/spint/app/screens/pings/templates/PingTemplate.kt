package com.spint.app.screens.pings.templates

import com.spint.app.R

data class VisualPingTemplate(
    val id: String,
    val icon: String,
    val title: String,
    val defaultCaption: String,
    val imageResId: Int,
    val tags: List<String>, // used for search
    val requiredFields: List<String>
)
val visualPingTemplates = listOf(
    VisualPingTemplate(
        id = "study_group",
        icon = "📚",
        title = "Join a Study Group",
        defaultCaption = "Let’s study together at the library, 5PM today!",
        imageResId = R.drawable.personal,
        tags = listOf("study", "group", "exam", "library"),
        requiredFields = listOf("location", "deadline")
    ),
    VisualPingTemplate(
        id = "hangout",
        icon = "☕",
        title = "Coffee Hangout",
        defaultCaption = "Anyone up for coffee near campus?",
        imageResId = R.drawable.coffeecup,
        tags = listOf("coffee", "meet", "hangout", "chill"),
        requiredFields = listOf("location")
    ),
    VisualPingTemplate(
        id = "sell_food",
        icon = "🍛",
        title = "Selling Lunch Boxes",
        defaultCaption = "Fresh homemade biryani available today!",
        imageResId = R.drawable.dinner,
        tags = listOf("sell", "food", "biryani", "homemade"),
        requiredFields = listOf("location", "description", "deadline")
    ),
    // Add more...
)
