package com.example.foodapp.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.example.foodapp.utils.toVND
data class Checkout(
    val userId: String = "",
    val userName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val cartItems: List<CartItem> = emptyList(),
    val paymentMethod: String = PaymentMethod.CASH.name,
    val subtotals: Double = 0.0,
    val deliveryFree: Double = 0.0,
    val discountAmount: Double = 0.0, //số tiền giảm giá
    val totalAmount: Double = 0.0,
    val note: String = "",
    val estimatedDeliveryTime: Int = 30,
    val promoCode: String = "",
    @ServerTimestamp
    val createAt: Timestamp? = null
) {
    constructor(): this("", "", "", "", emptyList())
    fun calculateTotal(): Double {
        return totalAmount + deliveryFree - discountAmount
    }
    fun getFormattedSubtotals(): String {
        return "${subtotals.toVND()}đ"
    }
    fun getFormattedDeliveryFree(): String {
        return if(deliveryFree > 0)"${deliveryFree.toVND()}đ" else "Miễn phí"
    }
    fun getFormattedDiscountAmount(): String {
        return if(discountAmount > 0) "${discountAmount.toVND()}đ" else ""
    }
    fun getFormattedTotalAmount(): String {
        return "${totalAmount.toVND()}.đ"
    }
    fun getFormattedDeliveryText(): String {
        return "${estimatedDeliveryTime.toVND()}đ"
    }

}
enum class PaymentMethod(val displayName: String) {
    CASH("Tiền mặt"),
    ZALOPAY("Zalo Pay"),
    VNPAY("VNPay"),
    MOMO("Ví MoMo"),
    CREDIT_CARD("Thẻ tín dụng");

    //Hàm tiện ích, trả về 1 chuỗi string thành một giá trị payment tương ứng
    companion object {
        fun fromString(value: String): PaymentMethod  {
            return values().find { it.name == value } ?:CASH
        }
    }
}