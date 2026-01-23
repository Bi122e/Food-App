package com.example.foodapp.domain.model

data class OrderItem(
    val foodId: String = "",
    val foodName: String = "",
    val variations: List<Variation> = emptyList(),
    val selectedOptions: Map<String, List<String>> = emptyMap(),
    val imgUrl: String = "",
    val notes: String = "",
    val price: Int = 0,
    val quantity: Int = 0,
) {
    fun isValid(): Boolean {
        return foodName.isNotEmpty() &&
                foodId.isNotEmpty() &&
                price >= 0 &&
                quantity > 0
    }

    //    fun getTotalPrice(): Int {
//        return variation.sumOf {
//            it.options.sumOf { variationOption -> variationOption.price }
//            //[[topping, [topping1, 15]], [size, [m, 15], [l, 14]] ]
//        }
//    }



    fun getTotalPrice(): Int = price * quantity

    fun hasNotes(): Boolean = notes.isNotEmpty()
    //cai nay co trong khong -> kt thang nay khong duoc trong vi no se tra ve true neu k trong, false neu k trong

    fun getVariationSummary(): String {
        if (variations.isEmpty()) return ""

        return variations
            .map { variation ->
                val selected = variation.options
                    .filter { it.price >= 0 }
                    .joinToString(", ") { it.name }

                if (selected.isNotEmpty())
                    "${variation.name}: $selected"
                else
                    ""
            }
            .filter { it.isNotEmpty() }
            .joinToString(", ")
    }

}

