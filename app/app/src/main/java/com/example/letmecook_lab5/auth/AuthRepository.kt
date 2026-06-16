package com.example.letmecook_lab5.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.auth.FirebaseUser


interface AuthRepository {

    // current state of the authenticated user (null -> not authenticated)
    val currentUser: StateFlow<FirebaseUser?>

    // login w/ Google token
    suspend fun signInWithGoogle(idToken: String): Result<Unit>

    // anonymous login (guest)
    suspend fun signInAnonymously(): Result<Unit>

    // logout
    fun signOut()
}