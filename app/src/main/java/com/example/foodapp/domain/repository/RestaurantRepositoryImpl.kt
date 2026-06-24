package com.example.foodapp.domain.repository

import android.util.Log
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.core.PaginationResult
import com.example.foodapp.core.utils.toNormalizeSearch
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.domain.model.RatingCount
import com.example.foodapp.domain.model.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.Query
import com.google.protobuf.Api
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import okhttp3.Response
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RestaurantRepository {
    private val restaurantRef = firestore.collection(Constance.COLLECTION_RESTAURANTS)

    override suspend fun getRandomRestaurantsByDifferentTags(): ApiResponse<List<Restaurant>> {
        Log.d("CheckRestaurantRepo", "getRandomRestaurantsByDifferentTags run")
        return try {
            val allTags = listOf(
                "tra-sua",
                "com",
                "pizza",
                "bun",
                "banh-mi",
                "ca-kho",
                "tra-me",
                "bun-moc",
                "bun-thit-nuong",
            )
//            val randomTag = allTags.shuffled().take(3)
            val randomTag = allTags.shuffled()

            val result = mutableListOf<Restaurant>()
            val usedRestaurantIds = mutableSetOf<String>()
            randomTag.forEach { tag ->
                Log.d("CheckRestaurantRepo", "query tag = $tag")

                val snapshot = firestore.collection(Constance.COLLECTION_RESTAURANTS)
                    .whereArrayContains("categories", tag)
                    .limit(5)
                    .get()
                    .addOnSuccessListener {
                        Log.d("OrderFlow_cheeck", "DATA=${it.size()}")
                    }
                    .addOnFailureListener {
                        Log.d("OrderFlow_cheeck", "ERROR=${it.message}")
                    }
                    .await()
                Log.d("CheckRestaurantRepo", "snapshot size = ${snapshot.size()}")

                val restaurants = snapshot.toObjects(Restaurant::class.java)
                    .filter { it.restaurantId !in usedRestaurantIds }
                Log.d("CheckRestaurantRepo", "restaurants = $restaurants")

                val randomResult = restaurants.randomOrNull()
                if (randomResult != null) {
                    result.add(randomResult)
                    usedRestaurantIds.add(randomResult.restaurantId)
                }
            }
            Log.d("CheckRestaurantRepo", "getRandomRestaurantsByDifferentTags success $result")
            ApiResponse.Success(result)
        } catch (e: Exception) {
            Log.d("CheckRestaurantRepo", "getRandomRestaurantsByDifferentTags error ${e.message}")
            ApiResponse.Error(e.message ?: "Failed to load restaurant")
        }
    }

    override suspend fun searchRestaurants(query: String): ApiResponse<PaginationResult<Restaurant>> {
        val keyword = query.toNormalizeSearch()
        if (keyword.isEmpty()) return ApiResponse.Empty
        return try {
            val snapshot = restaurantRef
                .whereGreaterThanOrEqualTo("searchName", query.lowercase())
                .whereLessThan("searchName", query.lowercase() + "\uf8ff")
                .get()
                .await()
            val restaurants = snapshot.toObjects(Restaurant::class.java)
            ApiResponse.Success(
                PaginationResult(
                    data = restaurants,
                    lastDoc = snapshot.lastOrNull(),
                    isEndReached = false,
                )
            )
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to searchRestaurants")
        }
    }

    override suspend fun getAllRestaurants(
        category: String?,
        lastDoc: DocumentSnapshot?
    ): ApiResponse<PaginationResult<Restaurant>> {

        return try {

            Log.d("Check_getAllRestaurants", "getAllRestaurants cate ${category}")
            val handleCategory = category ?: "tat-ca"
            var query = restaurantRef
                .whereArrayContains("categories", handleCategory)
                .orderBy("createdAt")
                .limit(2)
            if (lastDoc != null) {
                query = query.startAfter(lastDoc)
            }

            val snapshot = query.get().await()
            Log.d("Firestore", "count=${snapshot.size()}")

            val restaurants = snapshot.toObjects(Restaurant::class.java)
            val newLastDoc = snapshot.documents.lastOrNull()

            Log.d("CheckRestaurantRepo", "getAllRestaurants success $restaurants")
            ApiResponse.Success(
                PaginationResult(
                    data = restaurants,
                    lastDoc = newLastDoc,
                    isEndReached = restaurants.size < 2 //neu nho < 3 tuc la het du lieu, end = true
                )
            )
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get restaurants")
        }
    }

    override suspend fun getRestaurants(category: String?): ApiResponse<PaginationResult<Restaurant>> {
        return try {
            val handleCategory = category ?: "tat-ca"
            Log.d(
                "Check_getAllRestaurants",
                "getRestaurants: handle = ${handleCategory} category = $category "
            )
            Log.d(
                "check_InitialRestaurants",
                "handleCategory: $handleCategory -  category$category "
            )
            val snapshot = restaurantRef
                .whereArrayContains("categories", handleCategory)
                .orderBy("createdAt")
                .limit(6)
                .get()
                .await()
            val restaurants = snapshot.toObjects(Restaurant::class.java)
            val newDoc = snapshot.documents.lastOrNull()

            Log.d("getRestaurants", "getRestaurants success $restaurants")
            Log.d("Check_getAllRestaurants", "getRestaurants: ${restaurants.size}")

            ApiResponse.Success(
                data = PaginationResult(
                    data = restaurants,
                    lastDoc = newDoc,
                    isEndReached = restaurants.size < 5, //tranh query cuoi isEMpty de goi load null
                )
            )
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to load restaurants")
        }
    }

    override suspend fun updateRatingCount(restaurantId: String, ratingType: String): ApiResponse<Unit> {
        return try {

            Log.d("checkDB_updateRatingCount", "run -> ratingCount.$ratingType ")
            restaurantRef
                .document(restaurantId)
                .update(
                    "ratingCount.$ratingType", FieldValue.increment(1)
                )
                .await()
            Log.d("checkDB_updateRatingCount", "success")
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            Log.d("checkDB_updateRatingCount", "error ${e.message}")
            ApiResponse.Error(e.message ?: "error")
        }
    }

    override suspend fun getRestaurantById(restaurantId: String): ApiResponse<Restaurant> {
        return try {
            val doc = restaurantRef
                .document(restaurantId)
                .get()
                .await()
            val restaurant = doc.toObject(Restaurant::class.java)
            if (restaurant != null) {
                Log.d("checkFB_getRestaurantById", "success: $restaurant")
                ApiResponse.Success(restaurant)
            }
            else {
                Log.d("checkFB_getRestaurantById", "ERROR NULL")
                ApiResponse.Error("Restaurant not found")
            }

        } catch (e: Exception) {
            Log.d("checkFB_getRestaurantById", e.message ?: "error")
            ApiResponse.Error(e.message ?: "error")

        }
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
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        trySend(ApiResponse.Error(error.message ?: "Unknown"))
                        Log.d("HomeViewModelState", " categort error")
                        return@addSnapshotListener
                    }

                    if (snapshot == null || snapshot.isEmpty) {
                        trySend(ApiResponse.Empty)
                        Log.d("HomeViewModelState", "category empty error")

                        return@addSnapshotListener
                    }

                    val restaurants = snapshot.mapNotNull {
                        it.toObject(Restaurant::class.java)

                    }

                    if (restaurants.isNotEmpty()) {
                        Log.d("HomeViewModelState", "res cate success")
                        trySend(ApiResponse.Success(restaurants))
                    } else {
                        Log.d("HomeViewModelState", "res cate empty")
                        trySend(ApiResponse.Empty)
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