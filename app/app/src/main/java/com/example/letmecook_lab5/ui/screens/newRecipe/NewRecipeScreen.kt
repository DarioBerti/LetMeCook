package com.example.letmecook_lab5.ui.screens.newRecipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.letmecook_lab5.ui.components.newRecipe.RecipeForm
import com.example.letmecook_lab5.model.Difficulty
import com.example.letmecook_lab5.viewModel.NewRecipeState
import com.example.letmecook_lab5.model.Ingredient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeScreen(
    uiState: NewRecipeState,
    onTitleChanged: (String) -> Unit,
    onImageChanged: (String) -> Unit,
    onDifficultyChanged: (Difficulty) -> Unit,
    onCookingTimeChanged: (Int) -> Unit,
    onAddIngredient: (Ingredient) -> Unit,
    onRemoveIngredient: (Int) -> Unit,
    onIncreaseServings: () -> Unit,
    onDecreaseServings: () -> Unit,
    onAddStep: () -> Unit,
    onNextStep: () -> Unit,
    onSave: () -> Unit,
    onClearError: () -> Unit,
    onStepTitleChanged: (String) -> Unit,
    onStepDescriptionChanged: (String) -> Unit,
    onStepImageChanged: (String) -> Unit,
    onBackStep: () -> Unit,
    onDeleteStep: () -> Unit
) {

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        if (uiState.currentStepIndex == -1) {
            RecipeForm(
                title = uiState.title,
                imageUrl = uiState.imageUrl,
                difficulty = uiState.difficulty,
                cookingTime = uiState.cookingTime,
                servings = uiState.servings,
                steps = uiState.steps,
                ingredients = uiState.ingredients,
                availableIngredients = uiState.availableIngredients,
                titleChanged = onTitleChanged,
                addIngredient = onAddIngredient,
                removeIngredient = onRemoveIngredient,
                imageChanged = onImageChanged,
                cookingTimeChanged = onCookingTimeChanged,
                difficultyChanged = onDifficultyChanged,
                increaseServings = onIncreaseServings,
                decreaseServings = onDecreaseServings,
                addStep = onAddStep,
                nextStep = onNextStep,
                saveRecipe = onSave,
                errorMessage = uiState.errorMessage,
                onClearError = onClearError
            )
        } else {
            val step = uiState.steps[uiState.currentStepIndex]

            StepEditorScreen(
                step = step,
                steps = uiState.steps,
                stepIndex = uiState.currentStepIndex,
                stepTitleChanged = onStepTitleChanged,
                stepDescriptionChanged = onStepDescriptionChanged,
                stepImageChanged = onStepImageChanged,
                onAddStep = onAddStep,
                onNextStep = onNextStep,
                onBack = onBackStep,
                onDelete = onDeleteStep,
                onSave = onSave,
                errorMessage = uiState.errorMessage,
                onClearError = onClearError
            )
        }
    }
}