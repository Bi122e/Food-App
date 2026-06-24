package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.PaginationResult
import com.example.foodapp.domain.model.RatingCount
import com.example.foodapp.domain.model.Restaurant
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import okhttp3.Response

interface RestaurantRepository {

//    suspend fun getRestaurants(): ApiResponse<PaginationResult<Restaurant>>

    suspend fun getRandomRestaurantsByDifferentTags(): ApiResponse<List<Restaurant>>

//    suspend fun getAllRestaurants(): Pair<List<Restaurant>, DocumentSnapshot?>

    fun getRestaurantsByCategory(category: String = "nuoc"): Flow<ApiResponse<List<Restaurant>>>
    suspend fun updateRatingCount(restaurantId: String, ratingType: String): ApiResponse<Unit>

    suspend fun getRestaurantById(restaurantId: String): ApiResponse<Restaurant>

    suspend fun searchRestaurants(query: String): ApiResponse<PaginationResult<Restaurant>>

    fun getPopularRestaurants(limit: Int = 10): Flow<ApiResponse<List<Restaurant>>>

    fun getHighlyRatedRestaurants(limit: Int = 10): Flow<ApiResponse<List<Restaurant>>>

    fun getNearbyRestaurants(
        userLat: Double,
        userLng: Double,
        radiusKm: Double = 5.0
    ): Flow<ApiResponse<List<Restaurant>>>

    fun getFreeDeliveryRestaurants(): Flow<ApiResponse<List<Restaurant>>>

    suspend fun addReview(restaurantId: String, rating: Double): ApiResponse<Unit>
    suspend fun updateRestaurantStatus(restaurantId: String, isOpen: Boolean): ApiResponse<Unit>
//    suspend fun getAllRestaurants(lastDoc: DocumentSnapshot?): ApiResponse<PaginationResult<Restaurant>>
    suspend fun getRestaurants(category: String?): ApiResponse<PaginationResult<Restaurant>>
    suspend fun getAllRestaurants(
        category: String?,
        lastDoc: DocumentSnapshot?
    ): ApiResponse<PaginationResult<Restaurant>>
}