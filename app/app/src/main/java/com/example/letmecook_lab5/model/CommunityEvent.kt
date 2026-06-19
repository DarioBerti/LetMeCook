package com.example.letmecook_lab5.model

import javax.annotation.meta.TypeQualifierNickname

data class CommunityEvent (
    val id: String,
    val type: CommunityEventType,
    val userId: String,
    val userName: String,
    val userNickname: String,
    val userImage: String?,
    val recipeId: String,
    val recipeTitle: String,
    val text: String,
    val recipeImage: String?,
    val reviewImage: String?,
    val timestamp: Long
)

enum class CommunityEventType {
    NEW_RECIPE,
    REVIEW
}
