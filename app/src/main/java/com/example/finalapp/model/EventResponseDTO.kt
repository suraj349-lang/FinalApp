package com.example.finalapp.model

data class EventResponseDTO(
    val success:Boolean,
    val code:Int,
    val data: EventRequestDTO
){
    constructor():this(false,0,EventRequestDTO())
}

data class PremiumEventResponseDTO(
    val success:Boolean,
    val code:Int,
    val data: EventRequestDTO
)