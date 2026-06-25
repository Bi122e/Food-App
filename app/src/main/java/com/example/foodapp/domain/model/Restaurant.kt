package com.example.foodapp.domain.model

import com.example.foodapp.core.utils.toNormalizeSearch
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

data class Restaurant(
    val restaurantName: String = "",
    val searchName: String = restaurantName.toNormalizeSearch(),
    val ownerUid: String = "",
    val totalReview: Int = 0,
    val restaurantId: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isOpen: Boolean = true,
    val rating: Double = 0.0,
    val estimatedDeliveryTime: Int = 30,
    val minOrderAmount: Double = 0.0,
    val coverImage: String = "",
    val deliveryFee: Long = 30,
    val openingHours: String = "",
    val closingHours: String = "",
    val categories: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val ratingCount: RatingCount = RatingCount(),
    @ServerTimestamp
    val updatedAt: Date? = null,
    @ServerTimestamp
    val createdAt: Date? = null
) {

    @get:Exclude
    val reviewsList: List<Int>
        get() = listOf(ratingCount.oneStars, ratingCount.twoStars, ratingCount.threeStars, ratingCount.fourStars, ratingCount.fiveStars)

    @get:Exclude
    val totalReviews: Int
        get() = ratingCount.oneStars + ratingCount.twoStars + ratingCount.threeStars + ratingCount.fourStars + ratingCount.fiveStars

    @get:Exclude
    val totalScore: Int //trọng số
        get() = (ratingCount.oneStars * 1) + (ratingCount.twoStars * 2) + (ratingCount.threeStars * 3) + (ratingCount.fourStars * 4) + (ratingCount.fiveStars * 5)

    @get: Exclude
    val totalRating: Float
        get() {
            if (totalReviews == 0) return 0f
            return (totalScore.toFloat() / totalReviews * 10).roundToInt() /10f //lam tron chu so thap phan để tránh 3.1212121
        }


    fun getAverageRating(): Double {
        if (totalReviews < 0) return 0.0
        return (totalReview / rating).coerceIn(0.0, 5.0)
    }

    fun calculateDistance(userLat: Double, userLng: Double): Double {
        if (!hasLocation()) error("Restaurant has no location")

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

    fun isPopular(): Boolean {
        return rating >= 5 && totalReview >= 5;
    }

    fun isHighlyRated(): Boolean {
        return rating >= 10 && totalReview >= 10;
    }

    fun isDeliveryFree(): Boolean {
        return deliveryFee == 0L;
    }

    fun canOrder(orderAmount: Double): Boolean {
        return isOpen && orderAmount >= minOrderAmount
    }

    fun isNearby(userLat: Double, userLng: Double): Boolean {
        val distance = calculateDistance(userLat, userLng)
        return distance > 0 && distance < 2.0
    }

    fun addReview(newRating: Double): Restaurant {
        require(newRating in 1.0..5.0)

        val newTotalReview = totalReview + 1
        val newAvgRating = ((rating * totalReview) + newRating) / newTotalReview

        return copy(
            rating = newAvgRating,
             updatedAt = Date()
        )
    }
}

data class RatingCount(
    val oneStars: Int = 0,
    val twoStars: Int = 0,
    val threeStars: Int = 0,
    val fourStars: Int = 0,
    val fiveStars: Int = 0,
)
