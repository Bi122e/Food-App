package com.example.foodapp.data.model

data class FoodCategory(
    val id: String,
    val name: String,
    val iconRes: Int,
    var isSelected: Boolean = false,
)
