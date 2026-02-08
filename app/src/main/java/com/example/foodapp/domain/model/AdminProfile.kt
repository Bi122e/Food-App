package com.example.foodapp.domain.model

data class AdminProfile(
    val uid: String = "",
    val name: String = "",
    val permissions: List<String> = emptyList()
)
