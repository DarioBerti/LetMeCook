package com.example.letmecook_lab5.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.ui.components.recipeList.FilterMenu
import com.example.letmecook_lab5.ui.components.recipeList.ModalBottomIngredients
import com.example.letmecook_lab5.ui.components.recipeList.RecipeCard
import com.example.letmecook_lab5.ui.components.recipeList.SearchAndFiltersSection
import com.example.letmecook_lab5.viewModel.RecipeListUiState



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: RecipeListUiState,
    recipes: List<Recipe>,
    onTitleChanged: (String) -> Unit,
    onIngredientSelected: (String) -> Unit,
    onIngredientDeselected: (String) -> Unit,
    onTagSelected: (String) -> Unit,
    onTagDeselected: (String) -> Unit,
    onMaxCostUpdate: (Double) -> Unit,
    onMinCostUpdate: (Double) -> Unit,
    onRecipeClick: (String) -> Unit,
    isLogged: Boolean
){

    var isFilterExpanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = true )
    val scope = rememberCoroutineScope ()
    var bottomMenu by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ){
        SearchAndFiltersSection(
            query = uiState.inputName,
            onQueryChanged = onTitleChanged,
            onTrailingButton = { if (!uiState.isLoading) isFilterExpanded = !isFilterExpanded }
        )

        AnimatedVisibility(
            visible = isFilterExpanded
        ) {
            FilterMenu(
                ingredientsSelected = uiState.inputSelectedIngredients,
                onIngredientDeselect = onIngredientDeselected,
                onIngredientsDisplay = { bottomMenu = true },
                onTagSelect = onTagSelected,
                onTagDeselect = onTagDeselected,
                inputMinCost = uiState.inputMinCost,
                inputMaxCost = uiState.inputMaxCost,
                minCost = uiState.totalMinCost,
                maxCost = uiState.totalMaxCost,
                onMaxCostUpdate = onMaxCostUpdate,
                onMinCostUpdate = onMinCostUpdate,
                tagsSelected = uiState.inputSelectedTags,
                totalTags = uiState.tags,
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.padding(vertical = 8.dp))



                recipes.forEach { recipe ->
                    RecipeCard(recipe, onClick = { onRecipeClick(recipe.id) }, isLogged = isLogged)
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                }

                if (bottomMenu) {
                    ModalBottomIngredients(
                        uiState.ingredients,
                        onDismiss = { bottomMenu = false },
                        sheetState = sheetState,
                        scope = scope,
                        onAdd = onIngredientSelected
                    )
                }
            }
        }
    }

}