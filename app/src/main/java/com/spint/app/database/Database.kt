package com.spint.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spint.app.database.dao.NotificationDao
import com.spint.app.database.tables.NotificationEntity

@Database(entities = [Profile::class,Chat::class,NotificationEntity::class], version = 6, exportSchema = false)
abstract class FrisbeeDatabase : RoomDatabase() {
    abstract fun profileDao():ProfileDao
    abstract fun chatDao():ChatDao
    abstract fun NotificationDao():NotificationDao
}