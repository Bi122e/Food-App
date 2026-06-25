package com.example.foodapp.ui.fakeData

import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Restaurant

object PreviewDataRestaurant {

    val restaurant =
        Restaurant(
            restaurantId = "resId",
            restaurantName = "Pizza Hurt",
            imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_domino_l1ney6.png",
            address = "abc/12 abc",
            totalReview = 12,
            rating = 3.3,
            isOpen = true,
            minOrderAmount = 2.3,
        )
    val restaurants = listOf(
        Restaurant(
            restaurantId = "resId",
            restaurantName = "Pizza Hurt",
            imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_domino_l1ney6.png",
            address = "abc/12 abc",
            totalReview = 12,
            rating = 3.3,
            isOpen = true,
            minOrderAmount = 2.3,
        ),
        Restaurant(
            restaurantId = "resId",
            restaurantName = "nha hang abc",
            imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_domino_l1ney6.png",
            address = "abc/12 abc",
            totalReview = 12,
            rating = 3.3,
            isOpen = true,
            minOrderAmount = 2.3,
        ),
        Restaurant(
            restaurantId = "resId",
            restaurantName = "nha hang akbc",
            imageUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1749103438/logo_domino_l1ney6.png",
            address = "abc/12 abc",
            totalReview = 12,
            rating = 3.3,
            isOpen = true,
            minOrderAmount = 2.3,
        ),

    )

    val restaurantState=  UiState.Success(restaurants)
}