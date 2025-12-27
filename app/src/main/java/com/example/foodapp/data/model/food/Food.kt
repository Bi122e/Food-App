//package com.example.foodapp.data
//
//import com.google.firebase.firestore.ServerTimestamp
//import java.util.Date
//
//// ============================================
//// FILE GỘP: Food.kt
//// Bao gồm: Food + Variation + SubVariation
//// Giảm từ 3 files → 1 file
//// ============================================
//
///**
// * Domain Model cho Food (món ăn)
// * Map với Firestore collection "foods"
// */
//data class Food(
//    val foodId: String = "",
//    val name: String = "",
//    val description: String = "",
//    val price: Double = 0.0,
//    val imageUrl: String = "",
//    val categoryId: String = "",
//    val restaurantId: String = "",
//    val isAvailable: Boolean = true,
//    val foodTime: Int = 0,
//    val reviews: Int = 0,
//    val totalRating: Double = 0.0,
//    val ingredient: String = "",
//    val calories: Int = 0,
//    val variation: List<Variation> = emptyList(),
//    @ServerTimestamp
//    val createdAt: Date? = null,
//    val updatedAt: Date? = null
//) {
//    constructor() : this(
//        "", "", "", 0.0, "", "", "", true,
//        0, 0, 0.0, "", 0, emptyList(), null, null
//    )
//
//    // === RATING METHODS ===
//    fun getAverageRating(): Double = if (reviews > 0) {
//        (totalRating / reviews).coerceIn(0.0, 5.0)
//    } else 0.0
//
//    fun getFormattedRating(): String = String.format("%.1f", getAverageRating())
//
//    fun addReview(rating: Double): Food {
//        require(rating in 1.0..5.0) { "Rating phải từ 1-5" }
//        return copy(
//            reviews = reviews + 1,
//            totalRating = totalRating + rating,
//            updatedAt = Date()
//        )
//    }
//
//    // === VARIATION METHODS ===
//    fun hasVariations(): Boolean = variation.isNotEmpty()
//
//    fun getMinPrice(): Double = price
//
//    fun getMaxPrice(): Double {
//        if (variation.isEmpty()) return price
//        val maxVariationPrice = variation.maxOfOrNull { group ->
//            group.subVariation.maxOfOrNull { it.price } ?: 0
//        } ?: 0
//        return price + maxVariationPrice
//    }
//
//    fun calculatePriceWithVariations(selectedVariations: Map<String, List<SubVariation>>): Int {
//        val basePrice = price.toInt()
//        val variationPrice = selectedVariations.values.flatten().sumOf { it.price }
//        return basePrice + variationPrice
//    }
//
//    // === FORMATTING METHODS ===
//    fun getFormattedPrice(): String = if (hasVariations()) {
//        val min = getMinPrice().toInt()
//        val max = getMaxPrice().toInt()
//        if (min == max) "${min}₫" else "${min}₫ - ${max}₫"
//    } else "${price.toInt()}₫"
//
//    fun getFormattedFoodTime(): String = "$foodTime phút"
//
//    // === VALIDATION & STATUS ===
//    fun isValid(): Boolean = foodId.isNotEmpty() &&
//            name.isNotEmpty() &&
//            price >= 0 &&
//            categoryId.isNotEmpty() &&
//            restaurantId.isNotEmpty() &&
//            foodTime >= 0 &&
//            calories >= 0
//
//    fun isPopular(): Boolean = reviews >= 50 && getAverageRating() >= 4.0
//
//    fun isHighlyRated(): Boolean = reviews >= 10 && getAverageRating() >= 4.5
//}
//
//// ============================================
//// VARIATION GROUP
//// ============================================
//
///**
// * Model cho Variation Group (nhóm tùy chọn)
// * VD: "Size", "Topping", "Độ ngọt"
// */
//data class Variation(
//    val id: String = "",
//    val name: String = "",
//    val description: String = "",
//    val type: VariationType = VariationType.SINGLE,
//    val isRequired: Boolean = true,
//    val minSelection: Int = 0,
//    val maxSelection: Int = 0,
//    val subVariation: List<SubVariation> = emptyList()
//) {
//    constructor() : this("", "", "", VariationType.SINGLE, true, 0, 0, emptyList())
//
//    // === VALIDATION ===
//    fun isValid(): Boolean = id.isNotEmpty() &&
//            name.isNotEmpty() &&
//            subVariation.isNotEmpty() &&
//            (type == VariationType.SINGLE || (minSelection >= 0 && maxSelection >= 0))
//
//    fun isSelectionValid(selectedCount: Int): Boolean = when (type) {
//        VariationType.SINGLE -> selectedCount == 1
//        VariationType.MULTI -> {
//            val min = if (isRequired) minSelection.coerceAtLeast(1) else 0
//            val max = if (maxSelection > 0) maxSelection else Int.MAX_VALUE
//            selectedCount in min..max
//        }
//    }
//
//    // === DISPLAY TEXT ===
//    fun getSelectionRuleText(): String = when (type) {
//        VariationType.SINGLE -> if (isRequired) "Chọn 1" else "Chọn 1 (tùy chọn)"
//        VariationType.MULTI -> when {
//            isRequired && maxSelection > 0 -> "Chọn $minSelection-$maxSelection"
//            isRequired -> "Chọn tối thiểu $minSelection"
//            maxSelection > 0 -> "Chọn tối đa $maxSelection"
//            else -> "Chọn nhiều (tùy chọn)"
//        }
//    }
//
//    // === PRICE CALCULATION ===
//    fun calculatePrice(selectedSubVariationIds: List<String>): Int =
//        subVariation.filter { selectedSubVariationIds.contains(it.id) }
//            .sumOf { it.price }
//
//    // === HELPERS ===
//    fun getSubVariationById(id: String): SubVariation? = subVariation.find { it.id == id }
//
//    fun hasExtraCharge(): Boolean = subVariation.any { it.price > 0 }
//
//    fun getCheapestOption(): SubVariation? = subVariation.minByOrNull { it.price }
//
//    fun getMostExpensiveOption(): SubVariation? = subVariation.maxByOrNull { it.price }
//
//    companion object {
//        fun createSizeVariation(
//            sizes: List<SubVariation> = SubVariation.createStandardSizes()
//        ): Variation = Variation(
//            id = "size",
//            name = "Size",
//            description = "Chọn kích cỡ",
//            type = VariationType.SINGLE,
//            isRequired = true,
//            subVariation = sizes
//        )
//
//        fun createToppingVariation(
//            toppings: List<SubVariation>,
//            maxSelection: Int = 0
//        ): Variation = Variation(
//            id = "topping",
//            name = "Topping",
//            description = "Chọn topping",
//            type = VariationType.MULTI,
//            isRequired = false,
//            maxSelection = maxSelection,
//            subVariation = toppings
//        )
//    }
//}
//
//// ============================================
//// SUB-VARIATION (OPTION)
//// ============================================
//
///**
// * Model cho sub-variation (lựa chọn cụ thể)
// * VD: Variation = "Size", SubVariation = ["Nhỏ", "Vừa", "Lớn"]
// */
//data class SubVariation(
//    val id: String = "",
//    val name: String = "",
//    val price: Int = 0,
//    val description: String = "",
//    val isAvailable: Boolean = true
//) {
//    constructor() : this("", "", 0, "", true)
//
//    // === VALIDATION ===
//    fun isValid(): Boolean = name.isNotEmpty() && price >= 0
//
//    // === FORMATTING ===
//    fun getFormattedPrice(): String = when {
//        price > 0 -> "+${price}₫"
//        price < 0 -> "${price}₫"
//        else -> "Miễn phí"
//    }
//
//    fun getDisplayText(): String {
//        val priceText = getFormattedPrice()
//        return if (priceText == "Miễn phí") name else "$name ($priceText)"
//    }
//
//    // === COMPARISON ===
//    fun isSameAs(other: SubVariation): Boolean =
//        this.name.equals(other.name, ignoreCase = true)
//
//    companion object {
//        fun createFree(name: String): SubVariation = SubVariation(
//            id = name.lowercase().replace(" ", "_"),
//            name = name,
//            price = 0
//        )
//
//        fun createStandardSizes(): List<SubVariation> = listOf(
//            SubVariation("small", "Nhỏ", 0),
//            SubVariation("medium", "Vừa", 5000),
//            SubVariation("large", "Lớn", 10000)
//        )
//    }
//}
//
//// ============================================
//// ENUMS
//// ============================================
//
//enum class VariationType {
//    SINGLE,  // Radio button
//    MULTI    // Checkbox
//}