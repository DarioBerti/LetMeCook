package com.example.letmecook_lab5.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.letmecook_lab5.model.Review
import com.example.letmecook_lab5.domain.ReviewRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.domain.NotificationRepository
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.model.Notification
import com.example.letmecook_lab5.model.NotificationType
import com.example.letmecook_lab5.navigation.RecipeReviewsRoute
import com.example.letmecook_lab5.session.SessionManager
import kotlinx.coroutines.flow.first
import com.example.letmecook_lab5.repository.FirebaseStorageRepository
import java.util.UUID

class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val recipeRepository: RecipeRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val storageRepo: FirebaseStorageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val reviewsRoute = savedStateHandle.toRoute<RecipeReviewsRoute>()
    private val recipeId = reviewsRoute.recipeId
    private val userId = SessionManager.CURRENT_LOGGED_IN_USER_ID


    val recipeReviews =
        reviewRepository.getReviewsByRecipe(recipeId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val userReviews =
        reviewRepository.getReviewsByAuthor(userId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    val averageRating =
        recipeReviews.map { reviews ->
            if (reviews.isEmpty()) 0.0
            else reviews.map { it.rating }.average()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )

    fun addReview(review: Review) {
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeById(review.recipeId)
            val user = userRepository.getUserById(review.authorId).first()

            val uploadedUrl = storageRepo.uploadIfLocal(
                review.imageUrl,
                "review_images/${UUID.randomUUID()}.jpg"
            ) ?: ""

            val reviewWithAuthor = review.copy(
                authorFullName = user?.fullName ?: "",
                authorNickname = user?.nickname ?: "",
                imageUrl = uploadedUrl
            )

            reviewRepository.addReview(reviewWithAuthor)


            notificationRepository.sendNotification(
                Notification(
                    userId = recipe.ownerId,
                    title = "New review received",
                    message = "${user?.fullName} cooked your recipe ${recipe.title} and shared a review",
                    type = NotificationType.REVIEW_RECEIVED,
                    relatedId = recipe.id
                )
            )

            syncAverageRating()
        }
    }

    fun updateReview(review: Review) {
        viewModelScope.launch {
            val uploadedUrl = storageRepo.uploadIfLocal(
                review.imageUrl,
                "review_images/${UUID.randomUUID()}.jpg"
            ) ?: ""
            reviewRepository.updateReview(review.copy(imageUrl = uploadedUrl))
            syncAverageRating()
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)

            syncAverageRating()
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory{
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val reviewRepository = application.container.reviewRepository
                val recipeRepository = application.container.recipeRepository
                val notificationRepository = application.container.notificationRepository
                val userRepository = application.container.userRepository
                val storageRepository = application.container.storageRepository
                val savedStateHandle = createSavedStateHandle()
                ReviewViewModel(reviewRepository, recipeRepository, notificationRepository, userRepository, storageRepository, savedStateHandle)            }
        }
    }

    private suspend fun syncAverageRating() {
        val reviews = reviewRepository.getReviewsByRecipe(recipeId).first()
        val newAvg = if (reviews.isEmpty()) 0.0 else reviews.map { it.rating }.average()
        val recipe = recipeRepository.getRecipeById(recipeId)
        recipeRepository.updateRecipe(recipe.copy(averageRating = newAvg))
    }
}