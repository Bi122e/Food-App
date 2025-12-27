//file model cart
package com.example.foodapp.data.model.cart

data class Cart(
    val userId: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val restaurantName: String = "",
    val restaurantId: String = "",
    val deliveryFee: Int = 0,


) {

    fun calculateSubTotal(): Int {
        return cartItems.sumOf { it.getTotalPrice() }
    }
    fun calculateTotal(): Int {
        return calculateSubTotal() + deliveryFee
    }
    fun isEmpty(): Boolean {
        return cartItems.isEmpty()
    }
    fun canAddFromRestaurant(newRestaurantId: String): Boolean {
        return isEmpty() || restaurantId == newRestaurantId
    }
}