package com.example.finalapp.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val profileDao: ProfileDao){

    fun getProfileData():Flow<Profile> {
        return profileDao.getProfileData()
    }
    suspend fun saveProfileData(profile: Profile){
        profileDao.saveProfileData(profile =profile)
    }
}