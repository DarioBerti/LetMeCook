package com.example.letmecook_lab5.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun handleGoogleSignIn(idToken: String) {
        signInWithGoogle(idToken)
    }

    fun signInAnonymously() {
        viewModelScope.launch {

            _uiState.value = AuthUiState(isLoading = true)

            val result = SessionManagerFacade.signInAnonymously()

            _uiState.value = if (result.isSuccess) {
                AuthUiState()
            } else {
                AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {

            _uiState.value = AuthUiState(isLoading = true)

            val result = SessionManagerFacade.signInWithGoogle(idToken)

            Log.d(
                "AUTH",
                FirebaseAuth.getInstance().currentUser?.uid ?: "NULL"
            )

            _uiState.value = if (result.isSuccess) {
                AuthUiState()
            } else {
                AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun startGoogleSignIn(
        credentialManager: CredentialManager,
        request: GetCredentialRequest,
        context: Context
    ) {
        viewModelScope.launch {
            try {

                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val credential = result.credential
                val idToken = GoogleAuthHelper.extractIdToken(credential)

                handleGoogleSignIn(idToken)

            } catch (e: Exception) {
                clearError()
            }
        }
    }
}