package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Food(
    val name: String = "",
    val nameLower: String = "",
    val foodId: String = "",
    val description: String = "",
    val price: Int = 0,
    val imgUrl: String = "",
    val available: Boolean = true,
    val reviewCount: Int = 0,
    val totalRating: Double = 0.0,
    val foodTime: Int = 0,
    val ingredient: String = "",
    val calories: Int = 0,
    val restaurantId: String = "",
    val categoryId: String = "",
    val variations: List<Variation> = emptyList(),
    val averageRating: Double = 0.0,
    val minPrice: Int = 0,
    val maximumPrice: Int = 0,
    val isHighlyRatedFlag: Boolean = false,
    val popular: Boolean = false,
    val valid: Boolean = true,
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
) {
    fun checkIsValid(): Boolean {
        return foodId.isNotEmpty() &&
                name.isNotEmpty() &&
                price >= 0 &&
                variations.all { it.isValidVariation() } &&
                categoryId.isNotEmpty() &&
                restaurantId.isNotEmpty()
    }
    fun getVariationSize(): Int {
        return variations.sumOf { it.options.size }
    }
    fun calculateAverageRating(): Double {
        return if (reviewCount > 0) {
            //            (totalRating / reviews).coerceIn(0.0, 5.0) get total rate <= 5
            totalRating.toDouble() / reviewCount
        } else {
            0.0
        }
    }

    fun hasVariations(): Boolean = variations.isNotEmpty()

    //get base/ default price
//    fun getMinPrice(): Int = price

    //get maximum price
//    fun getMaximumPrice(): Int {
//        // if (variations.isEmpty()) return price
//        //
//        //        val maxVariationPrice = variations.maxOfOrNull { variation ->
//        //            variation.options.maxOfOrNull { it.price } ?: 0
//        //        } ?: 0
//        //
//        if (variations.isEmpty()) return getMinPrice()
//        return price + variations.sumOf { it.options.maxOfOrNull { option -> option.price } ?: 0 }
//    }


    //calculate price with variation

    //variation: Map<String, List<VariationOption>>
    fun getPriceWithVariation(selectedVariation: Map<String, Set<VariationOption>>): Long {
        if (!valid || !available) return 0

        val basePrice = price

         val variationPrice = selectedVariation.entries.sumOf { (variationId, variationOption, ) ->

            val variation = variations.find {
                it.id == variationId && it.options.isNotEmpty()
            } ?: return@sumOf 0



            variation.options
                .filter { option ->
                    option.valid &&
                            option.available &&
                            variationOption.contains(option)
                }
                .sumOf { it.price }
        }

        return basePrice + variationPrice
    }

    //add a review
    fun addReview(rating: Double): Food {
        require(rating in 1.0..5.0)

        val newReviewCount = reviewCount + 1
        val newTotalRating = totalRating + rating
        val newAverage = newTotalRating / newReviewCount

        return copy(
            reviewCount = newReviewCount,
            totalRating = newTotalRating,
            averageRating = newAverage,
            updatedAt = Date()
        )
    }

    //popular by view food
    fun checkIsPopular(): Boolean = reviewCount > 2 && calculateAverageRating() > 2

    //popular by rating
    fun checkHighlyRated(): Boolean = calculateAverageRating() > 3 && reviewCount > 3
}

// File: domain/model/Food.kt hoặc FoodExt.kt

fun Food.toCartItem(
    quantity: Int,
    selectedVariations: Map<String, Set<VariationOption>>,
    specialInstructions: String,
): CartItem {
    return CartItem(
//        cartItemId = "${this.foodId}_${selectedVariations.hashCode()}",
        foodId = this.foodId,
        basePrice = this.price,
        name = this.name,
        quantity = quantity,
        imgUrls = this.imgUrl,
        restaurantId = this.restaurantId,
        notes = specialInstructions,
        variation = selectedVariations.mapValues { it.value.toList() }
    )
}