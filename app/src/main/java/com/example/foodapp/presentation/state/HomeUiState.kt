package com.example.foodapp.presentation.state

import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.Restaurant

data class HomeUiState(
    val oder: List<Order> = emptyList(),
    val restaurants: UiState<List<Restaurant>> = UiState.Loading,
    val restaurantsByCategory: UiState<List<Restaurant>> = UiState.Loading,
    val restaurantByRandom: UiState<List<Restaurant>> = UiState.Loading,
    val isLoadingMoreRandom: Boolean = false,
    val isLoadingMoreRestaurants: Boolean = false,
    val isEndReachedRestaurants: Boolean = false,
    val badgeCount: Int = 0,
)


data class HomeData(
    val oder: List<Order> = emptyList(),
    val restaurants: List<Restaurant> = emptyList(),
    val restaurantsByCategory: List<Restaurant> = emptyList(),
    val restaurantByRandom: List<Restaurant> = emptyList(),
)
