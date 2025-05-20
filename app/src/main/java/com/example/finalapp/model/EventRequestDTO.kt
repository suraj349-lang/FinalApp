package com.example.finalapp.model

data class EventRequestDTO(
  val user: String="",
  val userName:String="",
  val title: String? = null,
  val description: String? = null,
  val image: String = "",
  val category: String = "",
  val location: String = "",
  val offer: String = "",
  val isPrivate: Boolean = true,
  val expirationTime: String = ""
)


data class EventResponse(
  val _id:String,
  val user: User? = null,
  val title: String? = null, // for public only
  val description: String? = null, // for public only
  val image: String = "",
  val category: String = "",
  val location: String = "",
  val offer: String = "",
  val isPrivate: Boolean = true,
  val expirationTime: String = ""
)
