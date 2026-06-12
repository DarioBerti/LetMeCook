package com.example.letmecook_lab5.model

import java.util.UUID

data class Review(
    val id: String = UUID.randomUUID().toString(),
    val recipeId: String = "",
    val authorId: String = "",
    val authorFullName: String = "",
    val authorNickname: String = "",
    val rating: Int = 0,
    val imageUrl: String = "",
    val comment: String = "",
    val doTips: List<String> = emptyList(),
    val dontTips: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
