package com.example.foodapp.core.utils

import com.example.foodapp.presentation.extentions.toVND
import java.text.NumberFormat
import java.util.Locale

fun Long.toVND(): String {
    val format = NumberFormat.getInstance(Locale("vi", "VN"))
    return "${format.format(this)}đ"
}

fun Int.toVND(): String = this.toDouble().toVND()

