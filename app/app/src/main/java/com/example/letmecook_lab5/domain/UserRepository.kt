package com.example.letmecook_lab5.domain

import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserById  (id: String): Flow<User?>
    fun getAllUsers(): Flow<List<User?>>

    suspend fun updateUser(user: User)

    fun getCollectionsByOwner(ownerId: String): Flow<List<Collection>>
    suspend fun saveRecipeToCollections(userId: String, recipeId: String, collectionIds: List<String>)

    suspend fun addCollection(userId: String, collection: Collection)
    suspend fun deleteCollection(userId: String, collectionId: String)
    suspend fun followUser(followerId: String, followedId: String)
    suspend fun unfollowUser(followerId: String, followedId: String)
    fun getFollowers(userId: String): Flow<List<User>>
    fun getFollowing(userId: String): Flow<List<User>>
}