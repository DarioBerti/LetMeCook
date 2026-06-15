package com.example.letmecook_lab5.repository

import android.util.Log
import com.example.letmecook_lab5.domain.GroceriesRepository
import com.example.letmecook_lab5.domain.Collections
import com.example.letmecook_lab5.model.CartIngredient
import com.example.letmecook_lab5.model.CartRecipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirebaseGroceriesRepository (
    private val firestore: FirebaseFirestore
) : GroceriesRepository {
    private val userCollection = firestore.collection(Collections.USERS)

    suspend fun initializeData(){
        val existing = userCollection.document("xIm055MN1pOdo7WLbLbuvbVQJWz1").collection(Collections.GROCERIES).limit(1).get().await()
        if (existing.isEmpty){
            firestore.runBatch { batch ->
                placeholderGroceries.forEach { groceries ->
                    val docRef = userCollection.document("xIm055MN1pOdo7WLbLbuvbVQJWz1").collection(Collections.GROCERIES).document(groceries.recipeId )
                    batch.set(docRef, groceries)
                }
            }.await()
        }
    }

    override fun getCartItems(userId: String): Flow<List<CartRecipe>> {
        return userCollection.document(userId).collection(Collections.GROCERIES)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(CartRecipe::class.java)
            }
    }

    override suspend fun addToCart(userId: String, item: CartRecipe) {
        try{
            userCollection.document(userId)
                .collection(Collections.GROCERIES)
                .document(item.recipeId)
                .set(item)
                .await()
        }catch (e: Exception){
            throw e
        }
    }

    override fun toggleIsChecked(userId: String, recipeId: String, ingredientName: String) {
        val docRef = userCollection.document(userId)
            .collection(Collections.GROCERIES)
            .document(recipeId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            val recipe = snapshot.toObject(CartRecipe::class.java)
                ?: throw FirebaseFirestoreException(
                    "Document not found or invalid",
                    FirebaseFirestoreException.Code.NOT_FOUND
                )

            val updatedIngredients = recipe.ingredients.map { ingredient ->
                if (ingredient.name == ingredientName) {
                    ingredient.copy(isChecked = !ingredient.isChecked)
                } else {
                    ingredient
                }
            }

            transaction.update(docRef, "ingredients", updatedIngredients)

        }.addOnSuccessListener {
            Log.d("toggleIsChecked", "Transaction success")
        }.addOnFailureListener { e ->
            Log.w("toggleIsChecked", "Transaction failure.", e)
        }
    }

    override fun updateRecipeServings(userId: String, recipeId: String, newServings: Int) {
        val docRef = userCollection.document(userId)
            .collection(Collections.GROCERIES)
            .document(recipeId)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)

            val recipe = snapshot.toObject(CartRecipe::class.java)
                ?: throw FirebaseFirestoreException(
                    "Document not found or invalid",
                    FirebaseFirestoreException.Code.NOT_FOUND
                )

            val oldServings = recipe.servings

            val updatedIngredients = recipe.ingredients.map { ingredient ->
                ingredient.copy(
                    quantity = (ingredient.quantity * newServings) / oldServings
                )
            }

            transaction.update(docRef, "servings", newServings)
            transaction.update(docRef, "ingredients", updatedIngredients)

        }.addOnSuccessListener {
            Log.d("updateRecipeServings", "Transaction success")
        }.addOnFailureListener { e ->
            Log.w("updateRecipeServings", "Transaction failure.", e)
        }
    }

    override suspend fun deleteRecipe(userId: String, recipeId: String) {
        try{
            userCollection.document(userId)
                .collection(Collections.GROCERIES)
                .document(recipeId)
                .delete()
                .await()
        }catch (e: Exception){
            throw e
        }
    }

}

val placeholderGroceries = listOf(
    CartRecipe(
        recipeId = "11",
        recipeTitle = "Classic Beef Wellington",
        servings = 4,
        recipeImage = "https://firebasestorage.googleapis.com/v0/b/letmecook-d3112.firebasestorage.app/o/recipe_images%2F11.jpg?alt=media&token=93fb5eee-c215-4e1d-b277-09f58a273507",
        ingredients = listOf(
            CartIngredient(
                name = "Beef tenderloin",
                quantity = 800.0,
                unit = "g",
                isChecked = false
            ),
            CartIngredient(name = "Puff pastry", quantity = 500.0, unit = "g", isChecked = false),
            CartIngredient(name = "Mushrooms", quantity = 400.0, unit = "g", isChecked = false),
            CartIngredient(name = "Prosciutto", quantity = 100.0, unit = "g", isChecked = false),
            CartIngredient(name = "Egg", quantity = 1.0, unit = "pcs", isChecked = false)
        )
    )
)