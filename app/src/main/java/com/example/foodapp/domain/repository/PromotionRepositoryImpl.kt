package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.PromotionRepository
import com.example.foodapp.domain.model.Promotion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class PromotionRepositoryImpl @Inject constructor(
    private val firebase: FirebaseFirestore,
): PromotionRepository {
    private val promoCollection = firebase.collection(Constance.COLLECTION_PROMOTION)

    override suspend fun getPromotions(): ApiResponse<List<Promotion>> {
        return try {
            val promoCollection = promoCollection
                .get()
                .await()
            val promotions = promoCollection.documents.mapNotNull {
                it.toObject(Promotion::class.java)
            }
            ApiResponse.Success(promotions)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get promotions")
        }
    }
}