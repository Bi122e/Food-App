package com.example.foodapp.domain.model

enum class PaymentMethod(
    val displayName: String,
    val description: String,
    val isOnlinePayment: Boolean
) {
    CASH(
        "Tien mat",
        "Thanh toan khi nhap hang",
        false
    ),
     ZALO(
         "ZaloPay",
         "Vi dien tu ZaloPay",
         true
     ),

    MOMO(
        "Vi MoMo",
        "Thanh toan qua vi MoMo",
        true
    ),

    VNPAY(
        "VNPAY",
        "Thanh toan qua VNPAY",
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