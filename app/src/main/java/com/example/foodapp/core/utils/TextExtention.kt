package com.example.foodapp.core.utils

fun String.limit(max: Int): String {
    return if (length > max) take(max) + "..." else this
}

fun String.displayAddress(): String =
    when {
        isBlank() -> "Chọn địa chỉ"
        length > 5 -> take(5) + "..."
        else -> this
    }