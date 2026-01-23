package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {

    fun getRestaurants(): Flow<ApiResponse<List<Restaurant>>>
    suspend fun getAllRestaurants(): ApiResponse<List<Restaurant>>

    fun getRestaurantsByCategory(category: String): Flow<ApiResponse<List<Restaurant>>>

    fun getRestaurantById(restaurantId: String): Flow<ApiResponse<Restaurant>>

    fun searchRestaurants(query: String): Flow<ApiResponse<List<Restaurant>>>

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
}