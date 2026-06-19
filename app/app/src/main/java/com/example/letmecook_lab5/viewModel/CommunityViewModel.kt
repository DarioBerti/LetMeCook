package com.example.letmecook_lab5.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.domain.ReviewRepository
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.model.CommunityEvent
import com.example.letmecook_lab5.model.CommunityEventType
import com.example.letmecook_lab5.model.User
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommunityViewModel(
    private val recipeRepository: RecipeRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val currentUserId: String
) : ViewModel()  {
    data class CommunityUiState(
        val events: List<CommunityEvent> = emptyList(),
        val showFollowing: Boolean = true,
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState = _uiState.asStateFlow()

    private val _showFollowing = MutableStateFlow(true)

    init {
        loadFeed()
    }

    fun showFollowingFeed() {
        _showFollowing.value = true
    }

    fun showPopularFeed() {
        _showFollowing.value = false
    }

    private fun loadFeed() {
        viewModelScope.launch {
            combine(
                userRepository.getUserById(currentUserId),
                userRepository.getAllUsers(),
                recipeRepository.getAllRecipes(),
                reviewRepository.getAllReviews(),
                _showFollowing
            ) { currentUser, users, recipes, reviews, showFollowing ->
                if (currentUser == null) {
                    emptyList()
                } else {
                    buildEvents(
                        currentUser = currentUser,
                        users = users.filterNotNull(),
                        recipes = recipes,
                        reviews = reviews,
                        followingOnly = showFollowing
                    )
                }
            }.collect { events ->
                _uiState.update {
                    it.copy(
                        events = events,
                        isLoading = false,
                        showFollowing = _showFollowing.value
                    )
                }
            }
        }
    }

    private fun buildEvents(
        currentUser: User,
        users: List<User>,
        recipes: List<Recipe>,
        reviews: List<Review>,
        followingOnly: Boolean
    ): List<CommunityEvent> {
        val userMap = users.associateBy { it.id }
        val events = mutableListOf<CommunityEvent>()

        recipes.forEach { recipe ->
            if (followingOnly && recipe.ownerId !in currentUser.followingIds) return@forEach

            val author = userMap[recipe.ownerId] ?: return@forEach

            events += CommunityEvent(
                id = "recipe_${recipe.id}",
                type = CommunityEventType.NEW_RECIPE,
                userId = author.id,
                userName = author.fullName,
                userNickname = author.nickname,
                userImage = author.profileImageUri,
                recipeId = recipe.id,
                recipeTitle = recipe.title,
                text = "",
                recipeImage = recipe.imageUrl,
                reviewImage = "",
                timestamp = recipe.createdAt
            )
        }

        reviews.forEach { review ->
            if (followingOnly && review.authorId !in currentUser.followingIds) return@forEach

            val recipe = recipes.find {
                it.id == review.recipeId
            } ?: return@forEach

            val author = userMap[review.authorId]

            events += CommunityEvent(
                id = "review_${review.id}",
                type = CommunityEventType.REVIEW,
                userId = author?.id ?: review.authorId,
                userName = author?.fullName ?: review.authorFullName,
                userNickname = author?.nickname ?: review.authorNickname,
                userImage = author?.profileImageUri,
                recipeId = recipe.id,
                recipeTitle = recipe.title,
                recipeImage = recipe.imageUrl,
                reviewImage = review.imageUrl,
                text = review.comment,
                timestamp = review.createdAt
            )
        }

        return events.sortedByDescending {
            it.timestamp
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val recipeRepository = application.container.recipeRepository
                val reviewRepository = application.container.reviewRepository
                val userRepository = application.container.userRepository
                val currentUserId = SessionManagerFacade.currentUser.value?.uid ?: error("No logged-in user")

                CommunityViewModel(
                    recipeRepository = recipeRepository,
                    reviewRepository = reviewRepository,
                    userRepository = userRepository,
                    currentUserId = currentUserId
                )
            }
        }
    }
}