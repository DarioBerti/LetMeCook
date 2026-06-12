package com.example.letmecook_lab5.model
import com.example.letmecook_lab5.viewModel.NewRecipeState
import java.util.UUID

fun calculateCost(ingredients: List<Ingredient>): Double {
    return ingredients.sumOf { it.quantity * it.unitCost }
}

fun calculateCalories(ingredients: List<Ingredient>): Int {
    return ingredients.sumOf { it.quantity * it.unitCalories }.toInt()
}

fun NewRecipeState.toRecipe(): Recipe {
    return Recipe(
        id = UUID.randomUUID().toString(),
        title = title,
        imageUrl = imageUrl,
        difficulty = difficulty.name,
        cookingTime = cookingTime,
        servings = servings,
        cost = estimatedCost,
        ingredients = ingredients,
        calories = calories,
        steps = steps,
        tags = tags,
        inspiredByRecipeId = sourceRecipeId ?: ""
    )
}