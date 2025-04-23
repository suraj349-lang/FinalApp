package com.example.finalapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile_table order by id desc limit 1")
    fun getProfileData(): Flow<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfileData(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

}


@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChat(chat: Chat)

    @Query("SELECT * FROM chat_table WHERE sentTo = :userNumber or sentFrom=:userNumber ORDER BY id ASC")
     fun getChat(userNumber: String): Flow<List<Chat>>

}