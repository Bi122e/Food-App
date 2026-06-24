package com.example.foodapp.domain.model

data class DriverSnapshot(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val profileUrl: String = "",
    val vehicleType: String = "",
    val licensePlate: String = "",
    val isAvailable: Boolean = true
)