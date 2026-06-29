package com.example.foodapp.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun Date.toMessageTime(): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(this)
}

fun Date.toChatTime(): String {
    val diff = System.currentTimeMillis() - this.time
    val oneDay = 24 * 60 * 60 * 1000L

    return if (diff < oneDay) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
    } else {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this)
    }
}

fun Date.toChatDate(): String {
    return SimpleDateFormat(
        "HH:mm dd/MM/yyyy",
        Locale.getDefault()
    ).format(this)
}