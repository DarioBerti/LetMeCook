package com.example.letmecook_lab5.ui.screens.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.letmecook_lab5.auth.GoogleAuthHelper
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.model.TopBarConfig
import com.example.letmecook_lab5.navigation.AppBottomBar
import com.example.letmecook_lab5.navigation.AuthGraph
import com.example.letmecook_lab5.navigation.CommunityRoute
import com.example.letmecook_lab5.navigation.CookedRecipesRoute
import com.example.letmecook_lab5.navigation.GroceriesRoute
import com.example.letmecook_lab5.navigation.HomeRoute
import com.example.letmecook_lab5.navigation.LoginRoute
import com.example.letmecook_lab5.navigation.MainGraph
import com.example.letmecook_lab5.navigation.NewRecipeRecapRoute
import com.example.letmecook_lab5.navigation.NewRecipeRoute
import com.example.letmecook_lab5.navigation.NotificationsRoute
import com.example.letmecook_lab5.navigation.ProfileRoute
import com.example.letmecook_lab5.navigation.PublishedRecipesRoute
import com.example.letmecook_lab5.navigation.RecipeDetailRoute
import com.example.letmecook_lab5.navigation.RecipeListRoute
import com.example.letmecook_lab5.navigation.RecipePhotosRoute
import com.example.letmecook_lab5.navigation.RecipeReviewsRoute
import com.example.letmecook_lab5.navigation.SearchRoute
import com.example.letmecook_lab5.ui.components.notification.NotificationSnackbar
import com.example.letmecook_lab5.viewModel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val showBottomBar =
        currentDestination?.hierarchy?.any {
            it.hasRoute(MainGraph::class)
        } == true

    val firebaseUser by SessionManagerFacade.currentUser.collectAsStateWithLifecycle()
    val isLogged = firebaseUser?.isAnonymous == false

    val notificationViewModel : NotificationViewModel = viewModel(
        factory = NotificationViewModel.Factory
    )

    val notifications by notificationViewModel.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    val snackbarHostState = remember { SnackbarHostState() }

    var initialized by remember { mutableStateOf(false) }
    var previousUnreadCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(notifications) {

        val unreadCount = notifications.count { !it.isRead }

        if (!initialized) {
            previousUnreadCount = unreadCount
            initialized = true
            return@LaunchedEffect
        }

        if (unreadCount > previousUnreadCount) {
            snackbarHostState.showSnackbar("Nuova notifica ricevuta")
        }

        previousUnreadCount = unreadCount
    }

    val topBarConfig = when {

        currentDestination?.hasRoute(HomeRoute::class) == true ||
                currentDestination?.hasRoute(SearchRoute::class) == true ||
                currentDestination?.hasRoute(CommunityRoute::class) == true ||
                currentDestination?.hasRoute(GroceriesRoute::class) == true ->

            TopBarConfig(
                title = "LetMeCook",
                showBack = false,
                showNotifications = true,
                showProfile = true,
                unreadNotifications = unreadCount
            )

        currentDestination?.hasRoute(NotificationsRoute::class) == true ->
            TopBarConfig(
                title = "Notifications",
                showBack = true
            )

        currentDestination?.hasRoute(ProfileRoute::class) == true ->
            TopBarConfig(
                title = "Profile",
                showBack = true
            )

        currentDestination?.hasRoute(NewRecipeRoute::class) == true ->
            TopBarConfig(
                title = "New Recipe",
                showBack = true
            )

        currentDestination?.hasRoute(NewRecipeRecapRoute::class) == true ->
            TopBarConfig(
                title = "Recipe Details",
                showBack = true
            )


        currentDestination?.hasRoute(RecipeListRoute::class) == true ->
            TopBarConfig(
                title = "Recipe List",
                showBack = true
            )

        currentDestination?.hasRoute(RecipeDetailRoute::class) == true ->
            TopBarConfig(
                title = "Recipe Details",
                showBack = true,
                showOptions = RecipeOptions.isOwner
            )

        currentDestination?.hasRoute(RecipeReviewsRoute::class) == true ->
            TopBarConfig(
                title = "Recipe Reviews",
                showBack = true
            )

        currentDestination?.hasRoute(RecipePhotosRoute::class) == true ->
            TopBarConfig(
                title = "Recipe Photos",
                showBack = true
            )
        currentDestination?.hasRoute(PublishedRecipesRoute::class) == true ->
            TopBarConfig(title = "Published Recipes", showBack = true)

        currentDestination?.hasRoute(CookedRecipesRoute::class) == true ->
            TopBarConfig(title = "Cooked Recipes", showBack = true)

        else -> null
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { data ->
                NotificationSnackbar(
                    data = data,
                    onClick = { navController.navigate(NotificationsRoute) }
                )
            }
        },
        topBar = {
            val conf = topBarConfig
            if (conf != null) {
                TopAppBar(
                    title = { Text(conf.title,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ) },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor =MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        actionIconContentColor = MaterialTheme.colorScheme.primary,
                        subtitleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    navigationIcon = {
                        if (conf.showBack) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.Default.ArrowBack, null)
                            }
                        }
                    },
                    actions = {
                        if (conf.showNotifications && isLogged) {
                            Box {
                                IconButton(onClick = {
                                    navController.navigate(NotificationsRoute)
                                }) {
                                    Icon(Icons.Default.Notifications, contentDescription = null)
                                }

                                if (unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .offset(x = (-6).dp, y = (-6).dp)
                                            .background(
                                                color = Color.Red,
                                                shape = CircleShape
                                            )
                                            .size(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (unreadCount > 99) "99" else unreadCount.toString(),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                        if (conf.showProfile && isLogged) {
                            IconButton(onClick = {
                                navController.navigate(ProfileRoute)
                            }) {
                                Icon(Icons.Default.Person, null)
                            }
                        }
                        if (!isLogged) {
                            TextButton(onClick = {
                                navController.navigate(AuthGraph)
                            }) {
                                Text("Login")
                            }
                        }
                        if (conf.showOptions) {
                            IconButton(onClick = { RecipeOptions.openMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More options")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(navController)
            }
        }
    ) { padding ->

        content(padding)
    }
}