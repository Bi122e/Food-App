package com.example.foodapp.domain.mapper

import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Variation
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
            variation = ui.variations,
        )
    }

    //toi viet nhu vay dung chua
    fun toUi(cartItem: CartItem, food: Food,  allVariation: List<Variation> ): ActiveCartItemUi {
        val variationMap = cartItem.variation.mapValues { (variationId, optionIds) ->
            val variation = allVariation.find { it.id == variationId }
            optionIds.mapNotNull { variation?.getOptionById(it.id) }
        }
        return ActiveCartItemUi(
            food = food,
            quantity = cartItem.quantity,
            variations = variationMap,
            note = cartItem.notes
        )
    }

    //va co can map sang cart ko



}