package com.example.foodapp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.example.foodapp.utils.toVND
data class Restaurant(
    val restaurantId: String = "",
    val restaurantName: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val rating: Float = 0.0f,
    val totalReview: Int = 0,
    val deliveryFree: Double = 0.0,
    val minOrderAmount: Double = 0.0,
    val estimatedDeliveryTime: Int = 30,
    val isOpen: Boolean = true,
    val categories: List<String> = emptyList(),
    @ServerTimestamp
    val createAt: Timestamp? = null

) {
    constructor(): this("","","","","","","",0.0f, 0, 0.0, 0.0, 30, true, emptyList(), null)

    fun getFormattedDeliveryFree(): String {
        return if(deliveryFree > 0) "${deliveryFree.toVND()}đ" else "Miễn phí"
    }
    fun getFormattedMinOrder(): String {
        return "Đơn tối thiểu: ${minOrderAmount.toVND()}đ"
    }
    fun getTimeText(): String {
        return "${estimatedDeliveryTime} phút"
    }
    fun getRatingText(): String {
        return "$rating ($totalReview đánh giá)"
    }
}
