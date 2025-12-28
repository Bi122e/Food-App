package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Restaurant(
    val restaurantName: String = "",
    val restaurantId: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val description: String = "",
    val imgUrl: String,
    val isOpen: Boolean = true,
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val estimatedDeliveryTime: Int = 30,
    val minOrderAmount: Double = 0.0,

    val deliveryFee: Int = 30,
    val openingHours: String = "",
    val closingHours: String = "",
    val categories: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    @ServerTimestamp
    val updatedAt: Date? = null,
    val createdAt: Date? = null
) {
    fun getAverageRating(): Double {
        if (reviews < 0) return 0.0
        return (reviews / rating).coerceIn(0.0, 5.0)
    }

    fun calculateDistance(userLat: Double, userLng: Double): Double {
        if (!hasLocation()) return 0.0

        val earthRadius = 6371.0
        val dLat = Math.toRadians(userLat - latitude)
        val dLng = Math.toRadians(userLng - longitude)

            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(
                        (Math.toRadians(latitude)) *
                                cos(Math.toRadians(userLat)) *
                                sin(dLng / 2) * sin(dLng / 2)
                    )
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    fun hasLocation(): Boolean {
        return latitude != 0.0 && longitude != 0.0
    }

    fun isValid(): Boolean {
        return restaurantName.isNotEmpty() &&
                restaurantId.isNotEmpty() &&
                estimatedDeliveryTime > 0 &&
                address.isNotEmpty() &&
                deliveryFee >= 0 &&
                phoneNumber.isNotEmpty()
    }

    fun canOrder(newRestaurantId: String): Boolean {
        return  restaurantId == newRestaurantId
    }

    fun isPopular(): Boolean {
        return rating >= 5 && reviews >= 5;
    }

    fun isHighlyRated(): Boolean {
        return rating >= 10 && reviews >= 10;
    }

    fun isDeliveryFree(): Boolean {
        return deliveryFee == 0;
    }

    fun canOrder(orderAmount: Double): Boolean {
        return isOpen && orderAmount >= minOrderAmount
    }

    fun isNearby(userLat: Double, userLng: Double): Boolean {
        val distance = calculateDistance(userLat, userLng)
        return distance > 0 && distance < 2.0
    }

    fun addReview(newRating: Double): Restaurant {
        require(rating in 1.0..5.0) {"Rating phai trong khoang 1 den 5"}
        return copy(reviews = reviews + 1, updatedAt = Date(), rating =  rating + newRating)
    }
}
