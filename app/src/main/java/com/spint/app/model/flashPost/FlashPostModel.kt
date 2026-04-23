package com.spint.app.model.flashPost

import com.google.gson.annotations.SerializedName
import com.spint.app.model.User
import kotlinx.serialization.Serializable

data class FlashPostRequestDto(
    val user: String,
    val userName: String,
    val title: String = "",
    val description: String = "",
    val image: String?,
    val category: String = "",
    val location: String = "",
    val offer: String = "",
    val isPrivate: Boolean = true,
    val parentPostId: String? = null,
    val expirationHours: Int,
) {
    companion object {
        fun empty(): FlashPostRequestDto {
            return FlashPostRequestDto(
                user = "",
                userName = "",
                title = "",
                description = "",
                image = "",
                category = "",
                location = "",
                offer = "",
                parentPostId = "",
                expirationHours = 0
            )
        }
    }
}

@Serializable
data class FlashPostResponse(
    val _id: String = "",
    val user: User? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String = "",
    val category: String = "",
    val isPrivate: Boolean = false,
    val location: String = "",
    val offer: String = "",
    val pingCount: Int = 0,
    val topPostsList: List<String>? = emptyList(),
    val expirationTime: String = "",
    val createdAt: String = "",
    val peopleJoined: Int = 0,
    val commentsCount: Int = 0,
    val topComments: List<CommentData>? = null,
    val totalChildPosts: Int = 0,
    val viewsCount: Int = 0,
    val totalUpVotes: Int = 0,
    val totalShared: Int = 0
)

@Serializable
data class FlashPostDetailsResponse(
    val _id: String = "",
    val user: User? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String = "",
    val category: String = "",
    val isPrivate: Boolean = false,
    val location: String = "",
    val offer: String = "",
    val pings: List<PingsOnPost>? = listOf(),
    val pingCount: Int = 0,
    val topPostsList: List<String>? = emptyList(),
    val expirationTime: String = "",
    val createdAt: String = "",
    val peopleJoined: Int = 0,
    val commentsCount: Int = 0,
    val topComments: List<CommentData>? = null,
    val totalChildPosts: Int = 0,
    val totalViews: Int = 0,
    val totalUpVotes: Int = 0,
    val totalShared: Int = 0
)

@Serializable
data class PingsOnPost(
    @SerializedName("post_id")
    val id: String,
    @SerializedName("user_id")
    val userId: User,
    val message: String,
    val pingCount: Int,
)


@Serializable
data class CommentData(
    val userName: String,
    val profileImage: String,
    val commentText: String
)

@Serializable
data class CommentRequest(
    val userId: String,
    val comment: String,
    val flashPostId: String,
    val parentCommentId: String? = null
)


@Serializable
data class CommentResponse(
    val id: String,
    val flashPostId: String,
    val userId: User,
    val comment: String,
    val replies: List<CommentResponse> = emptyList(),
    val parentCommentId: String? = null,
    val isExpanded: Boolean = false,
    val showReplyBox: Boolean = false,
    val replyText: String = ""
)


@Serializable
data class PingsOnFlashPostRequest(
    val userId: String,
    val flashPostId: String,
    val message: String
)

data class PingsOnFlashPostResponse(
    val userId: String,
    val flashPostId: String,
    val message: String
)