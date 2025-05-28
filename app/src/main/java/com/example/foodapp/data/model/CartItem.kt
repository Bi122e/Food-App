package com.example.foodapp.data.model

data class CartItem(
    val userId: String = "",
    val foodId: String = "",
    val foodName: String = "",
    val foodImageUrl: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0,
    val address: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
) {
    constructor(): this("", "", "", "", 1, 0.0, "", "", "")

    fun getTotalPrice(): Double {
        return price * quantity
    }
}
