package com.example.finalapp.model

data class EventResponseDTO(
    val success:Boolean=false,
    val code:Int=0,
    val data: Event = Event.empty()
)

data class PremiumEventResponseDTO(
    val success:Boolean,
    val code:Int,
    val data: Event
)

data class EventDetailsResponse(
    val success:Boolean=false,
    val code:Int=0,
    val data: EventResponse
)