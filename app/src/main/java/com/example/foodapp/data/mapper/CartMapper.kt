////file cartmapper.kt
//
//package com.example.foodapp.data.mapper
//
//import com.example.foodapp.data.model.cart.Cart
//import com.example.foodapp.data.model.cart.CartItem
//import com.example.foodapp.data.model.cart.CartUI
//import com.example.foodapp.data.model.order.Order
//import com.example.foodapp.data.model.order.OrderItem
//
//fun CartItem.toOrderItem(
//    notes: String = ""
//): OrderItem {
//    return OrderItem(
//        foodId = foodId,
//        foodName = foodName,
//        foodImgUrl = foodImageUrl,
//        quantity = quantity,
//        price = price,
//        variation = variation,
//        notes = notes
//    )
//}
//
//fun Cart.toOrder(
//    address: String = "",
//    estimatedDeliveryTime: Int = 0,
//    phoneNumber: String = "",
//    email: String = "",
//    promoCode: String = "",
//    discountAmount: Int = 0,
//    notes: String = "",
//): Order {
//    return Order(
//        restaurantName = restaurantName,
//        restaurantId = restaurantId,
//        address = address,
//        deliveryFee = deliveryFee,
//        estimatedDeliveryTime = estimatedDeliveryTime,
//        items = cartItems.map { it.toOrderItem() },
//        phoneNumber = phoneNumber,
//        email = email,
//        promoCode = promoCode,
//        discountAmount = discountAmount,
//        status = Order.OrderStatus.PENDING,
//        notes = notes,
//
//        )
//}
//
//fun Cart.toCartUI(): CartUI {
//    val subTotal = calculateSubTotal()
//    val quantity = cartItems.sumOf { it.quantity }
//    return CartUI(
//        restaurantName = restaurantName,
//        subTotal = subTotal,
//        total = calculateTotal(),
//        quantity = quantity,
//    )
//}
