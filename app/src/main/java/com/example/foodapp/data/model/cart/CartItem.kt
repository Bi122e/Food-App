//file model cartitem
package com.example.foodapp.data.model.cart

 import com.example.foodapp.domain.Variation

data class CartItem(
    val foodId: String = "",
    val foodName: String = "",
    val foodImageUrl: String = "",
    val quantity: Int = 1,
    val price: Int = 0,
    val note: String = "",
    val variation: List<Variation> = emptyList(),
) {

    fun getTotalPrice(): Int {
        return price * quantity
    }

    fun isValid(): Boolean {
        return foodId.isNotEmpty() && quantity >= 1 && price >= 0
    }

    fun updateQuantity(newQuantity: Int): CartItem {
        return if (newQuantity > 1) {
            copy(quantity = newQuantity)
        } else this
    }
}