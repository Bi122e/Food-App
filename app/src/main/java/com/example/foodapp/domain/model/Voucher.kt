package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Voucher(
    val name: String = "",
    val id: String = "",
    val type: VoucherType = VoucherType.FIXED,
    val value: Long = 0,
    val minOrderAmount: Long = 0,
    val maxDiscount: Long? = null,
    val expiredAt: Date? = null,

    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)


enum class VoucherType{
    FIXED,
    PERCENT
}

