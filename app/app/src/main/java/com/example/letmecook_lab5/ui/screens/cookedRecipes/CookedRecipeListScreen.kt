package com.example.letmecook_lab5.ui.screens.cookedRecipes


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.letmecook_lab5.ui.components.recipeList.ModalBottomIngredients
import com.example.letmecook_lab5.ui.components.recipeList.*
import com.example.letmecook_lab5.viewModel.CookedRecipeListUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookedRecipeProposalList (
    uiState: CookedRecipeListUiState,
    recipes: List<Recipe>,
    onTitleChanged: (String) -> Unit,
    onIngredientSelected: (String) -> Unit,
    onIngredientDeselected: (String) -> Unit,
    onTagSelected: (String) -> Unit,
    onTagDeselected: (String) -> Unit,
    onMaxCostUpdate: (Double) -> Unit,
    onMinCostUpdate: (Double) -> Unit,
    onRecipeClick: (String) -> Unit,
){

    var isFilterExpanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
            onTrailingButton = { if (!uiState.isLoading ) isFilterExpanded = !isFilterExpanded }
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
                totalTags = uiState.tags
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
        }else {

            Box() {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                ) {
                    items(recipes) { recipe ->
                        MyRecipeCard(recipe, onClick = { onRecipeClick(recipe.id) })
                        Log.d("CookedRecipe", recipe.toString())
                    }
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
