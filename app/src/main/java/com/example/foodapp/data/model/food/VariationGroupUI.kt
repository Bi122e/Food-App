//file model variationgroupui
package com.example.foodapp.data.model.food

data class VariationGroupUI(
    val id: String,
    val name: String,
    val type: VariationType,
    val items: List<VariationUI>
)

enum class VariationType {
    SINGLE, MULTI
}