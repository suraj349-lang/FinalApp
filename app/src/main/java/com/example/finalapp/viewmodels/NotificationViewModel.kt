package com.example.finalapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.finalapp.database.tables.NotificationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.finalapp.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications

    init {
        loadNotifications()
    }

     fun loadNotifications() {
        viewModelScope.launch {
            _notifications.value = repository.getAll()
        }
    }

    fun save(notification: NotificationEntity) {
        viewModelScope.launch {
            repository.saveNotification(notification)
            loadNotifications()
        }
    }

    fun markAsRead(id: Long) {
        viewModelScope.launch {
            repository.markRead(id)
            loadNotifications()
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
            loadNotifications()
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
            loadNotifications()
        }
    }
}
