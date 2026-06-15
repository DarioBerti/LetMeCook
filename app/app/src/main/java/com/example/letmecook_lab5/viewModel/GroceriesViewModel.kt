package com.example.letmecook_lab5.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.domain.GroceriesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.auth.SessionManagerFacade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class GroceriesTabs { BY_RECIPE, ALL_INGREDIENTS }

data class groceriesUiState(
    val tabSelected : GroceriesTabs = GroceriesTabs.BY_RECIPE
)
class GroceriesViewModel(
    private val groceriesRepository: GroceriesRepository,
    private val userId : String,
) : ViewModel() {

    private val _uiState = MutableStateFlow(groceriesUiState())
    val uiState: StateFlow<groceriesUiState> = _uiState.asStateFlow()

    val cartItems = groceriesRepository.getCartItems(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleIsChecked(recipeId: String, ingredientName: String) {
        groceriesRepository.toggleIsChecked(userId, recipeId, ingredientName)
    }

    fun updateRecipeServings(recipeId: String, newServings: Int) {
        groceriesRepository.updateRecipeServings(userId, recipeId, newServings)
    }

    fun onTabSelected(tab: GroceriesTabs) {
        _uiState.value = _uiState.value.copy(tabSelected = tab)
    }

    fun deleteRecipe(recipeId: String) {
        try{
            viewModelScope.launch {
                groceriesRepository.deleteRecipe(userId, recipeId)
            }
        }catch (e: Exception){
            Log.d("GroceriesViewModel", "Errore delete: ${e.message}")
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val groceriesRepository = application.container.groceriesRepository
                val userId = SessionManagerFacade.currentUser.value?.uid ?: ""
                GroceriesViewModel(groceriesRepository, userId)
            }
        }
    }
}