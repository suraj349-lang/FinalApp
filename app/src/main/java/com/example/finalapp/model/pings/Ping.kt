package com.example.finalapp.model.pings

import com.example.finalapp.model.User

data class PingRequestDto(
    val user: String,
    val userName:String,
    val title: String = "",
    val description: String="",
    val image: String,
    val category: String = "",
    val location: String = "",
    val offer: String = "",
    val isChildPost :String="",
    val parentPostId:String?=null,
    val expirationTime: String,
){
    companion object{
        fun empty():PingRequestDto{
            return PingRequestDto(
                user="",
                userName = "",
                title = "",
                description = "",
                image = "",
                category = "",
                location = "",
                offer = "",
                parentPostId = "",
                expirationTime = ""
            )
        }
    }
}


data class PingResponse(
    val _id:String,
    val user: User? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String = "",
    val category: String = "",
    val location: String = "",
    val offer: String = "",
    val topPostsList:List<String>? = emptyList(),
    val expirationTime: String = "",
    val peopleJoined:Int=0,
    val totalComments:Int=0,
    val topComments:List<CommentData> ? =null,
    val totalChildPosts:Int =0,
    val totalViews:Int=0,
    val totalUpVotes:Int=0
)

data class CommentData(
    val userName:String,
    val profileImage:String,
    val commentText:String
)

