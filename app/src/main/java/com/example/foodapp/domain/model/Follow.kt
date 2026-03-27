package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Follow(
    val id: String = "",
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
        fun create(id: String,userId: String, restaurantId: String): Follow {
            return Follow(id, userId, restaurantId, Date())
        }
    }
}

data class Favorite(
    val favoriteId: String = "",
    val foodId: String = "",
    val foodName: String = "",
    val userId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    var isValid: Boolean = false,
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    fun checkIsValid(): Boolean {
        return foodId.isNotEmpty() &&
                foodName.isNotEmpty() &&
                restaurantId.isNotEmpty() &&
                restaurantName.isNotEmpty()
    }

    companion object {

        fun create(foodId: String, foodName: String, restaurantId: String, restaurantName: String, userId: String, id: String, isValid: Boolean): Favorite {
            return Favorite(favoriteId = foodId, foodName = foodName, restaurantId = restaurantId, restaurantName =  restaurantName, foodId = id, userId = userId, isValid = isValid)
        }
    }
}
