package com.example.foodapp.domain.model

data class DriverProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val vehicleType: String = "",
    val licensePlate: String = "",
    val isAvailable: Boolean = true
)
