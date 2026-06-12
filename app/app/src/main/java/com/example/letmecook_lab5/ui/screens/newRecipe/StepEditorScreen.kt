package com.example.letmecook_lab5.ui.screens.newRecipe

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.Step
import com.example.letmecook_lab5.ui.components.newRecipe.recipeForm.BottomButtons
import com.example.letmecook_lab5.ui.components.newRecipe.stepEditor.DeleteInsertBox
import com.example.letmecook_lab5.ui.components.newRecipe.stepEditor.StepDescriptionBox
import com.example.letmecook_lab5.ui.components.newRecipe.stepEditor.StepImageBox
import com.example.letmecook_lab5.ui.components.newRecipe.stepEditor.StepTitleBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepEditorScreen(
    step: Step,
    steps: List<Step>,
    stepIndex: Int,

    stepTitleChanged: (String) -> Unit,
    stepDescriptionChanged: (String) -> Unit,
    stepImageChanged: (String) -> Unit,

    onAddStep: () -> Unit,
    onNextStep: () -> Unit,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,

    errorMessage: String?,
    onClearError: () -> Unit
) {

    val isLast = stepIndex == steps.lastIndex

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            stepImageChanged(it.toString())
        }
    }

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

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        DeleteInsertBox(
            onDelete = onDelete,
            onAdd = onAddStep,
            isLast = isLast)
        Spacer(modifier = Modifier.height(8.dp))
        StepTitleBox(
            stepTitle = step.title,
            stepTitleChanged = stepTitleChanged,
            stepIndex = stepIndex
        )
        Spacer(Modifier.height(12.dp))
        StepImageBox(
            stepImageUrl = step.photo,
            onPickImage = { galleryLauncher.launch("image/*") },
            onRemoveImage = { stepImageChanged("") }
        )
        Spacer(Modifier.height(12.dp))
        StepDescriptionBox(
            stepDescription = step.description,
            stepDescriptionChanged = stepDescriptionChanged
        )
        Spacer(Modifier.height(12.dp))
        BottomButtons(
            steps = steps,
            addStep = onAddStep,
            saveRecipe = onSave,
            onBack = onBack,
            onNextStep = onNextStep,
            isLast = isLast,
            stepIndex = stepIndex
        )
    }
}