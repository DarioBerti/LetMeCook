package com.example.letmecook_lab5.ui.screens.recipeList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.letmecook_lab5.model.Review
import com.example.letmecook_lab5.ui.components.recipe.photos.CommunityPhotoDialog
import com.example.letmecook_lab5.viewModel.ReviewViewModel

@Composable
fun RecipePhotosScreen(
    reviewViewModel: ReviewViewModel
) {
    val reviews by reviewViewModel.recipeReviews.collectAsState()
    val photoReviews = reviews.filter { it.imageUrl.isNotBlank() }

    var selectedPhotoReview by remember { mutableStateOf<Review?>(null) }

    if (selectedPhotoReview != null) {
        Dialog(
            onDismissRequest = { selectedPhotoReview = null }
        ) {
            CommunityPhotoDialog(
                review = selectedPhotoReview!!,
                onDismiss = { selectedPhotoReview = null }
            )
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        items(photoReviews) { review ->
            AsyncImage(
                model = review.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { selectedPhotoReview = review },
                contentScale = ContentScale.Crop
            )
        }
    }
}