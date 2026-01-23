package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Food(
    val name: String = "",
    val foodId: String = "",
    val description: String = "",
    val price: Int = 0,
    val imgUrl: String = "",
    val isAvailable: Boolean = true,
    val reviews: Int = 0,
    val totalRating: Double = 0.0,
    val foodTime: Int = 0,
    val ingredient: String = "",
    val calories: Int = 0,
    val restaurantId: String = "",
    val categoryId: String = "",
    val variations: List<Variation> = emptyList(),
    @ServerTimestamp
    val createdAp: Date? = null,
    val updatedAt: Date? = null,
) {
    fun isValid(): Boolean {
        return foodId.isNotEmpty() &&
                name.isNotEmpty() &&
                price >= 0 &&
                categoryId.isNotEmpty() &&
                restaurantId.isNotEmpty()
    }

    fun getAverageRating(): Double {
        return if (reviews > 0) {
            //            (totalRating / reviews).coerceIn(0.0, 5.0) get total rate <= 5
            totalRating.toDouble() / reviews
        } else {
            0.0
        }
    }

    fun hasVariations(): Boolean = variations.isNotEmpty()

    //get base/ default price
    fun getMinPrice(): Int = price

    //get maximum price
    fun getMaximumPrice(): Int {
        // if (variations.isEmpty()) return price
        //
        //        val maxVariationPrice = variations.maxOfOrNull { variation ->
        //            variation.options.maxOfOrNull { it.price } ?: 0
        //        } ?: 0
        //
        if (variations.isEmpty()) return getMinPrice()
        return price + variations.sumOf { it.options.maxOfOrNull { option -> option.price } ?: 0 }
    }

    //calculate price with variation
    fun getPriceWithVariation(selectedVariation: Map<String, List<String>>): Int {
        val basePrice = price
        val variationPrice = selectedVariation.entries.sumOf { (variationId, optionIds) ->
            val variation = variations.find { it.id == variationId }
            variation?.options?.filter { optionIds.contains(it.id) }?.sumOf { it.price } ?: 0
        }
        return basePrice + variationPrice

    }

    //add a review
    fun addReview(rating: Double): Food {
        require(rating in 1.0..5.0) { "rating must be between 1 and 5" }
        return copy(
            reviews = reviews + 1,
            totalRating = totalRating + rating.toInt(),
            updatedAt = Date()
        )
    }

    //popular by view food
    fun isPopular(): Boolean = reviews > 2 && getAverageRating() > 2

    //popular by rating
    fun isHighlyRated(): Boolean = getAverageRating() > 3 && reviews > 3
}
