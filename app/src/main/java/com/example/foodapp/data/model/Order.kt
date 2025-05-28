package com.example.foodapp.data.model

import com.google.firebase.Timestamp

data class Order(

    val orderId: String = "",
    val userId: String = "",
    val items: List<Food> = emptyList(),
    val totalAmount: Double = 0.0,
    val address: String = "",
    val phoneNumber: String = "",
    val status: String = OrderStatus.PENDING.name, // pending, confirmed, delivering, delivered, cancelled
    val note: String = "",
    val createAt: Timestamp? = null
) {
    constructor(): this("", "", emptyList(), 0.0, "", "", "pending", "", null)
}
data class OrderItem(
    val foodId: String = "",
    val price: Double = 0.0,
    val foodName: String = "",
    val quantity: Int = 0,
) {
    constructor(): this("", 0.0, "", 0)
}

enum class OrderStatus(val label: String) {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Xác nhận"),
    PREPARING("Đang chuẩn bị"),
    DELIVERING("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã huỷ"),
}
