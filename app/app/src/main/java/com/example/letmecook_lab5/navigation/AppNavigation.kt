package com.example.letmecook_lab5.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.ui.screens.utils.MainScaffold

@Composable
fun AppNavigation(navController: NavHostController) {

    val user by SessionManagerFacade.currentUser.collectAsStateWithLifecycle()

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(AuthGraph) {
                popUpTo(0)
            }
        } else {
            navController.navigate(MainGraph) {
                popUpTo(AuthGraph) { inclusive = true }
            }
        }
    }

    MainScaffold(navController) { padding ->

        NavHost(
            navController = navController,
            startDestination = AuthGraph,
            modifier = Modifier.padding(padding)
        ) {
            authGraph(navController)
            mainGraph(navController)
            newRecipeGraph(navController)
        }
    }
}