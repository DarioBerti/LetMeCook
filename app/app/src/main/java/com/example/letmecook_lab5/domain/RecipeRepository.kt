package com.example.letmecook_lab5.domain

import com.example.letmecook_lab5.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun getRecipesByOwner(ownerId: String): Flow<List<Recipe>>
    fun getTopTenRecipes(): Flow<List<Recipe>>
    fun getNewRecipes(): Flow<List<Recipe>>
    fun getFastRecipes(): Flow<List<Recipe>>
    suspend fun getRecipeById(recipeId: String): Recipe
    suspend fun addRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipeId: String)
    suspend fun updateRecipe(recipe: Recipe)

}