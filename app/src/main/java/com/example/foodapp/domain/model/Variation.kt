package com.example.foodapp.domain.model


data class Variation(
    val name: String = "",
    val id: String = "",
    val description: String = "",
    val type: VariationType = VariationType.SINGLE, //chi duoc chon 1 option, multi la chon nhieu option
    val required: Boolean = true, //co bat buoc user chon variation ko
    val minSelection: Int = 0, // so luong min khi user chon
    val maxSelection: Int = 0, // so luong max khi user chon
    val  options: List<VariationOption> = emptyList(),
    val valid: Boolean = true
) {
    fun isValidVariation(): Boolean {
        return id.isNotEmpty() && name.isNotEmpty() && options.isNotEmpty()
    }

    fun isSelectedValid(selectedCount: Int): Boolean {
        return when (type) {
            VariationType.SINGLE -> {
                if (required) selectedCount == 1
                else selectedCount in 0..1
            }
            VariationType.MULTI -> {
                val min = if (required)
                    minSelection.coerceAtLeast(1) else 0
                val max = if (maxSelection > 0) maxSelection else Int.MAX_VALUE
                selectedCount in min..max

            }
        }
    }

    fun getSelectionRuleText(): String {
        return when (type) {
            VariationType.SINGLE -> {
                if (required) "Chọn 1" else "Chọn 1 (tùy chọn)"
            }
            VariationType.MULTI ->  {
                when {
                    required && maxSelection > 0 -> "Chọn từ $minSelection đến $maxSelection"
                    required -> "Chọn tối thiểu $minSelection"
                    maxSelection > 0 -> "Chọn tối đa $maxSelection"
                    else -> "Tùy chọn"
                    }
            }
        }
    }

    fun calculatePrice(selectedOptions: List<String>): Int {
        return options
            .filter { option ->
                option.valid &&
                        option.available &&
                        selectedOptions.contains(option.id)
            }
            .sumOf { it.price }
    }
    //thit, trung, canh
    //return options ds hien tai.filter {it.id}
    //

    fun getOptionById(id: String): VariationOption? {
        return options.find {
            it.id == id &&
                    it.valid &&
                    it.available
        }
    }

    enum class VariationType {
        SINGLE, MULTI

    }
}
