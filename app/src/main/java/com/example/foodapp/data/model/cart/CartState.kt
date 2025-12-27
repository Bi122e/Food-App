//file cartstate
package com.example.foodapp.data.model.cart

data class CartState(
    val quantity: Int = 1,
    val restaurantId: String = "",
    val restaurantName: String = "",
    val distance: Double = 0.0,
    val items: List<CartUI> = emptyList(),
    val isSelectAll: Boolean = false,

)
