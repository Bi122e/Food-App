package com.example.foodapp.core.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.limit(max: Int): String {
    return if (length > max) take(max) + "..." else this
}

fun String.displayAddress(): String =
    when {
        isBlank() -> "Chọn địa chỉ"
        length > 5 -> take(5) + "..."
        else -> this
    }


fun Timestamp.toFormattedTime(): String {
    val date = this.toDate()

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

    val time = timeFormat.format(date)
    val day = dateFormat.format(date)

    return "$time • $day"
}

fun Date?.toFormattedTime(): String {
    if (this == null) return  ""

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

    val time = timeFormat.format(date)
    val day = dateFormat.format(date)

    return "$time • $day"

}

