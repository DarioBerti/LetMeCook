package com.example.letmecook_lab5.ui.components.newRecipe.stepEditor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StepDescriptionBox(
    stepDescription: String,
    stepDescriptionChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = stepDescription,
            onValueChange = stepDescriptionChanged,
            placeholder = {
                Text(
                    text = "Enter step description",
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
    }
}