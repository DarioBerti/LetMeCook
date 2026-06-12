package com.example.letmecook_lab5.ui.components.newRecipe.recipeForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PrepTimeInputBox(
    cookingTime: Int,
    cookingTimeChanged: (Int) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(32.dp)
            )
            .defaultMinSize(minHeight = 80.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "PREP. TIME",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = cookingTime.toString(),
            onValueChange = { text ->       // conversion bcs cookingTime is Int
                val value = text.toIntOrNull() ?: 0
                cookingTimeChanged(value)
            },
            suffix = { Text(if (cookingTime == 1) "minute" else "minutes") },
            placeholder = {
                Text(
                    text = "0 minutes",
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            singleLine = true,
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