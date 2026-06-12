package com.example.letmecook_lab5.ui.components.newRecipe

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.Difficulty
import com.example.letmecook_lab5.model.Ingredient
import com.example.letmecook_lab5.model.Step
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.DifficultyInputBox
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.ImageInputBox
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.IngredientsInputBox
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.PrepTimeInputBox
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.TitleInputBox
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.ServingsInputBox
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.AddIngredientDialog
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.BottomButtons

@Composable
fun RecipeForm(
    title: String,
    imageUrl: String,
    cookingTime: Int,
    difficulty: Difficulty,
    servings: Int,
    ingredients: List<Ingredient>,
    availableIngredients: List<Ingredient>,
    steps: List<Step>,
    titleChanged: (String) -> Unit,
    imageChanged: (String) -> Unit,
    cookingTimeChanged: (Int) -> Unit,
    difficultyChanged: (Difficulty) -> Unit,
    increaseServings: () -> Unit,
    decreaseServings: () -> Unit,
    errorMessage: String?,
    onClearError: () -> Unit,
    addIngredient: (Ingredient) -> Unit,
    removeIngredient: (Int) -> Unit,
    addStep: () -> Unit,
    nextStep: () -> Unit,
    saveRecipe: () -> Unit
){

    var showIngredientDialog by remember { mutableStateOf(false) }  // addIngredient modal

    val filteredIngredients = availableIngredients.filter { available ->    // avoiding showing ingredients that are already selected
        ingredients.none { it.name == available.name }
    }

    if (showIngredientDialog) {
        AddIngredientDialog(
            availableIngredients = filteredIngredients,
            onAdd = {
                addIngredient(it)
                showIngredientDialog = false
            },
            onDismiss = { showIngredientDialog = false }
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageChanged(it.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()) // for scrolling
    ) {
        if (errorMessage != null) {
            AlertDialog(
                onDismissRequest = onClearError,
                title = { Text("Validation Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = onClearError) {
                        Text("OK")
                    }
                }
            )
        }

        ImageInputBox(
            imageUrl = imageUrl,
            onPickImage = { galleryLauncher.launch("image/*") },
            onRemoveImage = { imageChanged("") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        TitleInputBox(
            title = title,
            onTitleChange = titleChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        DifficultyInputBox(
            difficulty = difficulty,
            difficultyChanged = difficultyChanged,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))

        PrepTimeInputBox(
            cookingTime = cookingTime,
            cookingTimeChanged = cookingTimeChanged,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        ServingsInputBox(
            servings = servings,
            increaseServings = increaseServings,
            decreaseServings = decreaseServings,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        IngredientsInputBox(
            ingredients = ingredients,
            addIngredient = addIngredient,
            removeIngredient = removeIngredient,
            onClick = { showIngredientDialog = true },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        BottomButtons(
            steps = steps,
            addStep = addStep,
            saveRecipe = saveRecipe,
            onBack = {},
            onNextStep = nextStep,
            isLast = steps.isEmpty(),
            stepIndex = -1
        )
    }
}