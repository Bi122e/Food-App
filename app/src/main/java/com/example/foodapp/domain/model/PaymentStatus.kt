package com.example.foodapp.domain.model

enum class PaymentStatus(val label: String) {
    UNPAID("Chua thanh toan"),
    PAID("Da thanh toan"),
    REFUNDED("Da hoan tien"),
    FAILED("Thanh toan that bai");

    companion object {
        fun fromString(value: String): PaymentStatus {
            return PaymentStatus.entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: UNPAID
        }
    }
}