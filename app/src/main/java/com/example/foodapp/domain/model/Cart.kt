package com.example.foodapp.domain.model

data class Cart(
    val id: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val price: Double = 0.0,
    val deliveryFee: Int = 0,
    val restaurantId: String = "",
) {
    fun calculateSubTotalPrice(): Int {
        return cartItems.sumOf {it.price * it.quantity}
    }
    fun calculateTotalPrice(): Int {
        return calculateSubTotalPrice() + deliveryFee
    }
    fun getTotalItemCount(): Int {
        return cartItems.sumOf {it.quantity}
    }
    fun isEmpty(): Boolean {
        return cartItems.isEmpty()
    }


    fun isValid(): Boolean {
        return cartItems.isNotEmpty() &&
                restaurantId.isNotEmpty() &&
                deliveryFee >= 0 &&
                cartItems.all {it.isValid()}
    }

    //business rules
    //rule: only one restaurant per cart
    fun canAddFromRestaurant(newRestaurantId: String): Boolean {
        return isEmpty() || restaurantId == newRestaurantId
    }

    //exams: subtotal > 30
    fun meetsMinimumOrder(minAmount: Int): Boolean {
        return calculateSubTotalPrice() >= minAmount
    }

    fun canCheckout(): Boolean {
        return cartItems.isNotEmpty() && calculateTotalPrice() > 0 && isValid()
    }
}