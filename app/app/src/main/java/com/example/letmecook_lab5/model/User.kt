package com.example.letmecook_lab5.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val fullName: String = "",
    val nickname: String = "",
    val email: String = "",

    val cookingRole: String = "",

    val followers: Int = 0,
    val following: Int = 0,

    val dietaryPreferences: List<String> = emptyList(),
    val typesOfCuisine: List<String> = emptyList(),
    val favoriteIngredients: List<String> = emptyList() ,

    val profileImageUri: String? = null,

    val publishedRecipesIds: List<String> = emptyList(),
    val cookedRecipesIds: List<String> = emptyList(),
    val savedRecipesIds: List<String> = emptyList()
)