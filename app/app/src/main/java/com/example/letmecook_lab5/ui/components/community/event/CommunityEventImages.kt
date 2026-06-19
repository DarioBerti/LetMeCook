package com.example.letmecook_lab5.ui.components.community.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CommunityEventImages(
    recipeImage: String?,
    reviewImage: String?,
    onRecipeClick: () -> Unit
) {
    val hasRecipeImage = !recipeImage.isNullOrBlank()
    val hasReviewImage = !reviewImage.isNullOrBlank()

    when {
        hasRecipeImage && hasReviewImage -> {
            TwoImagesLayout(
                recipeImage = recipeImage,
                reviewImage = reviewImage,
                onRecipeClick = onRecipeClick
            )
        }

        hasRecipeImage -> {
            SingleImageLayout(
                image = recipeImage,
                onRecipeClick = onRecipeClick
            )
        }

        hasReviewImage -> {
            SingleImageLayout(
                image = reviewImage,
                onRecipeClick = onRecipeClick
            )
        }

        else -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                GoToRecipeButton(onClick = onRecipeClick)
            }
        }

    }
}

@Composable
private fun SingleImageLayout(
    image: String,
    onRecipeClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        GoToRecipeButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            onClick = onRecipeClick
        )
    }
}

@Composable
private fun TwoImagesLayout(
    recipeImage: String,
    reviewImage: String,
    onRecipeClick: () -> Unit
) {
    Box {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = recipeImage,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            AsyncImage(
                model = reviewImage,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }

        GoToRecipeButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            onClick = onRecipeClick
        )
    }
}

@Composable
private fun GoToRecipeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.RestaurantMenu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = "Go to recipe",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
