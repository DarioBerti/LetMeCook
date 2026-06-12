package com.example.letmecook_lab5.ui.components.recipe.reviews

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRating(rating: Int) {
    Row {
        repeat(5) { index ->
            val icon = when {
                index < rating -> Icons.Filled.Star
                else -> Icons.Outlined.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(12.dp)
            )
        }
    }
}