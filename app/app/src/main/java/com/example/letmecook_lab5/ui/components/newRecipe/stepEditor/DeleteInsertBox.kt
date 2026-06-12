package com.example.letmecook_lab5.ui.components.newRecipe.stepEditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DeleteInsertBox(
    onDelete: () -> Unit,
    onAdd: () -> Unit,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement =
            if (isLast)
                Arrangement.End
            else
                Arrangement.SpaceBetween
    ) {
        if (!isLast) {
            Row (
                modifier = Modifier.clickable { onAdd() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Insert Step",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "INSERT STEP",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Row (
            modifier = Modifier.clickable { onDelete() }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Step",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "DELETE STEP",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
