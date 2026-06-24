package com.example.foodapp.presentation.extentions

import com.example.foodapp.core.UiState
import com.example.foodapp.presentation.state.HomeData
import com.example.foodapp.presentation.state.HomeUiState


val HomeUiState.isLoading: Boolean
    get() = listOf(
        restaurants,
        restaurantsByCategory,
        restaurantByRandom
    ).any { it is UiState.Loading }

val HomeUiState.isSuccess: Boolean
    get() = listOf(
        restaurants,
        restaurantsByCategory,
        restaurantByRandom
    ).all { it is UiState.Success }


val HomeUiState.homeDataOrNull: HomeData?
    get()  {
        val restaurants =
            (restaurants as? UiState.Success)?.data
                ?: return null

        val category =
            (restaurantsByCategory as? UiState.Success)?.data
                ?: return null

        val random =
            (restaurantByRandom as? UiState.Success)?.data
                ?: return null
        return HomeData(
            restaurants = restaurants,
            restaurantsByCategory = category,
            restaurantByRandom = random
        )
    }