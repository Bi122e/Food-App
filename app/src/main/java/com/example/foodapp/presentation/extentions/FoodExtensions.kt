package com.example.foodapp.presentation.extentions

import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.core.utils.toVND
import java.util.Locale


// 50000 → "50.000₫","50.000₫ - 60.000₫"

fun Food.getFormattedPrice(): String {
    return if (hasVariations()) {
        val min = getMinPrice().toInt()
        val max = getMaximumPrice().toInt()
        if (min == max) {
            "${min.toVND()}đ"
        } else {
            "${min.toVND()}đ - ${max.toVND()}đ"
        }
    } else {
        "${price.toVND()}đ"
    }

}

fun Food.getFormattedRating(): String {
    return String.format(Locale.getDefault(), "%.1f", getAverageRating().toDouble())
}

//"⭐ 4.5 (120)"
fun Food.getFormattedText(): String {
    return if (reviews > 0) {
        "⭐${getFormattedRating()} (${reviews})"
    } else {
        "Chưa có đánh giá"
    }
}

//time formating,  30 → "30 phút"
fun Food.getFormattedFoodTime(): String = "$foodTime phút"

fun Food.getBadgeText(): String? {
    return when {
        isHighlyRated() -> "Danh gia cao"
        isPopular() -> "Pho bien"
        else -> null
    }
}


fun VariationOption.getFormattedPrice(): String {
    return when {
        price > 0 -> "+ ${price}đ"
        price < 0 -> "${price}đ"
        else -> "Miễn phí"
    }
}
//private fun Int.toVND(): String {
//    return "%,d".format(this).replace(",", ".")
//}
// * Get display text: "Lớn (+10.000₫)"
fun VariationOption.getDisplayText(): String {
    val priceText = getFormattedPrice()
    return if (priceText == "Mien phi") name else "$name ($priceText)"
}