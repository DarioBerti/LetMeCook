package com.example.letmecook_lab5.model

import java.util.UUID

data class Notification(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType = NotificationType.TEST,
    val relatedId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

enum class NotificationType {
    TEST,
    RECIPE_DUPLICATED,
    REVIEW_RECEIVED,
    RECOMMENDATION
}