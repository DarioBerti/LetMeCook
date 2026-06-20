package com.example.letmecook_lab5.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.letmecook_lab5.repository.TagProvider
import com.example.letmecook_lab5.ui.screens.newRecipe.NewRecipeScreen
import com.example.letmecook_lab5.ui.screens.newRecipe.RecapScreen
import com.example.letmecook_lab5.viewModel.NewRecipeViewModel

fun NavGraphBuilder.newRecipeGraph(
    navController: NavHostController
) {
    navigation<NewRecipeGraph>(startDestination = NewRecipeRoute()) {

        composable<NewRecipeRoute> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<NewRecipeGraph>()
            }

            val viewModel : NewRecipeViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = NewRecipeViewModel.Factory
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val route = backStackEntry.toRoute<NewRecipeRoute>()
            LaunchedEffect(route.editRecipeId, route.sourceRecipeId) {
                when {
                    route.editRecipeId != null   -> viewModel.initForEdit(route.editRecipeId)
                    route.sourceRecipeId != null -> viewModel.initFromRecipeId(route.sourceRecipeId)
                }
            }

            NewRecipeScreen(
                uiState = uiState,
                onTitleChanged = viewModel::titleChanged,
                onImageChanged = viewModel::imageChanged,
                onDifficultyChanged = viewModel::difficultyChanged,
                onCookingTimeChanged = viewModel::cookingTimeChanged,
                onAddIngredient = viewModel::addIngredient,
                onRemoveIngredient = viewModel::removeIngredient,
                onIncreaseServings = viewModel::increaseServings,
                onDecreaseServings = viewModel::decreaseServings,
                onAddStep = viewModel::addStep,
                onNextStep = viewModel::nextStep,
                onSave = { MainActions(navController).goNewRecipeRecap() },
                onClearError = viewModel::clearError,
                onStepTitleChanged = viewModel::stepTitleChanged,
                onStepDescriptionChanged = viewModel::stepDescriptionChanged,
                onStepImageChanged = viewModel::stepImageChanged,
                onBackStep = viewModel::goBackStep,
                onDeleteStep = viewModel::deleteCurrentStep
            )
        }

        composable<NewRecipeRecapRoute> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry<NewRecipeGraph>()
            }

            val viewModel : NewRecipeViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = NewRecipeViewModel.Factory
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val context = LocalContext.current
            val availableTags = remember {
                TagProvider.getAllTags(context)
            }

            RecapScreen(
                title = uiState.title,
                imageUrl = uiState.imageUrl,
                errorMessage = uiState.errorMessage,
                tags = uiState.tags,
                availableTags = availableTags,
                onPublish = {
                    if (uiState.editingRecipeId != null) viewModel.updateRecipe()
                    else viewModel.saveRecipe()
                    MainActions(navController).goPublishedRecipes()
                },
                onKeepEditing = {
                    viewModel.keepEditing()
                    navController.popBackStack()
                },
                onAddTag = viewModel::addTag,
                onRemoveTag = viewModel::removeTag,
                onClearError = viewModel::clearError
            )
        }
    }
}