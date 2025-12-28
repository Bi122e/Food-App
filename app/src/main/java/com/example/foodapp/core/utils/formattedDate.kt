package com.example.foodapp.core.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date?.formatToDateText(): String {
    if (this == null) return ""

    val now = Calendar.getInstance()
    val date = Calendar.getInstance().apply { time = this@formatToDateText }

    return when {
        now.isSameDay(date) -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)
        now.isYesterday(date) -> "Hôm qua"
        else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(time)
    }
}

fun Calendar.isSameDay(other: Calendar): Boolean =
    this.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)

fun Calendar.isYesterday(other: Calendar): Boolean {
    val yesterday = this.clone() as Calendar
    yesterday.add(Calendar.DAY_OF_YEAR, -1)
    return yesterday.isSameDay(other)
}
