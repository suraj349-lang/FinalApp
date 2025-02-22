package com.example.finalapp.repository

import com.example.finalapp.database.Chat
import com.example.finalapp.database.ChatDao
import com.example.finalapp.database.Profile
import com.example.finalapp.database.ProfileDao
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileDatabaseRepository @Inject constructor(private val profileDao: ProfileDao){

    fun getProfileDataFromDb():Flow<Profile> {
        return profileDao.getProfileData()
    }
    suspend fun saveProfileDataInDb(profile: Profile){
        profileDao.saveProfileData(profile =profile)
    }

    fun deleteProfileData(){

    }
    fun updateUserData(){

    }

}

@ViewModelScoped
class ChatDatabaseRepository @Inject constructor(private val chatDao: ChatDao){
    suspend fun saveChat(chat: Chat){
        chatDao.saveChat(chat =chat)
    }
     fun getChat(userNumber: String):Flow<List<Chat>>{
        return chatDao.getChat(userNumber)
    }
}