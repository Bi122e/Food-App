//file model variationUI
package com.example.foodapp.data.model.food

data class VariationUI(
    val id: String,
    val name: String,
    val price: Int,
    val isSelected: Boolean = false
)