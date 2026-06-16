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

    val goNotifications = { navController.navigate(NotificationsRoute) }

    fun goNewRecipe(
        sourceRecipeId: String? = null,
        editRecipeId: String? = null
    ) {
        navController.navigate(
            NewRecipeRoute(
                sourceRecipeId = sourceRecipeId,
                editRecipeId = editRecipeId
            )
        )
    }

    val goNewRecipeRecap = { navController.navigate(NewRecipeRecapRoute) }

    val goCollectionDetail: (String) -> Unit = { id ->
        navController.navigate(CollectionDetailRoute(id))
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

    val goProfile = { navController.navigate(ProfileRoute) }

    val openRecipe: (String) -> Unit = { id ->
        navController.navigate(RecipeDetailRoute(id))
    }

    val goBack = { navController.popBackStack() }

    val goViewAllReviews: (String) -> Unit = { recipeId ->
        navController.navigate(RecipeReviewsRoute(recipeId))
    }

    val goViewAllPhotos: (String) -> Unit = { recipeId ->
        navController.navigate(RecipePhotosRoute(recipeId))
    }
    val goSaved =  { navController.navigate(SavedRecipesRoute) }
    val goCooked = { navController.navigate(CookedRecipesRoute) }
    val goPublished = { navController.navigate(PublishedRecipesRoute) }

}