////file paymentmethodtyoe\
//package com.example.foodapp.data.mapper
//
//import com.example.foodapp.data.model.checkout.PaymentMethodType
//import com.example.foodapp.data.model.checkout.PaymentMethodUI
//import com.example.foodapp.R
//
//fun PaymentMethodType.toPaymentMethodTypeUI(
//    isSelected: PaymentMethodType
//): PaymentMethodUI {
//    val icon = when (this) {
//        PaymentMethodType.MOMO -> R.drawable.ic_MOMO
//        PaymentMethodType.VNPAY -> R.drawable.ic_VNPAY
//        PaymentMethodType.CREDIT_CARD -> R.drawable.ic_CREDIT
//        PaymentMethodType.ZALOPAY -> R.drawable.ic_ZALOPAY
//        PaymentMethodType.CASH -> R.drawable.ic_CASH
//    }
//    return PaymentMethodUI(
//        icon =  icon,
//        title = displayName,
//        isSelected = this == isSelected,
//        type = this
//    )
//    //khi người dùng click chọn nó sẽ gửi type.name
//}