package com.spint.app.model

import com.spint.app.model.pings.FlashPostResponse

data class AllEventsResponseDTO(
    val success:Boolean,
    val code:Int,
    val data:List<EventResponse>
)


data class AllPingsResponseDTO(
    val success:Boolean,
    val code:Int,
    val data:List<FlashPostResponse>
)