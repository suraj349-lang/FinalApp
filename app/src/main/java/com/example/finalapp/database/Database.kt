package com.example.finalapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Profile::class,Chat::class], version = 1, exportSchema = false)
abstract class FrisbeeDatabase : RoomDatabase() {
    abstract fun profileDao():ProfileDao
    abstract fun chatDao():ChatDao
}