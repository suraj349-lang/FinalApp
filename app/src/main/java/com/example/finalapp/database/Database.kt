package com.example.finalapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Profile::class], version = 1, exportSchema = false)
abstract class FrisbeeDatabase : RoomDatabase() {
    abstract fun profileDao():ProfileDao
}