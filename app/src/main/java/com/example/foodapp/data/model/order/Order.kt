////FILE model order
//package com.example.foodapp.data.model.order
//
// import com.google.firebase.firestore.ServerTimestamp
// import java.util.Date
//
//data class Order(
//    val restaurantName: String = "",
//    val restaurantId: String = "",
//    val address: String = "",
//    val deliveryFee: Int = 30,
//    val estimatedDeliveryTime: Int = 30,
//    val items: List<OrderItem> = emptyList(),
//    val phoneNumber: String = "",
//    val email: String = "",
//    val promoCode: String = "",
//    val discountAmount: Int = 0,
//    val status: OrderStatus = OrderStatus.PENDING,
//    val notes: String = "",
//    @ServerTimestamp val createdAt: Date? = null,
//    val updatedAt: Date? = null,
//) {
//
//
//    enum class OrderStatus(val label: String) {
//        PENDING("CHO XAC NHAN"),
//        CONFIRMED("XAC NHAN"),
//        PREPARING("DANG CHUAN BI"),
//        DELIVERING("DANG GIAO"),
//        DELIVERED("DA GIAO HANG"),
//        CANCELLED("DA HUY"),
//
//    }
//
//    fun calculateSubTotal(): Int {
//        return items.sumOf { it.quantity * it.price }
//    }
//
//    fun calculateTotal(): Int {
//        return calculateSubTotal() + deliveryFee - discountAmount
//    }
//
//}