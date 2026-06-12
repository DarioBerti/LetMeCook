package com.example.letmecook_lab5.ui.components.newRecipe.stepEditor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StepTitleBox(
    stepTitle: String,
    stepTitleChanged: (String) -> Unit,
    stepIndex: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = stepTitle,
            onValueChange = stepTitleChanged,
            placeholder = {
                Text(
                    text = "Enter step title",
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            ),
            prefix = {
                Text(
                    text = "Step ${stepIndex + 1}:",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        )
    }
}