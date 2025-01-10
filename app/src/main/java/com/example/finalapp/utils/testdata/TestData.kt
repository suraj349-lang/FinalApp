package com.example.finalapp.utils.testdata
data class Item(
    val imageUrl: String,
    val name: String,
    val date: String
)

val items = listOf(
    Item(
        imageUrl = "https://randomuser.me/api/portraits/men/1.jpg",
        name = "Alice Johnson",
        date = "2025-01-01"
    ),
    Item(
        imageUrl = "https://randomuser.me/api/portraits/women/2.jpg",
        name = "Michael Smith",
        date = "2025-01-02"
    ),
    Item(
        imageUrl = "https://randomuser.me/api/portraits/men/3.jpg",
        name = "Sophia Williams",
        date = "2025-01-03"
    ),
    Item(
        imageUrl = "https://randomuser.me/api/portraits/women/4.jpg",
        name = "James Brown",
        date = "2025-01-04"
    ),
    Item(
        imageUrl = "https://randomuser.me/api/portraits/men/5.jpg",
        name = "Emma Davis",
        date = "2025-01-05"
    ),
    Item(
        imageUrl = "https://randomuser.me/api/portraits/women/6.jpg",
        name = "Liam Wilson",
        date = "2025-01-06"
    )
)
