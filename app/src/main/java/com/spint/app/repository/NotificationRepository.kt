package com.spint.app.repository

import com.spint.app.database.dao.NotificationDao
import com.spint.app.database.tables.NotificationEntity



import javax.inject.Inject

class NotificationRepository @Inject constructor(private val dao: NotificationDao) {

    suspend fun saveNotification(notification: NotificationEntity) = dao.insert(notification)

    suspend fun getAll() = dao.getAllNotifications()

    suspend fun markRead(id: Long) = dao.markAsRead(id)

    suspend fun delete(id: Long) = dao.delete(id)

    suspend fun clearAll() = dao.clearAll()
}
