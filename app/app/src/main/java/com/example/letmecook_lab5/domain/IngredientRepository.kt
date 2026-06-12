package com.example.letmecook_lab5.domain

import com.example.letmecook_lab5.model.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {
    suspend fun getAllIngredients(): List<Ingredient>
}