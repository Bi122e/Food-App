package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class AppNotification(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val message: String = "",
    val type: String = NotificationType.SYSTEM.name,
    val payload: Map<String, String> = emptyMap(),
    val active: Boolean = true,
    val read: Boolean = false,
    val imgUrls: String = "",

    @ServerTimestamp
    val updatedAt: Date? = null,
    @ServerTimestamp
    val createdAt: Date? = null
    )



enum class NotificationType(name: String) {
    SYSTEM(name = "SYSTEM"),
    ORDER_NEED_RATING(name = "ORDER_NEED_RATING"),
    PROMOTION(name = "PROMOTION")
}
