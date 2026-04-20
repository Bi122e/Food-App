package com.example.foodapp.ui.preview

import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.VariationOption

object PreviewDataFood {
    private fun setDefaultOption(): List<Variation> {
        val options1 = listOf(
            VariationOption("Size S", 35000, "sizeS", "", true),
            VariationOption("Size M", 45000, "sizeM", "", true),
            VariationOption("Size L", 55000, "sizeL", "", true),
        )
        val options2 = listOf(
            VariationOption("Tôm thêm", 35000, "tom", "", true),
            VariationOption("Thịt thêm", 45000, "thit", "", true),
            VariationOption("Mô mai thêm", 55000, "phomai", "", true),
        )

        val variations = listOf(
            Variation(
                "Size",
                "size",
                "Chỉ được chọn một kích thước",
                Variation.VariationType.SINGLE,
                true,
                1,
                1,
                options1
            ),
            Variation(
                "Size",
                "size",
                "nhiều lựa chọn ",
                Variation.VariationType.MULTI,
                true,
                1,
                20,
                options2
            )

        )

        return variations
    }
    val food = Food(

        name = "Pizza Pepperoni 1",
        nameLower = "pizza pepperoni 1",
        foodId = "pizza_Pepperoni_1",
        description = "Pizza Quattro Formaggi với mozzarella, cheddar, parmesan và blue cheese.",
        price = 70000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751529947/pizza5_kom9ey.jpg",
        available = true,
        reviewCount = 3,
        totalRating = 12.6,
        averageRating = 4.2,
        foodTime = 30,
        ingredient = "Phô mai Mozzarella, Cheddar, Parmesan và phô mai xanh Blue ",
        calories = 255,
        restaurantId = "pizza_hurt",
        categoryId = "pizza",
        variations = setDefaultOption()
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