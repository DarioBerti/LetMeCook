package com.example.letmecook_lab5.model

import com.google.firebase.firestore.PropertyName
import java.util.UUID

data class CartRecipe(
    val recipeId : String = UUID.randomUUID().toString(),
    val recipeTitle: String = "",
    val recipeImage: String = "",
    val servings : Int = 1,
    val ingredients: List<CartIngredient> = emptyList(),
)

data class CartIngredient(
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",

    @get:PropertyName("is_checked")
    @set:PropertyName("is_checked")
    var isChecked: Boolean = false
)
