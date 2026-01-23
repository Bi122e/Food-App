package com.example.foodapp.domain.mapper

import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.VariationOption

fun Map<String, Set<String>>.toVariations(): List<Variation> {
    return map { (variationName, optionNames) ->
        Variation(
            name = variationName,
            options = optionNames.map { optionName ->
                VariationOption(
                    name = optionName,
                    price = 0
                )
            }
        )
    }
}
