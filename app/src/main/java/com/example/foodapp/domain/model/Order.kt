package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Order(
    val restaurantId: String = "",
    val restaurantName: String = "",
    val subTotal: Long = 0,
    val total: Long = 0,
    val userAddress: String = "",
    val driverAvatar: String? = "",
    val restaurantAddress: String = "",
    val userPhoneSnapshot: String = "",
    val driverPhoneSnapshot: String = "",
    val userId: String = "",
    val genderDriver: String = "",
    val ratingDriver: Double = 0.0,
    val licensePlate: String = "",
    val vehicleName: String = "",
    val vehicleColor: String = "",
    val userName: String = "",
    val orderId: String = "",
    val driverId: String? = null,
    val hasRated: Boolean = false, //notifi ap
    val ratingNotificationSent: Boolean = false, //snackbar
    val driverName: String? = null,
    val active: Boolean = true,
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
    @ServerTimestamp
    val updatedAt: Date? = null,
    @ServerTimestamp
    val deliveredAt: Date? = null,

    ) {
    //calculate

    fun isFinished(): Boolean {
        return status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED
    }

    fun getTotalPrice(): Long {
        return total + 4000L
    }

    val isDriverAssigned: Boolean
        get() = driverId != null && driverName != null

}
