package com.example.letmecook_lab5.model

import java.util.UUID

data class Collection(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val recipeIds: List<String> = emptyList(),
    val ownerId: String = "",
    val description: String = "",
    val imageUrl: String? = null
)