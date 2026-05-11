package com.example.foodapp.domain.mapper

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderItem
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.domain.model.PaymentStatus
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.SelectedOption
import com.example.foodapp.domain.model.User

class OrderMapper

fun toOrder(cart: Cart, user: User, restaurant: Restaurant, foods: List<Food>): Order {

    return Order(
        restaurantId = cart.restaurantId,
        restaurantName = cart.restaurantName,
        subTotal = cart.price,
        total = cart.totalPrice,
        userAddress = user.profile?.customer?.address ?: "Lỗi không tìm thấy",
        restaurantAddress = restaurant.address,
        userPhoneSnapshot = user.profile?.customer?.phone ?: "Lỗi không tìm thấy",
        userId = user.uid,
        orderId = "",
        driverPhoneSnapshot = user.profile?.driver?.phone ?: "Lỗi không tìm thấy",
        items = toOrderItem(cartItem = cart.cartItems, foods = foods),
        userEmail = user.email,
        restaurantEmail = restaurant.email,
        driverEmail = "",
        deliveryFee = restaurant.deliveryFee,
        discountAmount = cart.discountAmount,
        estimatedDeliveryTime = restaurant.estimatedDeliveryTime,
        status = OrderStatus.PENDING,
        paymentMethod = PaymentMethod.CASH,
        paymentStatus = PaymentStatus.UNPAID,
        notes = "",
        paymentId = null,
        cancelReason = null,
        createdAt = null,
        updatedAt = null
    )
}

//public constructor SelectedOption(
//public final val optionId: String = "",
//public final val optionName: String = "",
//public final val variationId: String = "",
//public final val variationName: String = "",
//public final val price: Double = 0.
fun toOrderItem(cartItem: List<CartItem>, foods: List<Food>): List<OrderItem> {
    val orderItems = cartItem.map { item ->
        OrderItem(
            foodId = item.foodId,
            foodName = item.name,
            selectedOptions = toSelectOption(item, foods),
            imgUrl = item.imgUrls,
            notes = item.notes,
            price = item.getTotalPrice(),
            quantity = item.quantity

//            variations = CartMapper.toListVariation(variation = item.variation, foods = foods)
        )
    }
    return orderItems
}

fun toSelectOption(item: CartItem, foods: List<Food>): List<SelectedOption> {
//    cartItem.mapIndexed { idx, item ->
//        val itemOption = cartItem.associate{item -> item.name to item.variation}
//        if (item.foodId == foods[idx].foodId) {
//            SelectedOption(
//                optionId = itemOption.values.
//            )
//        }
//    }

//    val selectedOptions = cartItem.flatMap {  item ->
        val food = foods.find { it.foodId == item.foodId }
        val selected = item.variation.flatMap { (variationId, options) ->
            val variation = food?.variations?.find { it.id == variationId }
            options.map { option ->
                SelectedOption(
                    optionId = option.id,
                    optionName = option.name,
                    variationId = variationId,
                    variationName = variation?.name.orEmpty(),
                    price = item.getTotalPrice()
                )
            }
        }

    return selected
}
