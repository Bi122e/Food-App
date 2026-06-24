package com.example.foodapp.core.utils

import java.util.Date

fun Date.timeAgo(): String {
    val diff = System.currentTimeMillis() - this.time

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "Vừa xong"
        minutes < 60 -> "$minutes phút trước"
        hours < 24 -> "$hours giờ trước"
        days < 30 -> "$days ngày trước"
        days < 365 -> "${days / 30} tháng trước"
        else -> "${days / 365} năm trước"
    }
}