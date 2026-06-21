package com.example.letmecook_lab5.ui.screens.authentication

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.letmecook_lab5.auth.AuthViewModel
import com.example.letmecook_lab5.auth.GoogleAuthHelper

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    context: Context
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val credentialManager = remember { CredentialManager.create(context) }
    val serverClientId = "974389645416-qgt008fm8fr1abnj3absl53f9r4ljrp3.apps.googleusercontent.com"

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "LetMeCook",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    val request = GoogleAuthHelper.buildRequest(serverClientId)
                    viewModel.startGoogleSignIn(
                        credentialManager = credentialManager,
                        request = request,
                        context = context
                    )
                }
            ) {
                Text("Sign in with Google")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    Log.d("AUTH_DEBUG", "CLICK guest button")
                    viewModel.signInAnonymously()
                }
            ) {
                Text("Continue as guest")
            }

            Spacer(Modifier.height(12.dp))

            if (state.isLoading) {
                Spacer(Modifier.height(16.dp))
                Text("Loading...")
            }

            state.error?.let {
                Spacer(Modifier.height(8.dp))
                Text("Error: $it")
            }
        }
    }
}