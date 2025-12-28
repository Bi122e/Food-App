package com.example.foodapp.domain.model

enum class OrderStatus (
    val label: String,
    val vietnameseLabel: String
) {
    PENDING("Pending", "Chờ xác nhận"),
    CONFIRMED("Confirmed", "Đã xác nhận"),
    PREPARING("Preparing", "Đang chế biến"),
    DELIVERING("Delivering", "Đang giao"),
    DELIVERED("Delivered", "Đã giao"),
    CANCELLED("Cancelled", "Đã hủy");


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
 
