package com.example.foodapp.domain.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Cart(
    val userId: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val totalPrice: Long = 0,
    val price: Long = 0,
    val deliveryFee: Long = 0,
    val discountAmount: Long = 0,
    val voucher: Voucher? = null,
    val restaurantName: String = "",
    val restaurantId: String = "",
//    val note
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null,
) {
    @Exclude
    fun calculateSubTotalPrice(): Long {
        return cartItems.sumOf {it.getTotalPrice()}
    }
    @Exclude
    fun calculateTotalPrice(): Long {
//        return calculateSubTotalPrice() + deliveryFee - calculateDiscount()
        return maxOf(
            0,
            calculateSubTotalPrice() + deliveryFee - calculateDiscount()
        )
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
                cartItems.any {it.validate()} //nen thay = any
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
    fun calculateDiscount(): Long {
        val v = voucher ?: return 0
        val subTotal = calculateSubTotalPrice()
        if (subTotal < v.minOrderAmount) return 0

        val rawDiscount = when (v.type) {
            VoucherType.FIXED -> {
                v.value
            }
            VoucherType.PERCENT -> {
//                subTotal * (v.value / 100)
                subTotal * v.value / 100
            }
        }

        //max = 10
        return if (v.maxDiscount != null) { //nếu ko có tối đa thì lấy số nhỏ nhất
            minOf(rawDiscount, v.maxDiscount)
        } else rawDiscount
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