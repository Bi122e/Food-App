package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class RestaurantPreview(

    val previewId: String = "",
    val userId: String = "",
    val orderId: String = "",
    val restaurantId: String = "",

    val rating: Int = 0,
    val message: String = "",
    val imageUrls: String = "",
    val userName: String = "",
    val avatarUrls: String = "",
    val previewTags: List<String> = emptyList(),

    @ServerTimestamp
    val updatedAt: Date? = null,
    @ServerTimestamp
    val createdAt: Date? = null
)

