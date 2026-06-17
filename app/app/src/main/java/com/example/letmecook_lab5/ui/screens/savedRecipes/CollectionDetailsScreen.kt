package com.example.letmecook_lab5.ui.screens.savedRecipes

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.ui.components.common.TopAppLetMeCook
import com.example.letmecook_lab5.ui.components.recipeList.RecipeCard
import com.example.letmecook_lab5.viewModel.CollectionDetailsViewModel

@Composable
fun CollectionDetailsRoute(
    onBack: () -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    viewModel: CollectionDetailsViewModel = viewModel(
        factory = CollectionDetailsViewModel.Factory
    ),
    isLogged: Boolean
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppLetMeCook("Collection details", onClick = onBack)
        uiState.collection?.let { col ->
            CollectionRow(collection = col, onClick = {}, clickable = false)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            uiState.recipes.forEach { recipe ->
                RecipeCard(recipe, onClick = { onRecipeClick(recipe) }, isLogged = isLogged)
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}