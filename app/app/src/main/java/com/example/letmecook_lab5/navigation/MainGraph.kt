package com.example.letmecook_lab5.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.letmecook_lab5.ui.screens.community.CommunityScreen
import com.example.letmecook_lab5.ui.screens.groceries.GroceriesScreen
import com.example.letmecook_lab5.ui.screens.home.HomeScreen
import com.example.letmecook_lab5.ui.screens.notifications.NotificationsScreen
import com.example.letmecook_lab5.ui.screens.profile.ProfileScreen
import com.example.letmecook_lab5.ui.screens.recipeList.RecipeReviewsScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.model.NotificationType
import com.example.letmecook_lab5.ui.screens.groceries.GroceriesPlaceholder
import com.example.letmecook_lab5.ui.screens.profile.CookedRecipesScreen
import com.example.letmecook_lab5.ui.screens.profile.UserListScreen
import com.example.letmecook_lab5.ui.screens.publishedRecipes.OwnedRecipeProposalList
import com.example.letmecook_lab5.ui.screens.recipeList.RecipePhotosScreen
import com.example.letmecook_lab5.ui.screens.search.SearchScreen
import com.example.letmecook_lab5.ui.screens.recipeList.ShowRecipeProposalDetailsRoute
import com.example.letmecook_lab5.viewModel.ReviewViewModel
import com.example.letmecook_lab5.ui.screens.savedRecipes.SavedRecipesRoute as SavedRecipesScreenRoute
import com.example.letmecook_lab5.ui.screens.savedRecipes.CollectionDetailsRoute
import com.example.letmecook_lab5.viewModel.CommunityViewModel
import com.example.letmecook_lab5.viewModel.GroceriesViewModel
import com.example.letmecook_lab5.viewModel.ProfileViewModel
import com.example.letmecook_lab5.viewModel.HomeScreenViewModel
import com.example.letmecook_lab5.viewModel.NotificationViewModel
import com.example.letmecook_lab5.viewModel.OwnedRecipeProposalListViewModel
import com.example.letmecook_lab5.viewModel.RecipeListViewModel
import com.example.letmecook_lab5.viewModel.UserListViewModel

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    navigation<MainGraph>(
        startDestination = HomeRoute
    ) {

        composable<HomeRoute> {
            val viewModel : HomeScreenViewModel = viewModel( factory = HomeScreenViewModel.Factory)

            val topTen by viewModel.topTen.collectAsStateWithLifecycle()
            val newRecipes by viewModel.newRecipes.collectAsStateWithLifecycle()
            val fastRecipes by viewModel.fastRecipes.collectAsStateWithLifecycle()

            val firebaseUser by SessionManagerFacade
                .currentUser
                .collectAsStateWithLifecycle()

            val isLogged = firebaseUser?.isAnonymous == false

            HomeScreen(actions = MainActions(navController),
                topTen = topTen,
                newRecipes = newRecipes,
                fastRecipes = fastRecipes,
                isLogged = isLogged
            )
        }

        composable<SearchRoute> {
            val viewModel : RecipeListViewModel = viewModel( factory = RecipeListViewModel.Factory)

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val recipes by viewModel.filteredRecipes.collectAsStateWithLifecycle()

            val firebaseUser by SessionManagerFacade
                .currentUser
                .collectAsStateWithLifecycle()

            val isLogged = firebaseUser?.isAnonymous == false

            SearchScreen(
                uiState = uiState,
                recipes = recipes,
                onTitleChanged = viewModel::updateInputTitle,
                onIngredientSelected = viewModel::addIngredientFilter,
                onIngredientDeselected = viewModel::removeIngredientFilter,
                onTagSelected = viewModel::addTagFilter,
                onTagDeselected = viewModel::removeTagFilter,
                onMaxCostUpdate = viewModel::updateInputMaxCost,
                onMinCostUpdate = viewModel::updateInputMinCost,
                onRecipeClick = { recipeId -> MainActions(navController).openRecipe(recipeId) },
                isLogged = isLogged
            )
        }


        composable<CommunityRoute> {
            val firebaseUser by SessionManagerFacade.currentUser.collectAsStateWithLifecycle()

            if (firebaseUser == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Login required to access the community")
                }
            } else {
                val viewModel: CommunityViewModel = viewModel(
                    factory = CommunityViewModel.Factory
                )

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CommunityScreen(
                    uiState = uiState,
                    onFollowingClick = viewModel::showFollowingFeed,
                    onPopularClick = viewModel::showPopularFeed,
                    onRecipeClick = { recipeId -> MainActions(navController).openRecipe(recipeId) },
                    onUserClick = { userId -> MainActions(navController).goProfile(userId) }
                )
            }
        }

        composable<GroceriesRoute> {
            val groceriesViewModel: GroceriesViewModel = viewModel(
                factory = GroceriesViewModel.Factory
            )
            val groceriesByRecipes by groceriesViewModel.cartItems.collectAsStateWithLifecycle()
            val uiState by groceriesViewModel.uiState.collectAsStateWithLifecycle()
            val firebaseUser by SessionManagerFacade
                .currentUser
                .collectAsStateWithLifecycle()
            val isLogged = firebaseUser?.isAnonymous == false
            val currentUserId = firebaseUser?.uid

            if (!isLogged){
                GroceriesPlaceholder()
            }
            else if (currentUserId != null) {
                GroceriesScreen(
                    uiState = uiState,
                    groceriesByRecipes,
                    updateRecipeServings = groceriesViewModel::updateRecipeServings,
                    toggleIsChecked = groceriesViewModel::toggleIsChecked,
                    onTabSelected = groceriesViewModel::onTabSelected,
                    onRecipeDelete = groceriesViewModel::deleteRecipe,
                    onRecipeClick = { recipeId -> MainActions(navController).openRecipe(recipeId) }
                )
            }
        }

        composable<NotificationsRoute> {

            val notificationViewModel: NotificationViewModel = viewModel(
                factory = NotificationViewModel.Factory
            )

            NotificationsScreen(
                notificationViewModel = notificationViewModel,
                onClick = { notification ->
                    val relatedId = notification.relatedId
                    Log.d("Notification", notification.toString())
                    Log.d("RelatedIId", relatedId ?: "")
                    when (notification.type) {
                        NotificationType.REVIEW_RECEIVED,
                        NotificationType.RECIPE_DUPLICATED,
                        NotificationType.RECOMMENDATION ->
                            MainActions(navController).openRecipe(relatedId ?: "")
                        NotificationType.FOLLOW_RECEIVED ->
                            MainActions(navController).goProfile(relatedId ?: "")
                        NotificationType.TEST -> Unit
                    }
                }
            )
        }

        composable<RecipeDetailRoute> { backStackEntry ->
            val firebaseUser by SessionManagerFacade
                .currentUser
                .collectAsStateWithLifecycle()

            val isLogged = firebaseUser?.isAnonymous == false
            val currentUserId = firebaseUser?.uid

            val args = backStackEntry.toRoute<RecipeDetailRoute>()

            val reviewViewModel: ReviewViewModel = viewModel(
                factory = ReviewViewModel.Factory
            )


            ShowRecipeProposalDetailsRoute(
                reviewViewModel = reviewViewModel,
                onBack = { MainActions(navController).goBack() } ,
                onEdit = { MainActions(navController).goNewRecipe(editRecipeId = args.recipeId) },
                onCreateNew = { MainActions(navController).goNewRecipe(sourceRecipeId = args.recipeId) },
                onViewAllReviewsClick = { MainActions(navController).goViewAllReviews(args.recipeId) },
                onViewAllPhotosClick = { MainActions(navController).goViewAllPhotos(args.recipeId) },
                isLogged = isLogged,
                currentUserId = currentUserId,
                onAuthorClick = { authorId ->
                    MainActions(navController).goProfile(authorId)
                }
            )
        }

        composable<RecipeReviewsRoute> {
            val reviewViewModel: ReviewViewModel = viewModel(
                factory = ReviewViewModel.Factory
            )
            RecipeReviewsScreen(
                reviewViewModel = reviewViewModel,
            )
        }

        composable<RecipePhotosRoute> {
            val reviewViewModel: ReviewViewModel = viewModel(
                factory = ReviewViewModel.Factory
            )
            RecipePhotosScreen(
                reviewViewModel = reviewViewModel
            )
        }

        composable<SavedRecipesRoute> {
            val firebaseUser by SessionManagerFacade
                .currentUser
                .collectAsStateWithLifecycle()

            val isLogged = firebaseUser?.isAnonymous == false

            SavedRecipesScreenRoute(
                onBack = { MainActions(navController).goBack() } ,
                onCollectionClick = { id ->
                    MainActions(navController).goCollectionDetail(id)
                },
                onRecipeClick = { recipe ->
                    MainActions(navController).openRecipe(recipe.id)
                },
                isLogged = isLogged
            )
        }

        composable<CollectionDetailRoute> {
            val firebaseUser by SessionManagerFacade
                .currentUser
                .collectAsStateWithLifecycle()

            val isLogged = firebaseUser?.isAnonymous == false

            CollectionDetailsRoute(
                onBack = { MainActions(navController).goBack() } ,
                onRecipeClick = { recipe ->
                    MainActions(navController).openRecipe(recipe.id)
                },
                isLogged = isLogged
            )
        }

        composable<ProfileRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ProfileRoute>()

            val viewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModel.Factory(route.userId)
            )

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ProfileScreen(
                uiState = uiState,
                onEditClick = viewModel::startEditing,
                onDoneClick = viewModel::save,
                onCancelEdit = viewModel::cancelEditing,
                onNameChange = viewModel::setName,
                onNicknameChange = viewModel::setNickname,
                onEmailChange = viewModel::setEmail,
                onOpenCamera = viewModel::openCamera,
                onTakePhoto = viewModel::onImageCaptured,
                onRemovePhoto = viewModel::removePhoto,
                onAddDietary = viewModel::addDietaryPreference,
                onRemoveDietary = viewModel::removeDietaryPreference,
                onAddCuisine = viewModel::addTypeOfCuisine,
                onRemoveCuisine = viewModel::removeTypeOfCuisine,
                onAddIngredient = viewModel::addFavoriteIngredient,
                onRemoveIngredient = viewModel::removeFavoriteIngredient,
                onSavedClick = { MainActions(navController).goSaved() },
                onCookedClick = { MainActions(navController).goCooked() },
                onPublishedClick = { MainActions(navController).goPublished() },
                onBack = { MainActions(navController).goBack() } ,
                onLogoutClick = { SessionManagerFacade.signOut()},
                onFollowClick = viewModel::toggleFollow,
                onFollowersClick = { userId -> MainActions(navController).goFollowers(userId) },
                onFollowingClick = { userId -> MainActions(navController).goFollowing(userId) }
            )

            /*val firebaseUser by SessionManagerFacade
                .currentUser
                .collectAsStateWithLifecycle()

            Log.d("AUTH_UID", SessionManagerFacade.currentUser.value?.uid ?: "NULL")

            val userId = firebaseUser?.uid

            if (userId != null) {

                val viewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModel.Factory
                )
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                ProfileScreen(
                    uiState = uiState,
                    onEditClick = viewModel::startEditing,
                    onDoneClick = viewModel::save,
                    onCancelEdit = viewModel::cancelEditing,
                    onNameChange = viewModel::setName,
                    onNicknameChange = viewModel::setNickname,
                    onEmailChange = viewModel::setEmail,
                    onOpenCamera = viewModel::openCamera,
                    onTakePhoto = viewModel::onImageCaptured,
                    onRemovePhoto = viewModel::removePhoto,
                    onAddDietary = viewModel::addDietaryPreference,
                    onRemoveDietary = viewModel::removeDietaryPreference,
                    onAddCuisine = viewModel::addTypeOfCuisine,
                    onRemoveCuisine = viewModel::removeTypeOfCuisine,
                    onAddIngredient = viewModel::addFavoriteIngredient,
                    onRemoveIngredient = viewModel::removeFavoriteIngredient,
                    onSavedClick = { MainActions(navController).goSaved() },
                    onCookedClick = { MainActions(navController).goCooked() },
                    onPublishedClick = { MainActions(navController).goPublished() },
                    onBack = { MainActions(navController).goBack() } ,
                    onLogoutClick = { SessionManagerFacade.signOut()}
                )
            }*/
        }

        composable<PublishedRecipesRoute> {
            val viewModel : OwnedRecipeProposalListViewModel = viewModel(
                factory = OwnedRecipeProposalListViewModel.Factory
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val recipes by viewModel.filteredRecipes.collectAsStateWithLifecycle()

            OwnedRecipeProposalList (
                uiState = uiState,
                recipes = recipes,
                onTitleChanged = { viewModel.updateInputTitle(it) },
                onIngredientSelected = { viewModel.addIngredientFilter(it) },
                onIngredientDeselected = { viewModel.removeIngredientFilter(it) },
                onTagSelected = { viewModel.addTagFilter(it) },
                onTagDeselected = { viewModel.removeTagFilter(it) },
                onMaxCostUpdate = { viewModel.updateInputMaxCost(it) },
                onMinCostUpdate = { viewModel.updateInputMinCost(it) },
                onRecipeClick = { recipeId -> MainActions(navController).openRecipe(recipeId) },
                onNewRecipeClick = { MainActions(navController).goNewRecipe() }
            )
        }


        composable<CookedRecipesRoute> {
            CookedRecipesScreen()
        }

        composable<FollowersRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<FollowersRoute>()

            val viewModel: UserListViewModel = viewModel(
                factory = UserListViewModel.Factory(
                    userId = route.userId,
                    showFollowers = true
                )
            )

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            UserListScreen(
                title = "followers",
                uiState = uiState,
                onSearchChange = viewModel::onSearchChange,
                onUserClick = { userId -> MainActions(navController).goProfile(userId) },
            )
        }

        composable<FollowingRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<FollowingRoute>()

            val viewModel: UserListViewModel = viewModel(
                factory = UserListViewModel.Factory(
                    userId = route.userId,
                    showFollowers = false
                )
            )

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            UserListScreen(
                title = "following",
                uiState = uiState,
                onSearchChange = viewModel::onSearchChange,
                onUserClick = { userId -> MainActions(navController).goProfile(userId) },
            )
        }

    }
}