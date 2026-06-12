package com.example.letmecook_lab5.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(
    navController: NavHostController
) {

    // used to make BottomBar dynamic:
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    NavigationBar (
        modifier = Modifier.clip(RoundedCornerShape( topStart = 24.dp, topEnd = 24.dp ))
    ) {

        NavigationBarItem(
            selected = currentDestination?.hasRoute(HomeRoute::class) == true,
            onClick = {
                navController.navigate(HomeRoute) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId)
                }
            },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home", style = MaterialTheme.typography.bodySmall)  },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                indicatorColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationBarItem(
            selected = currentDestination?.hasRoute(SearchRoute::class) == true,
            onClick = {
                navController.navigate(SearchRoute) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search", style = MaterialTheme.typography.bodySmall)  },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                indicatorColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationBarItem(
            selected = currentDestination?.hasRoute(CommunityRoute::class) == true,
            onClick = {
                navController.navigate(CommunityRoute) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Groups, null) },
            label = { Text("Community", style = MaterialTheme.typography.bodySmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                indicatorColor = MaterialTheme.colorScheme.primary
            )
        )

        NavigationBarItem(
            selected = currentDestination?.hasRoute(GroceriesRoute::class) == true,
            onClick = {
                navController.navigate(GroceriesRoute) {
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.ShoppingCart, null) },
            label = { Text("Groceries", style = MaterialTheme.typography.bodySmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                indicatorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}