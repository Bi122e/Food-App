package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Cart(
    val userId: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val price: Double = 0.0,
    val deliveryFee: Int = 0,
    val restaurantName: String = "",
    val restaurantId: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null,
) {
    fun calculateSubTotalPrice(): Double {
        return cartItems.sumOf {it.getTotalPrice()}.toDouble()
    }
    fun calculateTotalPrice(): Double {
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
    fun Map<String, Set<String>>.toVariations(): List<Variation> {
        return map { (variationName, optionName) ->
            Variation(
                name = variationName,
                options = optionName.map { optionName ->
                    VariationOption(
                        name = optionName,
                        price = 0
                    )
                }
            )
        }
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