package com.example.letmecook_lab5.ui.screens.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object RecipeOptions {
    var isOwner by mutableStateOf(false)
    var openMenu by mutableStateOf(false)
}