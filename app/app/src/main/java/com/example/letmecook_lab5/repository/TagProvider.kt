package com.example.letmecook_lab5.repository

import android.content.Context
import com.example.letmecook_lab5.R

/*
object TagProvider {
    val allTags = listOf(
        "american", "appetizer", "baking", "beef", "breakfast",
        "luxury", "pasta", "pizza", "quick", "raw", "vegetarian"
    )
}
*/

object TagProvider {
 fun getAllTags(context: Context): List<String> {
  return context.resources .getStringArray(R.array.recipe_tags) .toList()
 }
}