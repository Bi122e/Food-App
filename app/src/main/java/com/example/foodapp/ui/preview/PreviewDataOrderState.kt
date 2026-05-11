package com.example.foodapp.ui.preview

import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderItem
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.domain.model.PaymentStatus
import com.example.foodapp.domain.model.SelectedOption

object PreviewDataOrderState {


    fun previewOrder(): Order{
       return Order(
            restaurantId = "test",
           restaurantName = "res name",
           subTotal =  12000,
           total =  15000,
            userAddress = "le thanh ton 4c/3",
           restaurantAddress = "dfdf",
            userPhoneSnapshot = "09023423",
            driverPhoneSnapshot = "20340234",
            userId = "userdf1231df",
           userName = "phat",
           orderId =  "orderId_09090",
            driverId = null,
                driverName = null,
           userEmail =  "resemail@fgdcom.v",
            restaurantEmail = "sdf@gmail.com",
            items = listOf(
                OrderItem(
                    "foodId",
                    "food name1",
                    selectedOptions = listOf(
                        SelectedOption(
                            "op1",
                            "op1 name",
                            "var1",
                            "var name",
                            12000
                        )
                    ),
                    imgUrl = "img",
                    notes = "note kfae",
                    12000,
                    23
                ),
                OrderItem(
                    "foodIDd",
                    "food name2",
                    selectedOptions = listOf(
                        SelectedOption(
                            "op1",
                            "op1 name",
                            "var1",
                            "var name",
                            12000
                        ),
                        SelectedOption(
                            "op2",
                            "op2 name",
                            "var2",
                            "var name2",
                            12000
                        )
                    ),
                    imgUrl = "img",
                    notes = "note kfae",
                    12000,
                    23
                )
            ),
           discountAmount = 0,
              deliveryFee =  30000,
            status = OrderStatus.PREPARING,
            paymentStatus = PaymentStatus.UNPAID,
            paymentMethod = PaymentMethod.CASH,
           paymentId = "",
            notes = "ko them ot",
           cancelReason =  null,

            )
    }
}