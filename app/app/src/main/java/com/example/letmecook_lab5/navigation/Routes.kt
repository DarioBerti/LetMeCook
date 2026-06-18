package com.example.letmecook_lab5.navigation

import kotlinx.serialization.Serializable

@Serializable object NewRecipeRecapRoute
@Serializable object HomeRoute
@Serializable object SearchRoute
@Serializable data class NewRecipeRoute(
    val sourceRecipeId: String? = null,
    val editRecipeId: String? = null
)@Serializable object CommunityRoute
@Serializable object GroceriesRoute
@Serializable object LoginRoute
@Serializable object RegisterRoute
@Serializable object FavoritesRoute
@Serializable data class ProfileRoute (val userId: String)
@Serializable object NotificationsRoute
@Serializable object ReviewHistoryRoute

@Serializable object AuthGraph
@Serializable object MainGraph
@Serializable object NewRecipeGraph
@Serializable data class RecipeDetailRoute(val recipeId: String)

@Serializable object RecipeListRoute
@Serializable object SavedRecipesRoute
@Serializable data class CollectionDetailRoute(val collectionId: String)

@Serializable data class RecipeReviewsRoute(val recipeId: String)

@Serializable data class RecipePhotosRoute(val recipeId: String)
@Serializable
object PublishedRecipesRoute

@Serializable
object CookedRecipesRoute

@Serializable data class FollowersRoute(val userId: String)

@Serializable data class FollowingRoute(val userId: String)