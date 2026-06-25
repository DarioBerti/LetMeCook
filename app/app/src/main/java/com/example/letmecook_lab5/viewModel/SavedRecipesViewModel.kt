package com.example.letmecook_lab5.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.letmecook_lab5.repository.FirebaseStorageRepository
import java.util.UUID
import com.example.letmecook_lab5.auth.SessionManagerFacade

enum class SavedTab { COLLECTIONS, ALL_RECIPES }

data class SavedRecipesUiState(
    val collections: List<Collection> = emptyList(),
    val savedRecipes: List<Recipe> = emptyList(),
    val selectedTab: SavedTab = SavedTab.COLLECTIONS,
    val isLoading: Boolean = true
)

class SavedRecipesViewModel(
    private val repo: RecipeRepository,
    private val userRepo : UserRepository,
    private val storageRepo: FirebaseStorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedRecipesUiState())
    val uiState: StateFlow<SavedRecipesUiState> = _uiState.asStateFlow()
    private val userId: String
        get() = SessionManagerFacade.currentUser.value?.uid.orEmpty()

    init {
        viewModelScope.launch {
            combine(
                userRepo.getCollectionsByOwner(userId),
                repo.getAllRecipes(),
                repo.getRecipesByOwner(userId)
            ) { cols, others, own ->
                val all = (others + own).distinctBy { it.id }
                val existingIds = all.map { it.id }.toSet()
                val cleanedCols = cols.map { col ->
                    col.copy(recipeIds = col.recipeIds.filter { it in existingIds })
                }
                val savedIds = cleanedCols.flatMap { it.recipeIds }.toSet()
                val saved = all.filter { it.id in savedIds }
                cleanedCols to saved
            }.collect { (cols, saved) ->
                _uiState.update { it.copy(collections = cols, savedRecipes = saved, isLoading = false) }
            }
        }
    }

    fun selectTab(tab: SavedTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun createCollection(name: String, description: String, imageUrl: String? = null) {
        viewModelScope.launch {
            val uploadedUrl = storageRepo.uploadIfLocal(
                imageUrl,
                "collection_images/${UUID.randomUUID()}.jpg"
            )
            val newCollection = Collection(
                name = name,
                description = description,
                ownerId = userId,
                imageUrl = uploadedUrl
            )
            userRepo.addCollection(userId, newCollection)
        }
    }

    fun saveToCollections(recipeId: String, collectionIds: List<String>) {
        viewModelScope.launch {
            userRepo.saveRecipeToCollections(SessionManagerFacade.currentUser.value?.uid.orEmpty(), recipeId, collectionIds)
        }
    }

    fun deleteCollection(collectionId: String) {
        viewModelScope.launch {
            userRepo.deleteCollection(userId, collectionId)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer{
                    val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                    val recipeRepository = application.container.recipeRepository
                    val userRepository = application.container.userRepository
                    val storageRepository = application.container.storageRepository
                    SavedRecipesViewModel(recipeRepository, userRepository, storageRepository)

            }
        }
    }
}