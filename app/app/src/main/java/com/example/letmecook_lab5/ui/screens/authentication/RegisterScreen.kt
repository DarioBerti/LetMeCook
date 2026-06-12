package com.example.letmecook_lab5.ui.screens.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    onBack: () -> Unit
) {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Register Screen",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // fake register success → go back
                    onBack()
                }
            ) {
                Text("Register (fake)")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {
                    onBack()
                }
            ) {
                Text("Back to Login")
            }
        }
    }
}