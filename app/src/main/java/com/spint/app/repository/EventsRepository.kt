package com.spint.app.repository

import android.content.Context
import android.net.Uri
import com.spint.app.model.DirectChat
import com.spint.app.model.DirectChatApiResponse
import com.spint.app.model.DirectChatRequest
import com.spint.app.model.GetDropProfileResponseModel
import com.spint.app.model.Event
import com.spint.app.model.EventResponseDTO
import com.spint.app.model.ImageUploadResponse
import com.spint.app.model.AllEventsResponseDTO
import com.spint.app.model.AllPingsResponseDTO
import com.spint.app.model.CreateFlashPostResponse
import com.spint.app.model.EventDetailsResponse
import com.spint.app.model.FlashPostResponseDTO
import com.spint.app.model.PremiumEventResponseDTO
import com.spint.app.model.flashPost.CommentRequest
import com.spint.app.model.flashPost.CommentResponse
import com.spint.app.model.flashPost.FlashPostRequestDto
import com.spint.app.model.flashPost.FlashPostResponse
import com.spint.app.model.flashPost.PingsOnFlashPostRequest
import com.spint.app.model.flashPost.PingsOnFlashPostResponse
import com.spint.app.model.places.Place
import com.spint.app.network.ApiService
import com.spint.app.screens._4profile.uriToMultipart
import com.spint.app.utils.PingsResponse
import com.spint.app.utils.ApiResponse
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@ViewModelScoped
class EventsRepository @Inject constructor(private val api: ApiService) {

    fun setLocationForDirectChat(directChat: DirectChatRequest):Flow<ApiResponse<DirectChat>> = flow {
        emit(api.setLocationForDirectChat(directChat))
    }.flowOn(Dispatchers.IO)
    suspend fun getAllDirectChatUsers(userId:String,lat:Double, long:Double, page:Int): Response<DirectChatApiResponse> {
        return api.getDirectChatUsers(userId,lat,long,page)
    }

    fun removeUserFromDirectChat(id: String):Flow<ApiResponse<String>> = flow {
        emit(api.removeUserFromDirectChat(id))
    }.flowOn(Dispatchers.IO)

    fun sendPremiumCreateEventData(event: Event): Flow<PremiumEventResponseDTO> = flow  {
        emit(api.premiumCreateEvent(event))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllDropProfiles(page:Int): Response<GetDropProfileResponseModel> {
        return api.getAllDropProfiles(page)
    }

   suspend fun createEvent(data:Event):Flow<EventResponseDTO> = flow {
       emit(api.createEvent(data))
   }.flowOn(Dispatchers.IO)
    fun getAllEvents(): Flow<AllEventsResponseDTO> = flow {
        emit(api.getAllEvents())
    }.flowOn(Dispatchers.IO)

    fun getUserEvents(id:String): Flow<AllEventsResponseDTO> = flow {
        emit(api.getUserEvents(id))
    }.flowOn(Dispatchers.IO)

    fun getEventDetails(id: String): Flow<EventDetailsResponse> = flow {
        emit(api.getEventDetails(id))
    }.flowOn(Dispatchers.IO)

    fun upvoteEvent(id: String): Flow<String> = flow {
        emit(api.upvoteEvent(id))
    }.flowOn(Dispatchers.IO)

    fun getUserPings(id:String): Flow<AllPingsResponseDTO> = flow {
        emit(api.getUserFlashPosts(id))
    }.flowOn(Dispatchers.IO)

    fun getUserFlashPostDetails(id:String): Flow<FlashPostResponseDTO> = flow {
        emit(api.getUserFlashPostDetails(id))
    }.flowOn(Dispatchers.IO)
    fun getUserDropProfiles(id:String): Flow<GetDropProfileResponseModel> = flow {
        emit(api.getUserDropProfiles(id))
    }.flowOn(Dispatchers.IO)


    suspend fun uploadImage(imageUri: Uri, context: Context): Flow<ImageUploadResponse> = flow {
        emit(withContext(Dispatchers.IO) {
            val filePart = uriToMultipart(imageUri, context)
            api.uploadImage(filePart) })
    }.flowOn(Dispatchers.IO)

     fun createFlashPost(data:FlashPostRequestDto):Flow<CreateFlashPostResponse> = flow {
        emit(api.createFlashPost(data))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllFlashPosts(page:Int): PingsResponse<List<FlashPostResponse>> {
        return api.getAllFlashPosts(page)
    }
    suspend fun registerFlashPostView(postId: String, userId: String): Response<String> {
        return api.registerView(postId, userId)
    }
    suspend fun getFlashPostComments(flashPostId: String): Flow<ApiResponse<List<CommentResponse>>> = flow {
        emit(api.getComments(flashPostId))
    }.flowOn(Dispatchers.IO)

    suspend fun addFlashPostComments(commentRequest: CommentRequest): Flow<ApiResponse<CommentResponse>> = flow {
        emit(api.addComment(commentRequest))
    }.flowOn(Dispatchers.IO)

    suspend fun deleteFlashPostComments(flashPostId: String): Flow<ApiResponse<String>> = flow {
        emit(api.deleteComment(flashPostId))
    }.flowOn(Dispatchers.IO)


    suspend fun addPingToFlashPost(pingsOnFlashPostRequest: PingsOnFlashPostRequest):Flow<PingsOnFlashPostResponse> = flow {
        emit(api.addPingToFlashPost(pingsOnFlashPostRequest))
    }.flowOn(Dispatchers.IO)

    suspend fun getPlaces(query: String): List<Place> {
        if (query.length < 3) return emptyList()

        return try {
            api.getAutocomplete(query).map {
                Place(
                    name = it.name,
                    address = it.address,
                    lat = it.lat,
                    lng = it.lng
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


}

//data class DataOrException<T,Boolean,E:Exception>(
//    var data:T?=null,
//    var loading:Boolean?=null,
//    var e:E?=null
//)

sealed class Resource<T>(val data:T?=null,val message:String?=null){
    class Empty<T> : Resource<T>()
    class Success<T>(data:T):Resource<T>(data)
    class Error<T>(message:String?,data: T?=null):Resource<T>(data,message)
    class Loading<T>(data:T):Resource<T>(data)

}