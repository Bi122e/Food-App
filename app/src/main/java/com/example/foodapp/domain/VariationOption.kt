package com.example.foodapp.domain

data class VariationOption(
    val name: String = "",
    val price: Int = 0,
    val id: String = "",
    val description: String = "",
    val isAvailable: Boolean = true

) {
    fun isValid(): Boolean {
        return name.isNotEmpty() && price >= 0 }

    companion object {
        fun createStandardSizes(): List<VariationOption> {
            return  listOf(
                VariationOption("small", 0, "Nhỏ"),
                VariationOption("medium", 5000, "Vừa"),
                VariationOption("large", 1000, "Lớn"),
            )
        }
    }
}
