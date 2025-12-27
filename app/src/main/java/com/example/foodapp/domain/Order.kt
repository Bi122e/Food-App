package com.example.foodapp.domain

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Order(
    val foodId: String = "",
    val foodName: String = "",
    val restaurantId: String = "",
    val restaurant: String = "",
    val subTotal: Int = 0,
    val total: Int = 0,
    val address: String = "",
    val phone: String = "",
    val userId: String = "",
    val oderId: String = "",
    val email: String = "",
    val items: List<OrderItem> = emptyList(),
    val deliveryFee: Int = 30,
    val discountAmount: Int = 0,
    val estimatedDeliveryTime: Int = 30,
    val status: OrderStatus = OrderStatus.PENDING,
    val paymentMethod: String = "CASH",
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val notes: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val deliveredAt: Date? = null,

) {
    //calculate

    fun calculateSubTotal(): Int {
        return items.sumOf{it.getTotalPrice()}
    }
}
