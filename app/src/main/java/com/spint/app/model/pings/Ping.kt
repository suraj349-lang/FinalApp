package com.spint.app.model.pings

import com.spint.app.model.User
import kotlinx.serialization.Serializable

data class PingRequestDto(
    val user: String,
    val userName:String,
    val title: String = "",
    val description: String="",
    val image: String?,
    val category: String = "",
    val location: String = "",
    val offer: String = "",
    val isPrivate :Boolean=true,
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

@Serializable
data class FlashPostResponse(
    val _id:String="",
    val user: User? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String = "",
    val category: String = "",
    val isPrivate: Boolean=false,
    val location: String = "",
    val offer: String = "",
    val topPostsList:List<String>? = emptyList(),
    val expirationTime: String = "",
    val createdAt:String="",
    val peopleJoined:Int=0,
    val totalComments:Int=0,
    val topComments:List<CommentData> ? =null,
    val totalChildPosts:Int =0,
    val totalViews:Int=0,
    val totalUpVotes:Int=0,
    val totalShared:Int=0
)

@Serializable
data class CommentData(
    val userName:String,
    val profileImage:String,
    val commentText:String
)

