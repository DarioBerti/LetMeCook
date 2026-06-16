package com.example.letmecook_lab5.repository

import android.content.Context
import com.example.letmecook_lab5.auth.AuthRepository
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.domain.IngredientRepository
//import com.example.letmecook_lab5.auth.AuthRepository
//import com.example.letmecook_lab5.auth.FirebaseAuthRepository
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.domain.ReviewRepository
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.domain.NotificationRepository
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer{
    val recipeRepository: RecipeRepository
    val userRepository: UserRepository
    val reviewRepository: ReviewRepository
    val ingredientRepository: IngredientRepository
    val notificationRepository : NotificationRepository
    val authRepository: AuthRepository
    val storageRepository: FirebaseStorageRepository
    val groceriesRepository : FirebaseGroceriesRepository
}

class DefaultAppContainer(private val context: Context): AppContainer{

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override val recipeRepository: RecipeRepository by lazy {
        FirebaseRecipeRepository(firestore)
    }

    override val userRepository: UserRepository by lazy {
        FirebaseUserRepository(firestore)
    }

    override val reviewRepository: ReviewRepository by lazy {
        FirebaseReviewRepository(firestore)
    }

    override val ingredientRepository: IngredientRepository by lazy {
        FirebaseIngredientRepository(firestore)
    }

    override val notificationRepository : NotificationRepository by lazy {
        FirebaseNotificationRepository(firestore)
    }

    override val authRepository: AuthRepository by lazy {
        SessionManagerFacade
    }

    override val storageRepository: FirebaseStorageRepository by lazy {
        FirebaseStorageRepository()
    }

    override val groceriesRepository: FirebaseGroceriesRepository by lazy{
        FirebaseGroceriesRepository(firestore)
    }

}