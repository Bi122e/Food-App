package com.example.foodapp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.example.foodapp.utils.toVND
data class Cart(
    val userId: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val restaurantName: String = "",
    val restaurantId: String = "",
    @ServerTimestamp
    val updateAt: Timestamp? = null
) {
    constructor(): this("", emptyList(), "", "", null)

    fun getTotalAmount(): Double {
        return cartItems.sumOf {
            it.getTotalPrice()
        }
    }
    fun getTotalItem(): Int {
        return cartItems.sumOf {
            it.quantity
        }
    }
    fun getFormattedTottalAmount(): String {
        return getTotalAmount().toVND() + "đ"
    }
    fun isEmty(): Boolean {
        return cartItems.isEmpty()
    }
    fun canAddFromRestaurant(newRestaurantId: String): Boolean {
        return isEmty() || restaurantId == newRestaurantId
    }
}
