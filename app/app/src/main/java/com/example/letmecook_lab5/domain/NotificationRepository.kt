package com.example.letmecook_lab5.domain

import com.example.letmecook_lab5.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(userId: String): Flow<List<Notification>>
    suspend fun sendNotification(notification: Notification)
    suspend fun markAsRead(notificationId: String, userId: String)
    suspend fun deleteNotification(notificationId: String, userId: String)
}