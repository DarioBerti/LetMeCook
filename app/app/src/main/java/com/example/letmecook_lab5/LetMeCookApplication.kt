package com.example.letmecook_lab5

import android.app.Application
import android.util.Log
import com.example.letmecook_lab5.repository.AppContainer
import com.example.letmecook_lab5.repository.DefaultAppContainer
import com.example.letmecook_lab5.repository.FirebaseGroceriesRepository
import com.example.letmecook_lab5.repository.FirebaseIngredientRepository
import com.example.letmecook_lab5.repository.FirebaseRecipeRepository
import com.example.letmecook_lab5.repository.FirebaseUserRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LetMeCookApplication : Application() {
    lateinit var container: AppContainer

    //lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
        //auth = Firebase.auth
        MainScope().launch(Dispatchers.IO){
            (container.recipeRepository as? FirebaseRecipeRepository)?.initializeData()
            (container.userRepository as? FirebaseUserRepository)?.initializeData()
            (container.ingredientRepository as? FirebaseIngredientRepository)?.initializeData()
            (container.groceriesRepository as? FirebaseGroceriesRepository)?.initializeData()

            val reviewRepository = container.reviewRepository
            try {
                // .first() "accende" il Flow, esegue la query una volta sola e prende il primo risultato
                reviewRepository.getReviewsByAuthor("101").first()
                Log.d("TEST_INDEX", "Query andata a buon fine! ${reviewRepository.getReviewsByAuthor("101").first()}")
            } catch (e: Exception) {
                // Se manca l'indice, l'eccezione verrà catturata qui e stampata in rosso
                Log.e("TEST_INDEX", "Errore: ${e.message}", e)
            }
        }
    }
}