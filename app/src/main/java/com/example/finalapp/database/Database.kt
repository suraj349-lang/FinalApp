package com.example.finalapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalapp.database.dao.NotificationDao
import com.example.finalapp.database.tables.NotificationEntity

@Database(entities = [Profile::class,Chat::class,NotificationEntity::class], version = 6, exportSchema = false)
abstract class FrisbeeDatabase : RoomDatabase() {
    abstract fun profileDao():ProfileDao
    abstract fun chatDao():ChatDao
    abstract fun NotificationDao():NotificationDao
}