package com.example.letmecook_lab5.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.letmecook_lab5.auth.AuthViewModel
import com.example.letmecook_lab5.ui.screens.authentication.LoginScreen
import com.example.letmecook_lab5.ui.screens.authentication.RegisterScreen

fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<AuthGraph>(
        startDestination = LoginRoute
    ) {

        composable<LoginRoute> {

            val context = LocalContext.current
            val viewModel: AuthViewModel = viewModel()
            LoginScreen(
                viewModel = viewModel,
                context = context,
            )
        }

    }
}