package com.example.foodapp.domain.model

import com.google.firebase.firestore.Exclude
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
    @Exclude
    fun calculateSubTotalPrice(): Double {
        return cartItems.sumOf {it.getTotalPrice()}.toDouble()
    }
    @Exclude
    fun calculateTotalPrice(): Double {
        return calculateSubTotalPrice() + deliveryFee
    }
    @Exclude
    fun getTotalItemCount(): Int {
        return cartItems.sumOf {it.quantity}
    }
    @Exclude
    fun isEmpty(): Boolean {
        return cartItems.isEmpty()
    }

    @Exclude
    fun checkValid(): Boolean {
        return cartItems.isNotEmpty() &&
                restaurantId.isNotEmpty() &&
                deliveryFee >= 0 &&
                cartItems.all {it.validate()}
    }
    @Exclude
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

    @Exclude
    fun getQuantityOf(key: String): Int {
        val quantity = cartItems.filter { it.foodId == key }.sumOf { item -> item.quantity }
        return quantity
    }

    //business rules
    //rule: only one restaurant per cart
    fun canAddFromRestaurant(newRestaurantId: String): Boolean {
        return isEmpty() || restaurantId == newRestaurantId
    }
    //exams: subtotal > 30
    @Exclude
    fun meetsMinimumOrder(minAmount: Int): Boolean {
        return calculateSubTotalPrice() >= minAmount
    }
    @Exclude
    fun canCheckout(): Boolean {
        return cartItems.isNotEmpty() && calculateTotalPrice() > 0 && checkValid()
    }


}