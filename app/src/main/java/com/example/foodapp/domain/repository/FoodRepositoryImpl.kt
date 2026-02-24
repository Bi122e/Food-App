package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.domain.Review
import com.example.foodapp.domain.model.Food
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FoodRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): FoodRepository {

    private val foodRef = firestore.collection(Constance.COLLECTION_FOOD)

    override suspend fun getFeaturedFood(limit: Int): Flow<ApiResponse<List<Food>>> = callbackFlow{
        val snapshot = foodRef
            .whereGreaterThan("totalRating", 4.2)
            .orderBy("totalRating", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Can't found featured food"))
                    return@addSnapshotListener
                }
                val featuredFood = snapshot?.documents?.mapNotNull {
                    it.toObject(Food::class.java)
                } ?: emptyList()
                trySend(ApiResponse.Success(featuredFood))
            }
        awaitClose { snapshot.remove() }
    }
    override fun getFoods(): Flow<ApiResponse<List<Food>>> = callbackFlow {
        val listener = foodRef.addSnapshotListener { snapshot, error ->
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

    override fun getFoodsByRestaurant(restaurantId: String): Flow<ApiResponse<List<Food>>> =
        callbackFlow {
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

    override suspend fun getFoodById(foodId: String): ApiResponse<Food>{
        return try {
            val foodRef = foodRef.document(foodId)
                .get()
                .await()
            val food = foodRef.toObject(Food::class.java)
                ?: return ApiResponse.Error("Food not found")
            ApiResponse.Success(food)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get Food by id")
        }
    }

    override fun getPopularFoods(limit: Int): Flow<ApiResponse<List<Food>>> = callbackFlow {

        val listener = foodRef
            .whereGreaterThan("reviews", 10)
            .orderBy(
                "reviews",
                Query.Direction.DESCENDING
            ) //ko dung cai nay se loi, query cao -> thap
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

    override fun getFoodsByCategory(categoryId: String): Flow<ApiResponse<List<Food>>> =
        callbackFlow {
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

    override suspend fun addReview(foodId: String, review: Review): ApiResponse<Unit> {
        return try {
            val foodRef = foodRef.document(foodId)
            val reviewRef = foodRef.collection(Constance.COLLECTION_REVIEWS).document()

            val reviewWithId = review.copy(reviewId = reviewRef.id)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(foodRef)
                val currentCount = snapshot.getLong("reviewCount") ?: 0
                val currentAverage = snapshot.getDouble("averageRate") ?: 0.0

                val newCount = currentCount + 1
                val newAverage = ((currentAverage * currentCount) + review.rating) / newCount

                transaction.set(reviewRef, reviewWithId)
                transaction.update(
                    foodRef, mapOf(
                        "reviewCount" to newCount,
                        "averageRate" to newAverage
                    )
                )
            }.await()
            ApiResponse.Success(Unit)
//            val food = foodDoc.toObject(Food::class.java)
//
//            if (food != null) {
//                val updatedFood = food.addReview(rating)
//                foodRef.document(foodId).set(updatedFood).await()
//                ApiResponse.Success(Unit)
//            } else {
//                ApiResponse.Error("Food not found")
//            }

        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to add review")
        }
    }

    override suspend fun getReviews(foodId: String): ApiResponse<List<Review>> {
        return try {
            val reviewDoc = foodRef
                .document(foodId)
                .collection(Constance.COLLECTION_REVIEWS)
                .orderBy("rating", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()
            val reviews = reviewDoc.documents.mapNotNull {
                it.toObject(Review::class.java)
            }
            ApiResponse.Success(reviews)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get Reviews")
        }
    }

    override suspend fun searchFoods(query: String): ApiResponse<List<Food>> {
        return try {
            val lowerQuery = query.lowercase()

            val snapshot = foodRef
                .orderBy("nameLower") //k co nay se bi loi
                .whereGreaterThanOrEqualTo("nameLower", lowerQuery)
                .whereLessThan("nameLower", lowerQuery + '\uf8ff')
                .get()
                .await()
            val foods = snapshot.documents.mapNotNull {
                it.toObject(Food::class.java)
            }
            ApiResponse.Success(foods)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to search food")
        }

    }
}