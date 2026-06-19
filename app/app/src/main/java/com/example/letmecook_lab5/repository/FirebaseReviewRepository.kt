package com.example.letmecook_lab5.repository

import android.util.Log
import com.example.letmecook_lab5.domain.Collections
import com.example.letmecook_lab5.domain.ReviewRepository
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseReviewRepository(
    private val firestore : FirebaseFirestore
) : ReviewRepository {

    private val recipesCollection = firestore.collection(
        Collections.RECIPES)
    private val reviewsCollection = firestore.collectionGroup(Collections.REVIEWS)

    override fun getReviewsByRecipe(recipeId: String): Flow<List<Review>> {
        return recipesCollection.document(recipeId)
            .collection(Collections.REVIEWS)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Review::class.java)
            }
    }

    override fun getReviewsByAuthor(authorId: String): Flow<List<Review>> {
        return reviewsCollection
            .whereEqualTo("authorId", authorId)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Review::class.java)
            }
    }

    override fun getAllReviews(): Flow<List<Review>> {
        return reviewsCollection
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Review::class.java)
            }
    }

    override suspend fun addReview(review: Review) {
        try {
            recipesCollection.document(review.recipeId)
                .collection(Collections.REVIEWS)
                .document(review.id)
                .set(review)
                .await()
        } catch (e: Exception) {
            Log.d("FirebaseReviewRepository", "Errore create: ${e.message}")
        }
    }

    override suspend fun updateReview(updatedReview: Review) {
        try {
            recipesCollection.document(updatedReview.recipeId)
                .collection(Collections.REVIEWS)
                .document(updatedReview.id)
                .set(updatedReview)
                .await()
        } catch (e: Exception) {
            Log.d("FirebaseReviewRepository", "Errore create: ${e.message}")
        }
    }

    override suspend fun deleteReview(reviewId: String) {
        try {
            recipesCollection.document(reviewId)
                .collection(Collections.REVIEWS)
                .document(reviewId)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.d("FirebaseReviewRepository", "Errore create: ${e.message}")
        }
    }


}