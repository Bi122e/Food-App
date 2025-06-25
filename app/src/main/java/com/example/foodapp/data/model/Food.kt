package com.example.foodapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

data class Food(
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val categoryId: String = "",
    val isAvailable: Boolean = true,
    val restaurantId: String = "",
    @ServerTimestamp
    val createAt: Date? = null
) {
    constructor() : this("", "", 0.0, "", "", true, "", null)
}