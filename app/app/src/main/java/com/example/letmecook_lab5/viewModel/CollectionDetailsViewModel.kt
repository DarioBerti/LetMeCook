package com.example.letmecook_lab5.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.navigation.toRoute
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.navigation.CollectionDetailRoute
import com.example.letmecook_lab5.auth.SessionManagerFacade
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


data class CollectionDetailsUiState(
    val collection: Collection? = null,
    val recipes: List<Recipe> = emptyList()
)

class CollectionDetailsViewModel(
    savedStateHandle : SavedStateHandle,
    private val userRepository : UserRepository,
    private val recipeRepository: RecipeRepository

) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionDetailsUiState())
    val uiState: StateFlow<CollectionDetailsUiState> = _uiState.asStateFlow()

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

    private val collectionRoute = savedStateHandle.toRoute<CollectionDetailRoute>()
    private val collectionId = collectionRoute.collectionId
    init {
        viewModelScope.launch {
            combine(
                userRepository.getCollectionsByOwner(SessionManagerFacade.currentUser.value?.uid.orEmpty()),
                recipeRepository.getAllRecipes(),
            ) { cols, all ->
                val col = cols.firstOrNull { it.id == collectionId }
                val recipes = all.filter { it.id in (col?.recipeIds ?: emptyList()) }
                col to recipes
            }.collect { (col, recipes) ->
                val cleanedCol = col?.copy(recipeIds = recipes.map { it.id })
                _uiState.update { it.copy(collection = cleanedCol, recipes = recipes) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val recipeRepository = application.container.recipeRepository
                val userRepository = application.container.userRepository
                val savedStateHandle = createSavedStateHandle()
                CollectionDetailsViewModel(
                    savedStateHandle = savedStateHandle,
                    userRepository = userRepository,
                    recipeRepository = recipeRepository,
                )
            }
        }
    }
}