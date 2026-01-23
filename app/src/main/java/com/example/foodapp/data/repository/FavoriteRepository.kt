package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeFavorites(userId: String): Flow<ApiResponse<List<Favorite>>>
    suspend fun addFavorite(favorite: Favorite): ApiResponse<Unit>
    suspend fun removeFavorite(favoriteId: String): ApiResponse<Unit>
    suspend fun isFavorite(userId: String, foodId: String): ApiResponse<Boolean>

}