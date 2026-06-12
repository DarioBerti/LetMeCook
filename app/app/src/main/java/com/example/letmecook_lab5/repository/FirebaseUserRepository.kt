package com.example.letmecook_lab5.repository

import android.util.Log
import com.example.letmecook_lab5.domain.Collections
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FirebaseUserRepository(
    private val firestore : FirebaseFirestore
) : UserRepository {

    private val userCollection = firestore.collection(Collections.USERS)

    suspend fun initializeData(){
        val existing = userCollection.limit(1).get().await()
        if(existing.isEmpty){
            firestore.runBatch { batch ->
                placeholderUsers.forEach { user ->
                    val docRef = userCollection.document(user.id)
                    batch.set(docRef, user)
                    placeholderCollections.forEach { collection ->
                        val collectionRef = docRef.collection(Collections.COLLECTIONS).document(collection.id)
                        batch.set(collectionRef, collection)
                    }
                }
            }.await()
        }
    }

    override fun getUserById(id: String): Flow<User?> {
        try{
            val userRef = userCollection.document(id)

            return userRef.snapshots().map{snapshot ->

                Log.d("PROFILE_DEBUG", "exists = ${snapshot.exists()}")

                Log.d("PROFILE_DEBUG", "raw data = ${snapshot.data}")

                val user = snapshot.toObject(User::class.java)

                Log.d("PROFILE_DEBUG", "mapped user = $user")

                user
            }
        }
        catch (e: Exception){
            throw e
        }
    }

    override fun getAllUsers(): Flow<List<User?>> = callbackFlow {
        val listener = userCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val users = snapshot.documents.map { doc ->
                    doc.toObject(User::class.java)?.copy(id = doc.id)
                }

                trySend(users)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun updateUser(user: User) {
        try {
            userCollection.document(user.id).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getCollectionsByOwner(ownerId: String): Flow<List<Collection>> {
        return userCollection.document(ownerId).collection(Collections.COLLECTIONS)
            .snapshots()
            .map{snapshot ->
                snapshot.toObjects(Collection::class.java)
            }
    }

    override suspend fun saveRecipeToCollections(
        userId: String,
        recipeId: String,
        collectionIds: List<String>
    ) {
        try {
            val allCollections = userCollection.document(userId)
                .collection(Collections.COLLECTIONS)
                .get()
                .await()

            firestore.runBatch { batch ->
                allCollections.documents.forEach { doc ->
                    if (doc.id in collectionIds) {
                        batch.update(doc.reference, "recipeIds", FieldValue.arrayUnion(recipeId))
                    } else {
                        batch.update(doc.reference, "recipeIds", FieldValue.arrayRemove(recipeId))
                    }
                }
            }.await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun addCollection(userId: String, collection: Collection) {
        try {
            userCollection.document(userId)
                .collection(Collections.COLLECTIONS)
                .document(collection.id)
                .set(collection)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteCollection(userId: String, collectionId: String) {
        try {
            userCollection.document(userId)
                .collection(Collections.COLLECTIONS)
                .document(collectionId)
                .delete()
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun ensureUserProfile(firebaseUser: FirebaseUser) {

        val uid = firebaseUser.uid

        val docRef = userCollection.document(uid)

        val snapshot = docRef.get().await()

        if (!snapshot.exists()) {

            val newUser = User(
                id = uid,
                fullName = firebaseUser.displayName ?: "Luke Cook",
                nickname = firebaseUser.email?.substringBefore("@") ?: "user",
                email = firebaseUser.email ?: "letmecook.team@gmail.com",
                cookingRole = "Home Chef",
                followers = 0,
                following = 0,
                dietaryPreferences = emptyList(),
                typesOfCuisine = emptyList(),
                favoriteIngredients = emptyList(),
                savedRecipesIds = emptyList(),
                profileImageUri = "",
                cookedRecipesIds = emptyList()
            )
            docRef.set(newUser).await()

            val defaultCollection = Collection(
                name = "Saved",
                ownerId = uid,
                description = "All my favorite recipes in one place",
                recipeIds = emptyList()
            )
            addCollection(uid, defaultCollection)
        }
    }
}


val placeholderUsers = listOf(
    User(
        id = "baIhsE6qFzMuvrwiqyA83NSnzwE2",    //firebase UID of letmecook.team@gmail.com
        fullName = "Luca Rossi",
        nickname = "LukeCook",
        email = "letmecook.team@gmail.com@",
        cookingRole = "Home Chef",
        followers = 120,
        following = 80,
        dietaryPreferences = listOf("Vegetarian"),
        typesOfCuisine = listOf("Italian", "Japanese"),
        favoriteIngredients = listOf("Tomato", "Basil"),
        savedRecipesIds = listOf("1", "2"),
        profileImageUri = "",
        cookedRecipesIds = listOf("2")
    ),
    User(
        id = "U3xYzBPl3UV04pgChCmaGEnMQIR2",
        fullName = "Dario Berti",
        nickname = "dario.berti100",
        email = "dario.berti100@gmail.com",
        cookingRole = "Home Chef",
        followers = 0,
        following = 0,
        dietaryPreferences = emptyList(),
        typesOfCuisine = emptyList(),
        favoriteIngredients = emptyList(),
        savedRecipesIds = emptyList(),
        profileImageUri = "",
        cookedRecipesIds = emptyList()
    )
)

val placeholderCollections = listOf(
    Collection(
        id = "1",
        name = "Saved",
        ownerId = "baIhsE6qFzMuvrwiqyA83NSnzwE2",
        description = "All my favorite recipes in one place",
        recipeIds = listOf("11", "12", "13", "14")
    ),
    Collection(
        id = "2",
        name = "Lunch ideas",
        ownerId = "101",
        description = "Quick meals for busy days",
        recipeIds = listOf("3", "4")
    ),
    Collection(
        id = "3",
        name = "Weekend cooking",
        ownerId = "101",
        description = "Recipes worth taking time for",
        recipeIds = listOf("5", "6")
    )
)