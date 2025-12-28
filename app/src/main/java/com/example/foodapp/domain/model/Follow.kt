package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Follow(
    val userId: String = "",
    val restaurantId: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    fun isValid(): Boolean {
        return userId.isNotEmpty() && restaurantId.isNotEmpty()
    }

    companion object {
        fun create(userId: String, restaurantId: String): Follow {
            return Follow(userId, restaurantId, Date())
        }
    }
}

data class Favorite(
    val foodId: String = "",
    val foodName: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    fun isValid(): Boolean {
        return foodId.isNotEmpty() &&
                foodName.isNotEmpty() &&
                restaurantId.isNotEmpty() &&
                restaurantName.isNotEmpty()
    }

    companion object {

        fun create(foodId: String, foodName: String, restaurantId: String, restaurantName: String): Favorite {
            return Favorite(foodId, foodName, restaurantId, restaurantName, Date())
        }
    }
}
