package com.example.foodapp.domain.model

data class SelectedOption(
    val optionId: String = "",
    val optionName: String = "",
    val variationId: String = "",
    val variationName: String = "",
    val price: Long = 0,
)
