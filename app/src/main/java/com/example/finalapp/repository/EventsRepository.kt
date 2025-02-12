package com.example.finalapp.repository

import android.content.Context
import android.net.Uri
import com.example.finalapp.model.DirectChat
import com.example.finalapp.model.GetDropProfileResponseModel
import com.example.finalapp.model.OfferModel
import com.example.finalapp.model.User
import com.example.finalapp.model.SingleOfferModel
import com.example.finalapp.model.ImageUploadResponse
import com.example.finalapp.model.OfferResponseModel
import com.example.finalapp.network.ApiService
import com.example.finalapp.screens._4profile.uriToMultipart
import com.example.finalapp.utils.ApiResponse
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class EventsRepository @Inject constructor(private val api: ApiService) {

    fun setLocationForDirectChat(directChat: DirectChat):Flow<ApiResponse<DirectChat>> = flow {
        emit(api.setLocationForDirectChat(directChat))
    }.flowOn(Dispatchers.IO)
    fun getDirectChatUsers(lat:Double,long:Double):Flow<ApiResponse<List<User>>> = flow {
        emit(api.getDirectChatUsers(lat,long))
    }.flowOn(Dispatchers.IO)

    fun sendCreateEventData(offerData: OfferModel): Flow<SingleOfferModel> = flow  {
        emit(api.premiumCreateEvent(offerData))
    }.flowOn(Dispatchers.IO)

    fun getAllDropProfiles(): Flow<GetDropProfileResponseModel> = flow  {
        emit(api.getAllDropProfiles())
    }.flowOn(Dispatchers.IO)
   suspend fun createEvent(data:OfferModel):Resource<SingleOfferModel>{
        return try {
          Resource.Loading(data=true)
           val createEventsResponse =api.createEvent(data)
           if(createEventsResponse.success){
               Resource.Loading(data=false)
           }
            Resource.Success(data=createEventsResponse)
       }catch (e:Exception){
           Resource.Error(e.message.toString())

       }

   }
    fun getAllEvents(): Flow<OfferResponseModel> = flow {
        emit(api.getAllEvents())
    }.flowOn(Dispatchers.IO)


    suspend fun uploadImage(imageUri: Uri, context: Context): Flow<ImageUploadResponse> = flow {
        emit(withContext(Dispatchers.IO) {
            val filePart = uriToMultipart(imageUri, context)
            api.uploadImage(filePart) })
    }.flowOn(Dispatchers.IO)
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