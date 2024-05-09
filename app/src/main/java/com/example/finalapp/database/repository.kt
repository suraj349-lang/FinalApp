package com.example.finalapp.database

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileDatabaseRepository @Inject constructor(private val profileDao: ProfileDao){

    fun getProfileData():Flow<Profile> {
        return profileDao.getProfileData()
    }
    suspend fun saveProfileData(profile: Profile){
        profileDao.saveProfileData(profile =profile)
    }
}

@ViewModelScoped
class ChatDatabaseRepository @Inject constructor(private val chatDao: ChatDao){
    suspend fun saveChat(chat: Chat){
        chatDao.saveChat(chat =chat)
    }
    suspend fun getChat(userNumber: String):List<Chat>{
        return chatDao.getChat(userNumber)
    }
}