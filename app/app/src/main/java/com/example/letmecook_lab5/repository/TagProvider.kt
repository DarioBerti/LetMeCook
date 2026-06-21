package com.example.letmecook_lab5.repository

import android.content.Context
import com.example.letmecook_lab5.R

object TagProvider {
 fun getAllTags(context: Context): List<String> {
  return context.resources .getStringArray(R.array.recipe_tags) .toList()
 }
}