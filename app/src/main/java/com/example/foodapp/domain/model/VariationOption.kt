package com.example.foodapp.domain.model

data class VariationOption(
    val name: String = "",
    val price: Long = 0L,
    val id: String = "",
    val description: String = "",
    val available: Boolean = true,
    val valid: Boolean = true, //thuoc ve logic he thong

) {
    fun isValidOption(): Boolean {
        return name.isNotEmpty() && price >= 0 }

//    companion object {
//        fun createStandardSizes(): List<VariationOption> {
//            return  listOf(
//                VariationOption("small", 0, "Nhỏ"),
//                VariationOption("medium", 5000, "Vừa"),
//                VariationOption("large", 1000, "Lớn"),
//            )
//        }
//    }
}
