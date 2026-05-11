package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.User

data class OrderUiState(
    val order: List<Order> = emptyList(),
    val cart: Cart? = null,
    val foods: List<Food> = emptyList(),
    val user: User? = null,
    val restaurant: Restaurant? = null,
    val orderStatus: Map<String, OrderStatus> = emptyMap(),
)