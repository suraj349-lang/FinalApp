package com.example.finalapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile_table ORDER BY id ASC")
    fun getProfileData(): Flow<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfileData(profile: Profile)
}