package com.example.foodapp.ui.fakeData
import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Category
import com.example.foodapp.domain.model.CategoryType
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Promotion
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.VariationOption
import java.util.Date

object PreviewData {

    val sizeVariation = Variation(
        id = "size",
        name = "Size",
        description = "Chỉ được chọn một kích thước",
        type = Variation.VariationType.SINGLE,
        required = true,
        minSelection = 1,
        maxSelection = 1,
        options = listOf(
            VariationOption(
                id = "sizeS",
                name = "Size S",
                price = 35000,
                available = true
            ),
            VariationOption(
                id = "sizeM",
                name = "Size M",
                price = 45000,
                available = true
            ),
            VariationOption(
                id = "sizeL",
                name = "Size L",
                price = 55000,
                available = true
            )
        )
    )

    val pizzaBongCai = Food(
        foodId = "pizza_bong_cai",
        name = "Pizza bông cải",
        nameLower = "pizza bông cải",
        description = "Pizza bông cải xanh, ít chất béo và nhiều chất xơ.",
        price = 54000,
        imgUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751530024/pizza0_tysmwq.jpg",
        categoriesId = listOf("pizza") ,
        restaurantId = "pizza_hurt",
        averageRating = 4.2,
        reviewCount = 3,
        totalRating = 12.6,
        calories = 266,
        foodTime = 20,
        ingredient = "Bông cải xanh, Mozzarella, Ớt chuông, Hành tây",
        available = true,
        isHighlyRatedFlag = false,
        popular = false,
        valid = true,
        createdAt = Date(),
        variations = listOf(sizeVariation)
    )





    val cakeCategory = Category(
        categoryId = "banh-cake",
        name = "Bánh cake",
        slug = "banh-cake",
        iconUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1751443004/cake_rfwgzb.png",
        order = 0,
        type = CategoryType.NORMAL
    )


    val summerPromotion = Promotion(
        promoId = "promo1_summer_2025",
        promoUrl = "https://res.cloudinary.com/dgbz1qem7/image/upload/v1757652093/banner_1_kldppy.png",
        discountPercent = 4,
        restaurantId = "pizza_hurt",
        foodNameIds = listOf(
            "pizza_4_cheese",
            "pizza_bong_cai",
            "pizza_hai_san",
            "pizza_pepperoni",
            "pizza_xuc_xich"
        ),
        startDate = Date(),
        endDate = Date()
    )


    val foodState = UiState.Success(listOf(pizzaBongCai, pizzaBongCai, pizzaBongCai, pizzaBongCai, pizzaBongCai, pizzaBongCai))
    val categoryState = UiState.Success(listOf(cakeCategory, cakeCategory, cakeCategory, cakeCategory, cakeCategory,cakeCategory))
    val promotionState = UiState.Success(listOf(summerPromotion, summerPromotion, summerPromotion, summerPromotion))
}