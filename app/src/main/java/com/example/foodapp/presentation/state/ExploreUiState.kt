package com.example.foodapp.presentation.state

import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Restaurant


data class ExploreUiState(

    val restaurants: UiState<List<Restaurant>> = UiState.Idle,
    val isEndReachedRestaurants: Boolean = false,
    val isLoadingRestaurant: Boolean = false,

    val restaurantSuggestion: UiState<List<Restaurant>> = UiState.Idle,
    val text: String = "",
    val suggestionTag: List<String> = listOf(
        "Gà rán", "Trà sữa", "Bún đậu", "Bún", "cơm chiên", "chè"
    )
)