package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.User

data class CheckoutUiState(
    val user: User? = null,
    val cart: Cart? = null,
    val restaurant: Restaurant? = null,
    val error: String? = null,
    val loading: Set<String> = emptySet(),
    val paymentMethod: PaymentMethod = PaymentMethod.CASH
)
