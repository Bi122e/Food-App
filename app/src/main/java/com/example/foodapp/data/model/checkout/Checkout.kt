//file model checkout
package com.example.foodapp.data.model.checkout

import com.example.foodapp.data.model.cart.CartItem

data class Checkout(
//    val userId: String = "",
    val userName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val paymentMethod: PaymentMethodType = PaymentMethodType.CASH,
//    val subtotals: Double = 0.0,
    val deliveryFee: Int = 0,
    val discountAmount: Double = 0.0, //số tiền giảm giá
//    val totalAmount: Double = 0.0,
    val note: String = "",
    val estimatedDeliveryTime: Int = 30,
    val promoCode: String = "",
//    @ServerTimestamp
//    val createAt: Timestamp? = null
    //val delivery
)
enum class PaymentMethodType(val displayName: String) {
    CASH("Tiền mặt"),
    ZALOPAY("Zalo Pay"),
    VNPAY("VNPay"),
    MOMO("Ví MoMo"),
    CREDIT_CARD("Thẻ tín dụng");

    //Hàm tiện ích, trả về 1 chuỗi string thành một giá trị payment tương ứng
    companion object {
        fun fromString(value: String): PaymentMethodType  {
            return values().find { it.name == value } ?:CASH
        }
    }
}