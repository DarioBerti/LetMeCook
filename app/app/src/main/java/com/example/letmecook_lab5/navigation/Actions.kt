package com.example.letmecook_lab5.navigation

import androidx.navigation.NavHostController

class MainActions(
    private val navController: NavHostController
) {

    val goHome = { navController.navigate(HomeRoute) }

    val goSearch = {
        navController.navigate(SearchRoute)
    }

    val goNewRecipe = {
        navController.navigate(NewRecipeRoute)
    }

    val goPublishedRecipes = {
        navController.navigate(PublishedRecipesRoute)
    }

    val goCommunity = {
        navController.navigate(CommunityRoute)
    }

    val goGroceries = {
        navController.navigate(GroceriesRoute)
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