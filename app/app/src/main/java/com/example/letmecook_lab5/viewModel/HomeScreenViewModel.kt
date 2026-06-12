package com.example.letmecook_lab5.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.domain.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY


class HomeScreenViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    val topTen : StateFlow<List<Recipe>> = recipeRepository.getTopTenRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val newRecipes : StateFlow<List<Recipe>> = recipeRepository.getNewRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val fastRecipes : StateFlow<List<Recipe>> = recipeRepository.getFastRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val recipeRepository = application.container.recipeRepository
                HomeScreenViewModel(recipeRepository = recipeRepository)
            }
        }
    }
}