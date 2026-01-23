package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RestaurantRepositoryImpl : RestaurantRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val restaurantRef = firestore.collection(Constance.COLLECTION_RESTAURANTS)

    override suspend fun getAllRestaurants(): ApiResponse<List<Restaurant>> {
        return try {
            val snapshot = restaurantRef
                .get()
                .await()
            val restaurants = snapshot.documents.mapNotNull {
                it.toObject(Restaurant::class.java)
            }
             ApiResponse.Success(restaurants)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get all restaurants")
        }
    }

    override fun getRestaurants(): Flow<ApiResponse<List<Restaurant>>> = callbackFlow {
        val listener = restaurantRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ApiResponse.Error(error.message ?: "Unknow"))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val restaurants = snapshot.mapNotNull {
                    it.toObject(Restaurant::class.java)
                }
                trySend(ApiResponse.Success(restaurants))
            } else {
                trySend(ApiResponse.Empty)
            }
        }
        awaitClose { listener.remove() }
    }

    override fun getRestaurantById(restaurantId: String): Flow<ApiResponse<Restaurant>> =
        callbackFlow {
            val listener =
                restaurantRef.document(restaurantId).addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Unknow"))
                        return@addSnapshotListener
                    }
                    val restaurant = snapshot?.toObject(Restaurant::class.java)
                    if (restaurant != null && restaurant.isValid()) {
                        trySend(ApiResponse.Success(restaurant))
                    } else {
                        trySend(ApiResponse.Empty)
                    }
                }
            awaitClose { listener.remove() }
        }

    override fun getFreeDeliveryRestaurants(): Flow<ApiResponse<List<Restaurant>>> = callbackFlow {
        trySend(ApiResponse.Loading)
        val listener =
            restaurantRef.whereEqualTo("deliveryFee", 0).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val restaurants = snapshot.mapNotNull {
                        it.toObject(Restaurant::class.java)
                    }
                    trySend(ApiResponse.Success(restaurants))
                } else {
                    trySend(ApiResponse.Empty)
                }
            }
    }

    override fun getNearbyRestaurants(
        userLat: Double, userLng: Double, radiusKm: Double
    ): Flow<ApiResponse<List<Restaurant>>> = callbackFlow {
        val listener =
            restaurantRef
                .whereGreaterThan("latitude", 0.0)
                .whereGreaterThan("longitude", 0.0)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val restaurants = snapshot.mapNotNull {
                            it.toObject(Restaurant::class.java)
                        }.filter { restaurant ->
                            restaurant.isValid() &&
                                    restaurant.hasLocation() &&
                                    restaurant.calculateDistance(userLat, userLng) <= radiusKm
                        }.sortedBy { it.calculateDistance(userLat, userLng) }

                        if (restaurants.isNotEmpty()) {
                            trySend(ApiResponse.Success(restaurants))
                        } else {
                            trySend(ApiResponse.Empty)
                        }
                    }
                }
        awaitClose { listener.remove() }
    }

    override fun getPopularRestaurants(limit: Int): Flow<ApiResponse<List<Restaurant>>> =
        callbackFlow {
            val listener = restaurantRef
                .limit(limit.toLong())
                .whereGreaterThanOrEqualTo("reviews", 10)
                .whereGreaterThanOrEqualTo("rating", 4.0)
                .orderBy("reviews")
                .orderBy("rating")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    }
                    if (snapshot != null) {
                        val restaurant = snapshot.mapNotNull {
                            it.toObject(Restaurant::class.java)
                        }
                        trySend(ApiResponse.Success(restaurant))
                    } else {
                        trySend(ApiResponse.Empty)
                    }
                }
            awaitClose { listener.remove() }
        }

    override fun getRestaurantsByCategory(category: String): Flow<ApiResponse<List<Restaurant>>> =
        callbackFlow {
            trySend(ApiResponse.Loading)
            val listener = restaurantRef
                .whereArrayContains("categories", category)
                .orderBy("categories")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Unknow"))
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val restaurants = snapshot.mapNotNull {
                            it.toObject(Restaurant::class.java).takeIf { restaurant ->
                                restaurant.isValid() && restaurant.isDeliveryFree()
                            }
                        }
                        if (restaurants.isNotEmpty()) {
                            trySend(ApiResponse.Success(restaurants))
                        } else {
                            trySend(ApiResponse.Empty)
                        }
                    }
                }
            awaitClose { listener.remove() }
        }

    override fun getHighlyRatedRestaurants(limit: Int): Flow<ApiResponse<List<Restaurant>>> =
        callbackFlow {
            val listener = restaurantRef
                .limit(limit.toLong())
                .orderBy("rating")
                .orderBy("reviews")
                .whereGreaterThanOrEqualTo("rating", 4.0)
                .whereGreaterThanOrEqualTo("reviews", 15)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Unknow"))
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val restaurants = snapshot.mapNotNull {
                            it.toObject(Restaurant::class.java).takeIf { restaurant ->
                                restaurant.isValid() && restaurant.isHighlyRated()
                            }
                        }
                        if (restaurants.isNotEmpty()) {
                            trySend(ApiResponse.Success(restaurants))
                        } else {
                            trySend(ApiResponse.Empty)
                        }
                    }
                }
            awaitClose { listener.remove() }
        }

    override fun searchRestaurants(query: String): Flow<ApiResponse<List<Restaurant>>> =
        callbackFlow {
            val listener = restaurantRef
                .whereGreaterThanOrEqualTo("restaurantName", query)
                .whereLessThan("restaurantName", query + '\uf8ff')
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    }
                    if (snapshot != null) {
                        val restaurants = snapshot.mapNotNull {
                            it.toObject(Restaurant::class.java).takeIf { restaurant ->
                                restaurant.isValid()
                            }
                        }
                        if (restaurants.isNotEmpty()) {
                            trySend(ApiResponse.Success(restaurants))
                        } else {
                            trySend(ApiResponse.Empty)
                        }
                    }
                }
            awaitClose { listener.remove() }
        }

    override suspend fun addReview(restaurantId: String, rating: Double): ApiResponse<Unit> {
        return try {
            require(rating in 1.0..5.0) { "Danh gia san pham trong khoang 1.0 den 5.0" }
            val restaurantDoc = restaurantRef.document(restaurantId).get().await()
            val restaurant = restaurantDoc?.toObject(Restaurant::class.java)
            if (restaurant != null) {
                val updatedRestaurant = restaurant.addReview(rating)
                restaurantRef.document(restaurantId).set(updatedRestaurant).await()
                ApiResponse.Success(Unit)
            } else {
                ApiResponse.Error("Restaurant not found")
            }
            //-> fix 2 user cung add, 1 review bị mất
            //firestore.runTransaction { tx ->
            //    val ref = restaurantRef.document(restaurantId)
            //    val snapshot = tx.get(ref)
            //    val restaurant = snapshot.toObject(Restaurant::class.java)
            //        ?: throw Exception("Not found")
            //
            //    val updated = restaurant.addReview(rating)
            //    tx.set(ref, updated)
            //}
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to add review")
        } catch (e: IllegalArgumentException) {
            ApiResponse.Error(e.message ?: "Invalid rating")
        }
    }

    override suspend fun updateRestaurantStatus(
        restaurantId: String,
        isOpen: Boolean
    ): ApiResponse<Unit> {
        return try {
            restaurantRef.document(restaurantId)
                .update("isOpen", isOpen)
            ApiResponse.Success(Unit)

        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update status")
        }
    }
}