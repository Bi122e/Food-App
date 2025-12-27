//file model restaurant
package com.example.foodapp.data.model.restaurant

import com.example.foodapp.utils.toVND
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Restaurant(
    val restaurantId: String = "",
    val restaurantName: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val review: Int = 0,
    val totalRating: Double = 0.0,
    val deliveryFree: Int = 30,
    val minOrderAmount: Double = 0.0,
    val estimatedDeliveryTime: Int = 30,
    val isOpen: Boolean = true,
    val categories: List<String> = emptyList(),
    @ServerTimestamp
    val createAt: Timestamp? = null

) {

    fun getFormattedDeliveryFree(): String {
        return if(deliveryFree > 0) "${deliveryFree.toVND()}đ" else "Miễn phí"
    }
    fun getFormattedMinOrder(): String {
        return "Đơn tối thiểu: ${minOrderAmount.toVND()}đ"
    }
    fun getTimeText(): String {
        return "${estimatedDeliveryTime} phút"
    }

    fun getAverageRating(): Double {
        return if (review > 0) totalRating.toDouble() / review else 0.0
    }
}