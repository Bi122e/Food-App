package com.example.foodapp.domain.mapper

import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Variation
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.presentation.state.ActiveCartItemUi

object CartMapper {

    fun toDomain(ui: ActiveCartItemUi): CartItem {
        return CartItem(
            foodId = ui.food.foodId,
            name = ui.food.name,
            basePrice = ui.food.price,
            quantity = ui.quantity,
            imgUrls = ui.food.imgUrl,
            restaurantId = ui.food.restaurantId,
            notes = ui.note,
            variation = ui.variations.mapValues { it.value.toList() },
        )
    }

    //toi viet nhu vay dung chua
    fun toUi(cartItem: CartItem, food: Food,  allVariation: Set<Variation> ): ActiveCartItemUi {
        val variationMap = cartItem.variation.mapValues { (variationId, optionIds) ->
            val variation = allVariation.find { it.id == variationId }
            optionIds.mapNotNull { variation?.getOptionById(it.id) }.toMutableSet()

        }
        return ActiveCartItemUi(
            food = food,
            quantity = cartItem.quantity,
            variations = variationMap,
            note = cartItem.notes
        )
    }
//    val variations: Map<String, Set<VariationOption>> = emptyMap(),


    fun toListVariation(variation: Map<String, List<VariationOption>>, food: Food): List<Variation> {
//        val listAsMap = food.variations.associate { it.name to it.options }
//            //variation: Map<String, Set<VariationOption>>o
//            .filter { it.key in variation }
//            .map { (k, v) ->
//                Variation(
//                    options = v
//                )
//            }
//
//        return listAsMap
        val mapValues = food.variations.associateBy { it.name }
        // val mapValues: Map<String, Variation>
        val mapVariation = variation.mapNotNull { (k,v) ->
            mapValues[k]?.copy(
                options = v.toList()
            )
        }
        return mapVariation
    }



    //va co can map sang cart ko



}