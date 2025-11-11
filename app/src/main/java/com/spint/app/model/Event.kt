package com.spint.app.model

import com.spint.app.model.pings.PingResponse

data class Event(
    val user: String,
    val userName:String,
    val title: String = "",
    val description: String="",
    val image: String,
    val location: String = "",
    val isChildPost :Boolean=false,
    val parentPostId:String?=null,
    val childPosts:List<Event> = listOf(),
    val expirationTime: String,
){
  companion object{
    fun empty():Event{
      return Event(
        user="",
        userName = "",
        title = "",
        description = "",
        image = "",
        location = "",
        isChildPost = false,
        parentPostId = "",
        childPosts = listOf(),
        expirationTime = ""
      )
    }
  }
}


data class EventResponse(
  val _id:String,
  val user: User = User(),
  val userName: String,
  val title: String="",
  val description: String? = null,
  val image: String = "",
  val location: String = "",
  val isChildPost: Boolean=false,
  val childPosts:List<EventResponse>? = emptyList(),
  val childPings:List<PingResponse>? = emptyList(),
  val childDropProfiles:List<DropProfileResponse>? = emptyList(),
  val childNearByUsers:List<User>? = emptyList(),
  val expirationTime: String = "",
  val upvoted:Boolean=false,
  val downvoted:Boolean=false,
  val totalUpVotes:Int=0,
  val totalDownVotes:Int=0,
  val totalJoined:Int=0,
  val totalComments:Int=0,
  val topComments:List<CommentData> ? =null,
  val totalChildPosts:Int =0,
  val totalViews:Int=0
)

data class CommentData(
  val userName:String,
  val profileImage:String,
  val commentText:String
)
