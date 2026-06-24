package com.example.foodapp.presentation.extentions

import com.example.foodapp.domain.model.AppNotification
import com.example.foodapp.domain.model.NotificationType
import com.example.foodapp.domain.model.Order


fun Order.toAppNotification(): AppNotification {
    return AppNotification(
        id = "",
        userId = this.userId,
        name = "Đánh giá nhà hàng",
        message = "Hãy đánh giá nhà hàng để nhận ưu đãi!",
        type = NotificationType.ORDER_NEED_RATING.name,
        payload = mapOf("orderId" to this.orderId),
        active = true,
        read = false,
        imgUrls = "",
    )
}