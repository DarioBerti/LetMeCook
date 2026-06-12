package com.example.letmecook_lab5.ui.components.newRecipe.recipeForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.Difficulty

@Composable
fun DifficultyInputBox(
    difficulty: Difficulty,
    difficultyChanged: (Difficulty) -> Unit,
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
            text = "DIFFICULTY",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(32.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Difficulty.values().forEach { d ->

                val isSelected = difficulty == d

                Button(
                    onClick = { difficultyChanged(d) },
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (isSelected)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Text(
                        text = d.name,
                        style =  MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}