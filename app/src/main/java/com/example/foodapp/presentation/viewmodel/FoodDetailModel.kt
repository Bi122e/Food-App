package com.example.foodapp.presentation.viewmodel

import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.domain.repository.RestaurantRepositoryImpl

class FoodDetailModel(
    private val foodRepository: FoodRepository,
    private val restaurant: RestaurantRepositoryImpl
    ) {
}