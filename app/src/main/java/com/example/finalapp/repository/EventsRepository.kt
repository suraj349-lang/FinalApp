package com.example.finalapp.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.DirectChatApiResponse
import com.example.finalapp.model.DirectChatRequest
import com.example.finalapp.model.GetDropProfileResponseModel
import com.example.finalapp.model.Event
import com.example.finalapp.model.EventResponseDTO
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.AllEventsResponseDTO
import com.example.finalapp.model.AllPingsResponseDTO
import com.example.finalapp.model.CreatePingResponse
import com.example.finalapp.model.EventDetailsResponse
import com.example.finalapp.model.PremiumEventResponseDTO
import com.example.finalapp.model.pings.PingRequestDto
import com.example.finalapp.model.pings.PingResponse
import com.example.finalapp.network.ApiService
import com.example.finalapp.screens._4profile.uriToMultipart
import com.example.finalapp.utils.AllPingsResponse
import com.example.finalapp.utils.ApiResponse
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
    suspend fun getAllDirectChatUsers(lat:Double, long:Double, page:Int): Response<DirectChatApiResponse> {
        return api.getDirectChatUsers(lat,long,page)
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
        emit(api.getUserPings(id))
    }.flowOn(Dispatchers.IO)
    fun getUserDropProfiles(id:String): Flow<GetDropProfileResponseModel> = flow {
        emit(api.getUserDropProfiles(id))
    }.flowOn(Dispatchers.IO)


    suspend fun uploadImage(imageUri: Uri, context: Context): Flow<ImageUploadResponse> = flow {
        emit(withContext(Dispatchers.IO) {
            val filePart = uriToMultipart(imageUri, context)
            api.uploadImage(filePart) })
    }.flowOn(Dispatchers.IO)

    suspend fun createPing(data:PingRequestDto):Flow<CreatePingResponse> = flow {
        emit(api.createPing(data))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllPings(page:Int): AllPingsResponse<List<PingResponse>> {
        return api.getAllPings(page)
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