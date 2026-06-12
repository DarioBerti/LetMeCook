package com.example.letmecook_lab5.auth

import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.repository.FirebaseUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) : AuthRepository {

    private val repositoryScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private suspend fun syncUser(firebaseUser: FirebaseUser) {
        if (userRepository is FirebaseUserRepository) {
            userRepository.ensureUserProfile(firebaseUser)
        }
    }

    // global state of the authenticated user
    override val currentUser: StateFlow<FirebaseUser?> =
        callbackFlow {

            val listener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser)
            }

            firebaseAuth.addAuthStateListener(listener)

            awaitClose {
                firebaseAuth.removeAuthStateListener(listener)
            }
        }.stateIn(
            scope = repositoryScope,
            started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
            initialValue = firebaseAuth.currentUser
        )

    // receives Google token, converts it to Firebase credential, then signs in
    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {

            val credential = GoogleAuthProvider.getCredential(idToken, null)

            val result = firebaseAuth.signInWithCredential(credential).await()

            result.user?.let { firebaseUser ->
                syncUser(firebaseUser)
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInAnonymously(): Result<Unit> {
        return try {

            firebaseAuth.signOut()

            val result = firebaseAuth.signInAnonymously().await()

            result.user?.let { firebaseUser ->
                syncUser(firebaseUser)
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}