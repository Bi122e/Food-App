package com.example.foodapp.core.utils

import com.example.foodapp.domain.model.VariationOption

fun buildCartItemKey(
    foodId: String,
    variations: Map<String, Set<VariationOption>>
): String {
    val variationKey = variations
        .toSortedMap()
        .map { (group, options) ->

            val sortedIds = options
                .mapNotNull { it.id.takeIf { it.isNotBlank() } }
                .sorted()

            "${group.trim().lowercase()}:${sortedIds.joinToString(",")}"
        }
        .joinToString("|")

    return "$foodId#$variationKey"
}