package com.example.letmecook_lab5.domain

import com.example.letmecook_lab5.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviewsByRecipe(recipeId: String): Flow<List<Review>>
    fun getReviewsByAuthor(authorId: String): Flow<List<Review>>
    suspend fun addReview(review: Review)
    suspend fun updateReview(updatedReview: Review)
    suspend fun deleteReview(reviewId: String)
}