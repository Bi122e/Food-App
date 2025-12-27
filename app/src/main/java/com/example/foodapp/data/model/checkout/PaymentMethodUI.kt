//file model paymentmethodui
package com.example.foodapp.data.model.checkout

data class PaymentMethodUI(
    val icon: Int,
    val title: String,
    val isSelected: Boolean,
    val type: PaymentMethodType
)
