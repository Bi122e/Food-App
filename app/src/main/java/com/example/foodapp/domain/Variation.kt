package com.example.foodapp.domain


data class Variation(
    val name: String = "",
    val id: String = "",
    val description: String = "",
    val type: VariationType = VariationType.SINGLE,
    val isRequired: Boolean = true,
    val minSelection: Int = 0,
    val maxSelection: Int = 0,
    val options: List<VariationOption> = emptyList()
) {
    fun isValid(): Boolean {
        return id.isNotEmpty() && name.isNotEmpty() && options.isNotEmpty()
    }

    fun isSelectedValid(selectedCount: Int): Boolean {
        return when (type) {
            VariationType.SINGLE -> selectedCount == 1
            VariationType.MULTI -> {
                val min = if (isRequired)
                    minSelection.coerceAtLeast(1) else 0
                val max = if (maxSelection > 0) maxSelection else Int.MAX_VALUE
                selectedCount in min..max

            }
        }
    }

    fun getSelectionRuleText(): String {
        return when (type) {
            VariationType.SINGLE -> {
                if (isRequired) "Chọn 1" else "Chọn 1 (tùy chọn)"
            }
            VariationType.MULTI ->  {
                when {
                    isRequired && maxSelection > 0 -> "Chọn từ $minSelection đến $maxSelection"
                    isRequired -> "Chọn tối thiểu $minSelection"
                    maxSelection > 0 -> "Chọn tối đa $maxSelection"
                    else -> "Tùy chọn"
                    }
            }
        }
    }

    fun calculatePrice(selectedOptions: List<String>): Int {
        return options
            .filter { selectedOptions.contains(it.id) }
            .sumOf { it.price }
    }

    fun getOptionById(id: String): VariationOption? {
        return options.find { it.id == id }
    }

    enum class VariationType {
        SINGLE, MULTI

    }
}
