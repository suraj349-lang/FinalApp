package com.example.finalapp.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0,
    val title:String,
    val body:String,
    val timeStamp:Long =System.currentTimeMillis(),
    val isRead:Boolean=false,
    val type:String? =null
){
    companion object{
        fun empty() {
            NotificationEntity(
                id = 0,
                title = "",
                body = "",
                timeStamp = System.currentTimeMillis(),
                isRead = false,
                type = "chat"
            )
        }
    }
}
