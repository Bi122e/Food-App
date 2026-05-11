package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Order(
    val restaurantId: String = "",
    val restaurantName: String = "",
    val subTotal: Long = 0,
    val total: Long = 0,
    val userAddress: String = "",
    val restaurantAddress: String = "",
    val userPhoneSnapshot: String = "",
    val driverPhoneSnapshot: String = "",
    val userId: String = "",
    val userName: String = "",
    val orderId: String = "",
    val driverId: String? = null,
    val driverName: String? = null,
    val userEmail: String = "",
    val restaurantEmail: String = "",
    val driverEmail: String = "",
    val items: List<OrderItem> = emptyList(),
    val deliveryFee: Long = 30000,
    val discountAmount: Long = 0,
    val estimatedDeliveryTime: Int = 30,
    val status: OrderStatus = OrderStatus.PENDING,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val notes: String = "",
    val paymentId: String? = null,
    val cancelReason: String? = null,
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val deliveredAt: Date? = null,

    ) {
    //calculate

    fun getTotalPrice(): Long {
        return total + deliveryFee + 4000L
    }
    fun calculateTotal(): Long {
        return items.sumOf { it.getTotalPrice() }
    }
}
