//file model checkoutui
package com.example.foodapp.data.model.checkout

import com.example.foodapp.data.model.cart.CartUI

data class CheckoutUI(
    val userName: String = "",
    val restaurantName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val address: String = "",
    val paymentMethod: List<PaymentMethodUI> = emptyList(),
    val deliveryFee: Int = 0,
    val price: Int = 0,
    val discountAmount: Int = 0,
    val subTotal: Int = 0,
    val total: Int = 0,
    val promo: String = "",
    val notes: String = "",
    val cartItems: List<CartUI> = emptyList()
)
