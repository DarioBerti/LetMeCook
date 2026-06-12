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

/*
object SessionManagerFacade: AuthRepository {

    override val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid ?:
            throw IllegalStateException("User not logged in")

    val _currentUserMutableStateFlow: MutableStateFlow<String?> = MutableStateFlow(FirebaseAuth.getInstance().currentUser?.uid)
    override val currentUserStateFlow: StateFlow<String?> = _currentUserMutableStateFlow.asStateFlow()

    init {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            _currentUserMutableStateFlow.value = auth.currentUser?.uid
        }
    }

    override val isLoggedIn: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    override suspend fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override suspend fun signInAnonymouse(): Result<Unit> {
        return try {
            FirebaseAuth.getInstance().signInAnonymously().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(context: Context): Result<Unit> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("REPLACE_ME_STRING") //write api key heere
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = CredentialManager.create(context).getCredential(context,request)
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken,null)

            FirebaseAuth.getInstance().signInWithCredential(firebaseCredential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
 */

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