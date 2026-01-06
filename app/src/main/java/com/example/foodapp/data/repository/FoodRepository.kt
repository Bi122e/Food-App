package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun getFoods(): Flow<ApiResponse<List<Food>>>

    fun getFoodById(foodId: String): Flow<ApiResponse<Food>>

    fun getPopularFoods(limit: Int = 10): Flow<ApiResponse<List<Food>>>

    suspend fun addReview(foodId: String, rating: Double): ApiResponse<Unit>

    fun getFoodsByRestaurant(restaurantId: String): Flow<ApiResponse<List<Food>>>

    fun getFoodsByCategory(categoryId: String): Flow<ApiResponse<List<Food>>>

    fun searchFoods(query: String): Flow<ApiResponse<List<Food>>>
}