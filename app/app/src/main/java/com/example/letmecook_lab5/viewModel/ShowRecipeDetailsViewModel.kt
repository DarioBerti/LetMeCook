package com.example.letmecook_lab5.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.navigation.RecipeDetailRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import kotlinx.coroutines.flow.first
import com.example.letmecook_lab5.auth.SessionManagerFacade

data class ShowRecipeDetailsUiState(
    val recipe: Recipe? = null,
    val isDeleted: Boolean = false,
    val collections: List<Collection> = emptyList(),
    val ownerName: String = "",
    val ownerAvatar: String? = null
)

class ShowRecipeDetailsViewModel(
    savedStateHandle : SavedStateHandle,
    private val userRepo: UserRepository,
    private val repo: RecipeRepository
) : ViewModel() {

    private val recipeDetailsRoute = savedStateHandle.toRoute<RecipeDetailRoute>()
    private val recipeId = recipeDetailsRoute.recipeId

    private val _uiState = MutableStateFlow(ShowRecipeDetailsUiState())
    val uiState: StateFlow<ShowRecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("ShowRecipeDetailsViewModel", "RecipeId: $recipeId")
            val recipe = repo.getRecipeById(recipeId)
            _uiState.update { it.copy(recipe = recipe) }

            val owner = userRepo.getUserById(recipe.ownerId).first()
            _uiState.update {
                it.copy(
                    ownerName = owner?.nickname?.takeIf { n -> n.isNotBlank() } ?: owner?.fullName ?: "",
                    ownerAvatar = owner?.profileImageUri
                )
            }
        }
        viewModelScope.launch {
            userRepo.getCollectionsByOwner(SessionManagerFacade.currentUser.value?.uid.orEmpty())                .collect { cols -> _uiState.update { it.copy(collections = cols) } }
        }
    }
    fun deleteRecipe() {
        viewModelScope.launch {
            repo.deleteRecipe(recipeId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }
    fun saveToCollections(collectionIds: List<String>) {
        viewModelScope.launch {
            userRepo.saveRecipeToCollections(SessionManagerFacade.currentUser.value?.uid.orEmpty(), recipeId, collectionIds)
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val recipeRepository = application.container.recipeRepository
                val userRepository = application.container.userRepository
                val savedStateHandle = createSavedStateHandle()
                ShowRecipeDetailsViewModel(
                    savedStateHandle = savedStateHandle,
                    userRepo = userRepository,
                    repo = recipeRepository
                )
            }
        }
    }
}