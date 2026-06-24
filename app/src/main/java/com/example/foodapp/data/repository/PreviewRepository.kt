package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Preview
import kotlinx.coroutines.flow.Flow

interface PreviewRepository {


    fun observePreviews(restaurantId: String): Flow<ApiResponse<List<Preview>>>

    suspend fun createPreview(orderId: String, preview: Preview): ApiResponse<Unit>

    suspend fun deletePreview(previewId: String): ApiResponse<Unit>

}