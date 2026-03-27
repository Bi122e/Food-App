package com.example.foodapp.ui.preview

import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Food

object PreviewDataFood {

    val food = Food(
        name = "ga ran",
        nameLower = "",
        foodId = "food_id",
        description = "null",
        price = 13000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529947/pizza5_kom9ey.jpg"
    )
    val listFood = listOf(
        Food(
            name = "ga ran",
            nameLower = "",
            foodId = "food_id",
            description = "null",
            price = 13000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529947/pizza5_kom9ey.jpg"
        ),
        Food(
            name = "ga ran",
            nameLower = "",
            foodId = "food_id",
            description = "null",
            price = 13000,
            imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529947/pizza5_kom9ey.jpg"
        )
    )

    val foodState = UiState.Success(listFood)

}