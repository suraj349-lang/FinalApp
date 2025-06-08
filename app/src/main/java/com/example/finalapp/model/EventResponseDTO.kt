package com.example.finalapp.model

data class EventResponseDTO(
    val success:Boolean=false,
    val code:Int=0,
    val data: EventRequestDTO = EventRequestDTO.empty()
)

data class PremiumEventResponseDTO(
    val success:Boolean,
    val code:Int,
    val data: EventRequestDTO
)