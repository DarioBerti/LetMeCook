package com.example.letmecook_lab5.model

import com.google.firebase.firestore.Exclude
import java.util.UUID

data class Recipe (
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val averageRating: Double = 0.0,
    val tags: List<String> = emptyList(),
    val difficulty: String = "",
    val servings: Int = 1,
    val cookingTime: Int = 0,    // minutes
    val calories: Int = 0,
    val cost: Double = 0.0,
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<Step> = emptyList(),
    val inspiredByRecipeId: String = "",

    @get:Exclude
    val reviews: List<Review> = emptyList(),
    val storageInstructions: String = "",
    val createdAt: Long = System.currentTimeMillis()
)