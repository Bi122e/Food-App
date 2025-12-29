package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.repository.FoodRepository
import com.google.android.gms.common.api.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FoodRepositoryImpl(
    private val firestore: FirebaseFirestore
): FoodRepository {

    private val foodRef = firestore.collection(Constance.COLLECTION_FOOD)

    override fun getFoods(): Flow<ApiResponse<List<Food>>> = callbackFlow{
        val listener = foodRef.addSnapshotListener{ snapshot, error ->
            if (error != null) {
                trySend(ApiResponse.Error(error.message ?: "Unknow Error"))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val foods = snapshot.documents.mapNotNull {
                    it.toObject(Food::class.java)
                }
                trySend(ApiResponse.Success(foods))
            } else {
                trySend(ApiResponse.Empty)
            }
        }
        awaitClose{listener.remove()}
    }

    override fun getFoodsByRestaurant(restaurantId: String): Flow<ApiResponse<List<Food>>> = callbackFlow{
        val listener = foodRef
            .whereEqualTo("restaurants", restaurantId)
            .addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ApiResponse.Error(error.message ?: "Unknow Error"))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val foods = snapshot.documents.mapNotNull {
                    it.toObject(Food::class.java)
                }
                trySend(ApiResponse.Success(foods))

            } else {
                trySend(ApiResponse.Empty)
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getFoodById(foodId: String): Flow<ApiResponse<Food>> = callbackFlow {
        val listener = foodRef.document(foodId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    return@addSnapshotListener
                }
                val food = snapshot?.toObject(Food::class.java)
                if (food != null) {
                    trySend(ApiResponse.Success(food))
                } else {
                    trySend(ApiResponse.Error("Food not found"))
                }
        }
        awaitClose { listener.remove() }
}

    override fun getPopularFoods(limit: Int): Flow<ApiResponse<List<Food>>> = callbackFlow {

        val listener = foodRef
            .whereGreaterThan("reviews", 10)
            .orderBy("reviews", Query.Direction.DESCENDING) //ko dung cai nay se loi, query cao -> thap
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val foods = snapshot.toObjects(Food::class.java)
                    trySend(ApiResponse.Success(foods))
                } else {
                    trySend(ApiResponse.Empty)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getFoodsByCategory(categoryId: String): Flow<ApiResponse<List<Food>>> = callbackFlow{
        val listener = foodRef
            .whereEqualTo("categoryId", categoryId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val foods = snapshot.documents.mapNotNull {
                        it.toObject(Food::class.java)
                    }
                    trySend(ApiResponse.Success(foods))
                } else {
                    trySend(ApiResponse.Empty)
                }
            }
            awaitClose { listener.remove() }
    }

    override suspend fun addReview(foodId: String, rating: Double): ApiResponse<Unit> {
        return try {
            val foodDoc = foodRef.document(foodId).get().await()
            val food = foodDoc.toObject(Food::class.java)

            if (food != null) {
                val updatedFood = food.addReview(rating)
                foodRef.document(foodId).set(updatedFood).await()
                ApiResponse.Success(Unit)
            } else {
                ApiResponse.Error("Food not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to add review")
        }
    }

    override fun searchFoods(query: String): Flow<ApiResponse<List<Food>>> = callbackFlow {
       val listener = foodRef
           .orderBy("name") //k co nay se bi loi
           .whereGreaterThanOrEqualTo("name", query)
           .whereLessThan("name", query + '\uf8ff')
           .addSnapshotListener { snapshot, error ->
           if (error != null) {
               trySend(ApiResponse.Error(error.message ?: "Unknow"))
               return@addSnapshotListener
           }
           if (snapshot != null) {
               val foods = snapshot.documents.mapNotNull {
                   it.toObject(Food::class.java)
               }
               trySend(ApiResponse.Success(foods))
           } else {
               trySend(ApiResponse.Empty)
           }
       }
        awaitClose { listener.remove() }
    }
}