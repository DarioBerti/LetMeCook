package com.example.letmecook_lab5.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.repository.TagProvider
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.R
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.domain.IngredientRepository
import com.example.letmecook_lab5.domain.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RecipeListUiState(
    val isLoading: Boolean = true,
    val inputName: String = "",
    val inputSelectedIngredients: Set<String> = emptySet(),
    val totalMaxCost : Double = Double.MAX_VALUE,
    val totalMinCost : Double = 0.0,
    val inputMaxCost: Double = Double.MAX_VALUE,
    val inputMinCost: Double = 0.0,
    val inputSelectedTags: Set<String> = emptySet(),
    val ingredients: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)

class RecipeListViewModel(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository : IngredientRepository,
    private val userRepository: UserRepository,
    private val tags: List<String>
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeListUiState())
    val uiState: StateFlow<RecipeListUiState> = _uiState.asStateFlow()
    val filteredRecipes : StateFlow<List<Recipe>> = combine(
        recipeRepository.getAllRecipes(),
        _uiState
    ){
            recipes, uiState ->
        recipes.filter { recipe ->
            recipe.title.contains(uiState.inputName, ignoreCase = true)
                    && recipe.cost <= uiState.inputMaxCost
                    && recipe.cost >= uiState.inputMinCost
                    && ( uiState.inputSelectedIngredients.isEmpty() || recipe.ingredients.map {it.name}.any { uiState.inputSelectedIngredients.contains(it) })
                    && (uiState.inputSelectedTags.isEmpty() || recipe.tags.any { uiState.inputSelectedTags.contains(it) })
        }
    }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            val currentUser = userRepository
                .getUserById(SessionManagerFacade.currentUser.value?.uid.orEmpty()).first()
            val defaultTags = currentUser?.dietaryPreferences
                ?.map { it.lowercase() }
                ?.toSet()
                ?: emptySet()
            recipeRepository.getAllRecipes().collect { recipes ->
                if (recipes.isNotEmpty()) {
                    val min = recipes.minOfOrNull { it.cost } ?: 0.0
                    val max = recipes.maxOfOrNull { it.cost } ?: Double.MAX_VALUE

                    _uiState.update { currentState ->
                        currentState.copy(
                            totalMinCost = min,
                            totalMaxCost = max,
                            inputMaxCost = max,
                            inputMinCost = min,
                            isLoading = false,
                            ingredients = ingredientRepository.getAllIngredients()
                                .map { it.name },
                            tags = tags,
                            inputSelectedTags = defaultTags
                        )
                    }
                }
                else {
                    Log.d("RecipeViewModel", "Attenzione: Il Flow ha emesso una lista vuota di ricette!")
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun updateInputTitle(title: String) {
        _uiState.update { it.copy(inputName = title) }
    }

    fun updateInputMaxCost(maxCost: Double) {
        _uiState.update { it.copy(inputMaxCost = maxCost) }
    }

    fun updateInputMinCost(minCost: Double) {
        _uiState.update { it.copy(inputMinCost = minCost) }
    }


    fun addIngredientFilter(ingredient: String) {
        _uiState.update { it.copy(inputSelectedIngredients = it.inputSelectedIngredients + ingredient) }
    }

    fun removeIngredientFilter(ingredient: String) {
        _uiState.update { it.copy(inputSelectedIngredients = it.inputSelectedIngredients - ingredient) }
    }

    fun addTagFilter(tag: String) {
        _uiState.update { it.copy(inputSelectedTags = it.inputSelectedTags + tag) }
    }

    fun removeTagFilter(tag: String) {
        _uiState.update { it.copy(inputSelectedTags = it.inputSelectedTags - tag) }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val recipeRepository = application.container.recipeRepository
                val ingredientRepository = application.container.ingredientRepository
                val userRepository = application.container.userRepository
                val tags = application.resources
                    .getStringArray(R.array.recipe_tags)
                    .toList()
                RecipeListViewModel(recipeRepository = recipeRepository, ingredientRepository = ingredientRepository, userRepository = userRepository, tags = tags)
            }
        }
    }
}