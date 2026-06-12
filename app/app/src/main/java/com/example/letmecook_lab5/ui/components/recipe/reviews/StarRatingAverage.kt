package com.example.letmecook_lab5.ui.components.recipe.reviews

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingAverage(
    averageRating: Double
) {
    val fullStars = averageRating.toInt()
    val decimal = averageRating - fullStars

    val hasHalfStar =
        decimal in 0.25..<0.75

    val extraFullStar =
        decimal >= 0.75

    Row {
        repeat(5) { index ->
            val icon = when {
                index < fullStars -> {
                    Icons.Filled.Star
                }

                index == fullStars && hasHalfStar -> {
                    Icons.AutoMirrored.Filled.StarHalf
                }

                index == fullStars && extraFullStar -> {
                    Icons.Filled.Star
                }

                else -> {
                    Icons.Outlined.StarOutline
                }

            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}