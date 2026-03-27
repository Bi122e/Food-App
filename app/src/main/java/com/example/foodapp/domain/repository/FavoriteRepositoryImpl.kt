package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.FavoriteRepository
import com.example.foodapp.domain.model.Favorite
import com.google.firebase.firestore.FirebaseFirestore
 import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    val firestore: FirebaseFirestore
): FavoriteRepository {

    private val favoriteCollection = firestore.collection(Constance.COLLECTION_FAVORITES)

    override suspend fun addFavorite(favorite: Favorite): ApiResponse<Unit> {
        return try {
            val docRef = favoriteCollection.document()
            val data = favorite.copy(
                favoriteId = docRef.id,
                userId = favorite.userId,
                isValid = true,
                createdAt = Date(),
                updatedAt = Date(),
            )
            docRef.set(data).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to add favorite")
        }
    }

    override fun observeFavorites(userId: String): Flow<ApiResponse<List<Favorite>>> = callbackFlow {
        val listener = favoriteCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Failed to observe favorite"))
                    close(error)
                    return@addSnapshotListener
                }
//                val favorite = snapshot?.toObjects(Favorite::class.java) ?: emptyList()
                val favorite = snapshot?.documents?.mapNotNull {
                 it.toObject(Favorite::class.java)
                } ?: emptyList()
                trySend(ApiResponse.Success(favorite))
            }
        awaitClose { listener.remove() }
    }

    override suspend fun removeFavorite(favoriteId: String): ApiResponse<Unit> {
        return try {
            favoriteCollection.document(favoriteId).delete().await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to remove favorite")
        }

    }
    override suspend fun isFavorite(userId: String, foodId: String): ApiResponse<Boolean> {
        return try {
            val snapshot = favoriteCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("foodId", foodId)
                .get()
                .await()
            ApiResponse.Success(snapshot.isEmpty.not())
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "check favorite failed")
        }
    }
}