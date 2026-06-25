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
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class NewRecipesViewModel(
    private val recipeRepository: RecipeRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val newRecipes : StateFlow<List<Recipe>> = recipeRepository.getNewRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val collections = if (SessionManagerFacade.currentUser.value?.isAnonymous == false) {
        userRepository.getCollectionsByOwner(SessionManagerFacade.currentUser.value?.uid.orEmpty())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    } else {
        MutableStateFlow(emptyList())
    }

    fun saveToCollections(recipeId: String, collectionIds: List<String>) {
        viewModelScope.launch {
            userRepository.saveRecipeToCollections(SessionManagerFacade.currentUser.value?.uid.orEmpty(), recipeId, collectionIds)
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val recipeRepository = application.container.recipeRepository
                val userRepository = application.container.userRepository
                NewRecipesViewModel(recipeRepository = recipeRepository, userRepository = userRepository)
            }
        }
    }
}