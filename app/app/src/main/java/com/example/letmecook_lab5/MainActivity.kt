package com.example.letmecook_lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.letmecook_lab5.ui.theme.LetMeCookLab5Theme
import com.example.letmecook_lab5.navigation.AppNavigation
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LetMeCookLab5Theme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}