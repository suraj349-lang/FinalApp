package com.example.finalapp.model

data class EventRequestDTO(
  val user: String="66867295208440d0e3e36ed1",
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
  val user: User = User(),
  val title: String? = null, // for public only
  val description: String? = null, // for public only
  val image: String = "",
  val category: String = "",
  val location: String = "",
  val offer: String = "",
  val isPrivate: Boolean = true,
  val expirationTime: String = ""
)
