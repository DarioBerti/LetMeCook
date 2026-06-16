package com.example.letmecook_lab5.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.letmecook_lab5.repository.FirebaseUserRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

// global singleton -> only one session, accessible everywhere
object SessionManagerFacade : AuthRepository {

    private val repository: AuthRepository by lazy {

        FirebaseAuthRepository(
            firebaseAuth = FirebaseAuth.getInstance(),
            userRepository = FirebaseUserRepository(
                firestore = FirebaseFirestore.getInstance()
            )
        )
    }

    override val currentUser: StateFlow<FirebaseUser?>
        get() = repository.currentUser

    override suspend fun signInWithGoogle(
        idToken: String
    ): Result<Unit> {
        return repository.signInWithGoogle(idToken)
    }

    override suspend fun signInAnonymously(): Result<Unit> {
        return repository.signInAnonymously()
    }

    override fun signOut() {
        repository.signOut()
    }
}