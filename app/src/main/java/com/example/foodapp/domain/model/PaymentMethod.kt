package com.example.foodapp.domain.model

import com.example.foodapp.R

enum class PaymentMethod(
    val displayName: String,
    val description: String,
    val iconRes: Int,
    val isOnlinePayment: Boolean
) {
    CASH(
        "Tiền mặt",
        "Thanh toan khi nhap hang",
        iconRes = R.drawable.ic_cash,
        false
    ),
     ZALO(
         "ZaloPay",
         "Vi dien tu ZaloPay",
         R.drawable.ic_zalo_pay,
         true
     ),

    MOMO(
        "Ví MoMo",
        "Thanh toan qua vi MoMo",
        R.drawable.ic_momo_resize,
        true
    ),

    VNPAY(
        "VNPAY",
        "Thanh toan qua VNPAY",
        R.drawable.ic_vnpay,
        true
    );

    fun requiresOnlineProcessing(): Boolean = isOnlinePayment

    companion object {

        //parse from string
        fun fromString(value: String): PaymentMethod {
            return entries.find {
                it.name.equals(value, ignoreCase = true)
            } ?: CASH
        }

        fun getAll(): List<PaymentMethod> = entries

        fun getCashOnlyDelivery(): PaymentMethod = CASH

        fun getOnlinePayments(): List<PaymentMethod> {
            return entries.filter{it.isOnlinePayment}
        }
    }
}