package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Promotion

interface PromotionRepository {

    suspend fun getPromotions(): ApiResponse<List<Promotion>>
}