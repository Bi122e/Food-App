package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Promotion(
    val promoId: String = "",
    val discountPercent: Int = 0,
    val foodNameIds: List<String> = emptyList(),
    val promoUrl: String = "",
    val restaurantId: String = "",
    @ServerTimestamp
    val startDate: Date? = null,
    @ServerTimestamp
    val endDate: Date? = null,
    val name: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)
