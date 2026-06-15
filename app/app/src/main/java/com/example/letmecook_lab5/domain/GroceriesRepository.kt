package com.example.letmecook_lab5.domain

import com.example.letmecook_lab5.model.CartRecipe
import kotlinx.coroutines.flow.Flow

interface GroceriesRepository {
    fun getCartItems( userId: String ): Flow<List<CartRecipe>>
    suspend fun addToCart( userId: String, item: CartRecipe)
    fun toggleIsChecked(userId: String, recipeId: String, ingredientName: String)
    fun updateRecipeServings(userId: String, recipeId: String, newServings: Int)

    suspend fun deleteRecipe(userId: String, recipeId: String)
}