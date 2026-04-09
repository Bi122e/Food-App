package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.VariationOption

data class CartUiState(
    val cart: Cart? = null,
    val restaurant: Restaurant? = null,
    val loadingFoodIds: Set<String> = emptySet(),
    val  currentEditingItem: ActiveCartItemUi? = null,
    val error: String? = null,
 )

data class ActiveCartItemUi(
    val food: Food,
    val quantity: Int = 1,
    val variations: Map<String, List<VariationOption>> = emptyMap(),
    val note: String = ""
)

