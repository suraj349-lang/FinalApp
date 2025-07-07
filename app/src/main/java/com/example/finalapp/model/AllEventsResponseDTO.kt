package com.example.finalapp.model

import com.example.finalapp.model.pings.PingResponse

data class AllEventsResponseDTO(
    val success:Boolean,
    val code:Int,
    val data:List<EventResponse>
)


data class AllPingsResponseDTO(
    val success:Boolean,
    val code:Int,
    val data:List<PingResponse>
)