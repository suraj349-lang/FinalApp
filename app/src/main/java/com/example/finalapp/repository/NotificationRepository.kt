package com.example.finalapp.repository

import com.example.finalapp.database.dao.NotificationDao
import com.example.finalapp.database.tables.NotificationEntity



import javax.inject.Inject

class NotificationRepository @Inject constructor(private val dao: NotificationDao) {

    suspend fun saveNotification(notification: NotificationEntity) = dao.insert(notification)

    suspend fun getAll() = dao.getAllNotifications()

    suspend fun markRead(id: Long) = dao.markAsRead(id)

    suspend fun delete(id: Long) = dao.delete(id)

    suspend fun clearAll() = dao.clearAll()
}
