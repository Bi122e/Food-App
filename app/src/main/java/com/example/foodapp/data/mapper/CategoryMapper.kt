//file categorymapper.kt
package com.example.foodapp.data.mapper

import com.example.foodapp.data.model.category.FoodCategoryUI

fun FoodCategoryUI.toFoodCategoryUI(
    isSelected: Boolean
): FoodCategoryUI {
    return FoodCategoryUI(
        name = name,
        id = id,
        imgRes = imgRes,
        isSelected = isSelected
    )
}