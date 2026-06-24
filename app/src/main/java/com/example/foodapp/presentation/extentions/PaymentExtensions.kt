package com.example.foodapp.presentation.extentions

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.foodapp.R
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray100

@Composable
fun SelectPayment(paymentMethod: PaymentMethod) {
    when (paymentMethod) {
        PaymentMethod.CASH -> {
            Icon(
                painter = painterResource(R.drawable.ic_cash),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified,
            )
        }

        PaymentMethod.ZALO -> {
            Icon(
                painter = painterResource(R.drawable.ic_zalo_pay),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified

            )
        }

        PaymentMethod.MOMO -> {
            Icon(
                painter = painterResource(R.drawable.ic_momo_resize),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified


            )
        }

        PaymentMethod.VNPAY -> {
            Icon(
                painter = painterResource(R.drawable.ic_vnpay),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified


            )
        }
    }
}

//  PENDING("Pending", "Chờ xác nhận"),
//    CONFIRMED("Confirmed", "Đã xác nhận"),
//    PREPARING("Preparing", "Đang làm món"),
//    DELIVERING("Delivering", "Đang giao"),
//    DELIVERED("Delivered", "Đã giao"),
//    CANCELLED("Cancelled", "Đã hủy");
fun getTextFromOrderStatus(orderStatus: OrderStatus): String {
    return when (orderStatus) {
        OrderStatus.PENDING -> {
            "Chờ xác nhận"
        }

        OrderStatus.CONFIRMED -> {
            "Đã xác nhận"
        }

        OrderStatus.PREPARING -> {
            "Đang làm món"
        }

        OrderStatus.DELIVERING -> {
            "Đang giao món"
        }

        OrderStatus.DELIVERED -> {
            "Đã giao món"
        }

        OrderStatus.CANCELLED -> {
            "Đã hủy"
        }
    }
}

fun isPassed(
    current: OrderStatus,
    target: OrderStatus
): Boolean {

    return current.ordinal >= target.ordinal
}

fun changeBgStatus(
    current: OrderStatus,
    target: OrderStatus
): Color {
    return if (current.ordinal >= target.ordinal) {
        Blue1
    } else {
        Gray100
    }
}

fun changeIconStatus(
    current: OrderStatus,
    target: OrderStatus
): Color {
    return if (current.ordinal >= target.ordinal) {
        Color.White
    } else {
        Color.Black
    }
}

fun changeLineStatus(
    current: OrderStatus,
    target: OrderStatus
): Color {
    return if (current.ordinal >= target.ordinal) {
        Blue1
    } else {
        Gray100
    }
}