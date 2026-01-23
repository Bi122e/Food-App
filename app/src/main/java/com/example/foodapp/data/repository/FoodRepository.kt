package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun getFoods(): Flow<ApiResponse<List<Food>>>

    suspend fun getFoodById(foodId: String): ApiResponse<Food>

    suspend fun getFeaturedFood(limit: Int = 10): Flow<ApiResponse<List<Food>>>

    fun getPopularFoods(limit: Int = 10): Flow<ApiResponse<List<Food>>>

    suspend fun addReview(foodId: String, rating: Double): ApiResponse<Unit>

    fun getFoodsByRestaurant(restaurantId: String): Flow<ApiResponse<List<Food>>>

    fun getFoodsByCategory(categoryId: String): Flow<ApiResponse<List<Food>>>

    suspend fun searchFoods(query: String): ApiResponse<List<Food>>
}