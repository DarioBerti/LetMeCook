package com.example.letmecook_lab5.navigation

import androidx.navigation.NavHostController

class MainActions(
    private val navController: NavHostController
) {

    val goHome = {
        navController.navigate(HomeRoute) {
            launchSingleTop = true
            popUpTo(navController.graph.startDestinationId)
        }
    }

    val goSearch = {
        navController.navigate(SearchRoute) {
            launchSingleTop = true
        }
    }

    val goNewRecipe = {
        navController.navigate(NewRecipeRoute)
    }

    val goPublishedRecipes = {
        navController.navigate(PublishedRecipesRoute)
    }

    val goCommunity = {
        navController.navigate(CommunityRoute) {
            launchSingleTop = true
        }
    }

    val goGroceries = {
        navController.navigate(GroceriesRoute) {
            launchSingleTop = true
        }
    }

    val goFavorites = { navController.navigate(FavoritesRoute) }

    val goProfile = { navController.navigate(ProfileRoute) }

    val goReviewHistory = { navController.navigate(ReviewHistoryRoute) }

    val openRecipe: (String) -> Unit = { id ->
        navController.navigate(RecipeDetailRoute(id))
    }

    val back = { navController.popBackStack() }

    val goViewAllReviews: (String) -> Unit = { recipeId ->
        navController.navigate(RecipeReviewsRoute(recipeId))
    }

    val goViewAllPhotos: (String) -> Unit = { recipeId ->
        navController.navigate(RecipePhotosRoute(recipeId))
    }

}