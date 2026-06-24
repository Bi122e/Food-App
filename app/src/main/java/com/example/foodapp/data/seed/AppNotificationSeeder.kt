package com.example.foodapp.data.seed

import com.example.foodapp.domain.model.AppNotification
import com.example.foodapp.domain.model.NotificationType


object AppNotificationSeeder {

    val seederNotification: List<AppNotification> = (
            listOf(
                AppNotification(
                    id = "test",
                    userId = "aFn2A3Xr8bRlUp6sRVT8j9KfM9B3",
                    message = "testing",
                    name = "TEST",
                    type = NotificationType.ORDER_NEED_RATING.name,
                    payload = mapOf("order123" to "sd"),
                    active = true,
                )
            )
            )
}