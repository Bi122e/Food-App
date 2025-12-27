////file ordermapper.kt
//package com.example.foodapp.data.mapper
//
//import com.example.foodapp.data.model.order.Order
//import com.example.foodapp.data.model.order.OrderUI
//import com.example.foodapp.utils.formatToDateText
//
//
//fun Order.toOrderUI(
//    userName: String,
//    userId: String,
////    totalPrice: Int,
//    orderStatus: Order.OrderStatus,
//    restaurantUrl: String,
//): OrderUI {
//    return OrderUI(
//        userName = userName,
//        userId = userId,
//        restaurantName = restaurantName,
//        phoneNumber = phoneNumber,
//        address = address,
//        items = items,
//        discountAmount = discountAmount,
//        totalPrice = calculateTotal(),
//        statusText = orderStatus.label,
//        restaurantUrl = restaurantUrl,
//        formattedTime = createdAt?.formatToDateText() ?: ""
//
//    )
//}
//
