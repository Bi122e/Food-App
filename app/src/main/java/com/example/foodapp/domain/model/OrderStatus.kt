package com.example.foodapp.domain.model

enum class OrderStatus (
    val label: String,
    val vietnameseLabel: String
) {
    PENDING("Pending", "Chờ xác nhận đơn hàng"),
    CONFIRMED("Confirmed", "Đã xác nhận đơn hàng"),
    PREPARING("Preparing", "Đang làm món"),
    DELIVERING("Delivering", "Đang giao món"),
    DELIVERED("Delivered", "Đã giao món"),
    CANCELLED("Cancelled", "Đã hủy món");


    fun getNextStatus(): OrderStatus? {
        return when(this) {
            PENDING -> CONFIRMED
            CONFIRMED -> PREPARING
            PREPARING -> DELIVERING
            DELIVERING -> DELIVERED
            DELIVERED, CANCELLED -> null
        }
    }

    fun canTransitionTo(newStatus: OrderStatus): Boolean {
        return when (this) {
            PENDING -> newStatus in listOf(CONFIRMED, CANCELLED)
            CONFIRMED -> newStatus in listOf(PREPARING, CANCELLED)
            PREPARING -> newStatus in listOf(DELIVERING, CANCELLED)
            DELIVERING -> newStatus == DELIVERED
            DELIVERED, CANCELLED -> false
        }
    }

    fun isFinal(): Boolean {
        return this in listOf(DELIVERED, CANCELLED)
    }

    companion object {
        fun fromString(value: String): OrderStatus {
            return entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: PENDING
        }
    }
}
 
