package com.example.finalapp.repository

import android.util.Log
import com.example.finalapp.database.Chat
import com.example.finalapp.database.ChatDao
import com.example.finalapp.database.Profile
import com.example.finalapp.database.ProfileDao
import com.example.finalapp.model.ChatList
import com.example.finalapp.model.Message
import com.example.finalapp.network.ApiService
import com.example.finalapp.utils.ApiResponse
import com.example.finalapp.utils.constants.Constants
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileDatabaseRepository @Inject constructor(private val profileDao: ProfileDao){

    fun getProfileDataFromDb():Flow<Profile?> {
        return profileDao.getProfileData()
    }
    suspend fun saveProfileDataInDb(profile: Profile){
        profileDao.saveProfileData(profile =profile)
    }
    suspend fun updateProfileDataInDB(profile: Profile) {
         profileDao.updateProfile(profile)
    }
    fun deleteProfileData(){

    }
    fun updateUserData(){

    }

}

@ViewModelScoped
class ChatDatabaseRepository @Inject constructor(private val chatDao: ChatDao,private val apiService: ApiService) {
    suspend fun saveChat(chat: Chat) {
        chatDao.saveChat(chat = chat)
    }

    fun getChat(userNumber: String): Flow<List<Chat>> {
        return chatDao.getChat(userNumber)
    }

    suspend fun getUserChatList(userID: String): Flow<ApiResponse<List<ChatList>>> = flow {
        emit(apiService.getUserChatList(userID))
    }.flowOn(Dispatchers.IO)

    suspend fun saveUserChatList(currentUserId: String, otherUserId: String): Flow<ApiResponse<ChatList>> =
        flow {emit(apiService.saveUserChatList(currentUserId, otherUserId))
    }.flowOn(Dispatchers.IO)

    suspend fun getMessages(userId: String, otherUserId: String): Flow<ApiResponse<List<Message>>> = flow {
        Log.d("Messageschat", "getAllMessages: called in repo")
        Log.i("Messageschat", "getMessages:$userId   $otherUserId ")
            emit( apiService.getChats("http://3.108.55.84/api/chat/api/chat/getMessages/${userId}/${otherUserId}"))
        }.flowOn(Dispatchers.IO)
}