package com.example.letmecook_lab5.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.ui.components.common.SaveRecipeDialog
import com.example.letmecook_lab5.ui.components.recipeList.RecipeCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipesScreen(
    recipes: List<Recipe>,
    onRecipeClick: (String) -> Unit,
    collections: List<Collection>,
    onSaveToCollections: (String, List<String>) -> Unit,
    isLogged: Boolean
){
    var showSaveDialog by remember { mutableStateOf("") }

    if (showSaveDialog.isNotBlank()) {
        SaveRecipeDialog(
            recipeId = showSaveDialog,
            collections = collections,
            onSave = onSaveToCollections,
            onDismiss = { showSaveDialog = "" }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            recipes.forEach { recipe ->
                RecipeCard(
                    recipe, onClick = { onRecipeClick(recipe.id) },
                    isLogged = isLogged,
                    isSaved = collections.any { recipe.id in it.recipeIds },
                    toggleSaveDialog = { recipeId -> showSaveDialog = recipeId }
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}