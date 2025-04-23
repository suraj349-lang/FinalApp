package com.example.finalapp.model

data class AllEventsResponseDTO(
    val success:Boolean,
    val code:Int,
    val data:List<EventResponse>
)
