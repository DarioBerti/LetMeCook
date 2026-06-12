package com.example.letmecook_lab5.auth

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.auth.FirebaseUser

/*

interface AuthRepository {
    val currentUserId: String?
    val currentUserStateFlow: StateFlow<String?>

    val isLoggedIn: Boolean

    suspend fun logOut()
    suspend fun signInAnonymouse(): Result<Unit>
    suspend fun signIn(context: Context): Result<Unit>
}

class FirebaseAuthRepository (
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager
): AuthRepository {

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

    //TODO()
    override suspend fun logOut() {
        //TODO("Not yet implemented")
    }

    override suspend fun signInAnonymouse(): Result<Unit> {
        //TODO("Not yet implemented")
        return Result.success(Unit)
    }

    override suspend fun signIn(context: Context): Result<Unit> {
        //TODO("Not yet implemented")
        return Result.success(Unit)
    }

    override val isLoggedIn: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null
}
*/





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