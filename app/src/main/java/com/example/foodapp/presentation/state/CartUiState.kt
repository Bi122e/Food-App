package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.VariationOption

data class CartUiState(
    val cart: Cart? = null,
    val isLoading: Boolean = false,
    val error: Boolean = false,

    val activeItem: Map<String, ActiveCartItemUi> = emptyMap(),

    val selectedQuantity: Int = 1,
    val selectedVariations: Map<String, List<VariationOption>> = emptyMap(),
    val specialInstructions: String = ""
)

data class ActiveCartItemUi(
    val food: Food,
    val quantity: Int = 1,
    val variations: Map<String, List<VariationOption>> = emptyMap()
)