package com.example.foodapp.domain.model

data class RestaurantProfile(
    val uid: String = "",
    val restaurantName: String = "",
    val ownerName: String = "",
    val address: String = "",
    val openTime: String = "",
    val isOpen: Boolean = true
)
