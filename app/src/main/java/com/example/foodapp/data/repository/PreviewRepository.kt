package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
 import com.example.foodapp.domain.model.RestaurantPreview
import kotlinx.coroutines.flow.Flow

interface PreviewRepository {


    fun observePreviews(restaurantId: String): Flow<ApiResponse<List<RestaurantPreview>>>

    suspend fun createPreview(orderId: String, preview: RestaurantPreview): ApiResponse<Unit>

    suspend fun deletePreview(previewId: String): ApiResponse<Unit>

}