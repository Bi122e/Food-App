package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.Variation

data class FoodDetailState(
    val food: Food? = null,
    val restaurant: Restaurant? = null,

    //usr select
    val quantity: Int = 1,
    val selectedVariations: Map<String, List<String>> = emptyMap(), // variationId -> optionIds)
    val notes: String = "",
    val isFavorite: Boolean = false,

    //ui state
    val isLoading: Boolean = false,
    val isAddingToCart: Boolean = false,
    val error: String? = null,
    val showVariationError: Boolean = false

    ) {
    //price calculations

    //get current price (base + selected variation)

    fun getCurrentPrice(): Int {
        if (food == null) return 0
        return food.getPriceWithVariation(selectedVariations)
    }
    fun getTotalPrice(): Int = getCurrentPrice() * quantity

    fun toggleVariation(variationId: String, optionId: String): FoodDetailState {
        if (food == null) return this

        val variation = food.variations.find { it.id == variationId } ?: return this
        val currentSelected = selectedVariations[variationId]?.toMutableList() ?: mutableListOf()

        val newSelected = when (variation.type) {
            Variation.VariationType.SINGLE -> {
                // Single choice: replace
                listOf(optionId)
            }
            Variation.VariationType.MULTI -> {
                // Multi choice: toggle
                if (currentSelected.contains(optionId)) {
                    currentSelected.remove(optionId)
                    currentSelected
                } else {
                    // Check max selection
                    if (variation.maxSelection > 0 &&
                        currentSelected.size >= variation.maxSelection) {
                        return this // Already at max
                    }
                    currentSelected.add(optionId)
                    currentSelected
                }
            }
        }

        val newMap = selectedVariations.toMutableMap()
        if (newSelected.isEmpty()) {
            newMap.remove(variationId)
        } else {
            newMap[variationId] = newSelected
        }

        return copy(
            selectedVariations = newMap,
            showVariationError = false
        )
    }

    fun isOptionSelected(variationId: String, optionId: String): Boolean {
        return selectedVariations[variationId]?.contains(optionId) ?: false
    }

    fun getSelectedCount(variationId: String): Int {
        return selectedVariations[variationId]?.size ?: 0
    }

    // ============================================
    // QUANTITY MANAGEMENT
    // ============================================

    fun incrementQuantity(): FoodDetailState {
        return if (quantity < 99) copy(quantity = quantity + 1) else this
    }

    fun decrementQuantity(): FoodDetailState {
        return if (quantity > 1) copy(quantity = quantity - 1) else this
    }

    fun updateQuantity(newQuantity: Int): FoodDetailState {
        return if (newQuantity in 1..99) copy(quantity = newQuantity) else this
    }

    // ============================================
    // VALIDATIONS
    // ============================================

    /**
     * Check if can add to cart
     */
    fun canAddToCart(): Boolean {
        if (food == null || isAddingToCart) return false

        // Check all required variations are selected
        val requiredVariations = food.variations.filter { it.isRequired }
        val allRequiredSelected = requiredVariations.all { variation ->
            val selectedCount = getSelectedCount(variation.id)
            variation.isSelectedValid(selectedCount)
        }

        return quantity > 0 && allRequiredSelected
    }

    /**
     * Get validation error message
     */
    fun getValidationError(): String? {
        if (food == null) return null

        val requiredVariations = food.variations.filter { it.isRequired }
        val missingVariation = requiredVariations.find { variation ->
            val selectedCount = getSelectedCount(variation.id)
            !variation.isSelectedValid(selectedCount)
        }

        return missingVariation?.let { "Vui lòng chọn ${it.name}" }
    }

    // ============================================
    // ACTIONS
    // ============================================

    fun updateNotes(newNotes: String): FoodDetailState {
        return copy(notes = newNotes.take(200)) // Max 200 chars
    }

    fun toggleFavorite(): FoodDetailState {
        return copy(isFavorite = !isFavorite)
    }

    fun reset(): FoodDetailState {
        return copy(
            quantity = 1,
            notes = "",
            selectedVariations = emptyMap(),
            showVariationError = false
        )
    }

    // ============================================
    // UI HELPERS
    // ============================================

    fun isReady(): Boolean {
        return food != null && restaurant != null && !isLoading
    }
}