package com.example.foodapp.presentation.state

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
 import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.RestaurantPreview

data class PreviewRestaurantUiState(
    val restaurants: UiState<Restaurant> = UiState.Loading,
    val previews: UiState<List<RestaurantPreview>> = UiState.Loading,
)




