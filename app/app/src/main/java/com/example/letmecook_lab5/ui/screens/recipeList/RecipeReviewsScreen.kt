package com.example.letmecook_lab5.ui.screens.recipeList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.ui.components.recipe.reviews.ReviewItem
import com.example.letmecook_lab5.ui.components.recipe.reviews.StarRatingAverage
import com.example.letmecook_lab5.viewModel.ReviewViewModel

@Composable
fun RecipeReviewsScreen(
    reviewViewModel: ReviewViewModel,
) {
    val reviews by reviewViewModel.recipeReviews.collectAsState()
    val averageRating by reviewViewModel.averageRating.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = Color(0xFFFFF1F2),
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = String.format("%.1f", averageRating),
                            fontSize = 45.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = " / 5",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "${reviews.size} REVIEWS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                StarRatingAverage(averageRating)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(reviews) { review ->
                ReviewItem(
                    review = review,
                    onDelete = reviewViewModel::deleteReview,
                    onUpdate = reviewViewModel::updateReview
                )
            }
        }
    }
}