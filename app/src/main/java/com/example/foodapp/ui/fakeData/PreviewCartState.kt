package com.example.foodapp.ui.fakeData

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.presentation.state.ActiveCartItemUi
import com.example.foodapp.presentation.state.CartUiState

object PreviewCartState {

    val previewCartState: CartUiState = CartUiState(
        cart = Cart(
            userId = "fake",
            cartItems = listOf(
                CartItem(
                    foodId = "foodId",
                    name = "name",
                    basePrice = 12,
                    quantity = 1,
                    imgUrls = "",
                    restaurantId = "resId",
                    notes = "",
                    variation = mapOf(
                        "id" to listOf(
                            VariationOption(
                                name = "var1",
                                price = 1,
                                id = "op1",
                                description = "p",
                                available = true,
                                valid = true
                            ),
                            VariationOption(
                                name = "var2",
                                price = 1,
                                id = "op1",
                                description = "p",
                                available = true,
                                valid = true
                            ),
                        )
                    )
                )
            ),
            totalPrice = 31,
            price = 13,
            deliveryFee = 12,
            restaurantId = "res1",
            restaurantName = "res",
        ),
        restaurant = Restaurant(),
        loadingItemKeys = emptySet(),
        currentEditingItem = ActiveCartItemUi(
            food = Food(),
            quantity = 1,
            variations = emptyMap(),
            note = ""
        )
    )

//    data class CartUiState(
//        val cart: Cart? = null,
//        val restaurant: Restaurant? = null,
//        val loadingFoodIds: Set<String> = emptySet(), //luwu key (foodkey + optionkey) vì var.id có thể trùng với option cũ, ví dụ cùng op
//        val currentEditingItem: ActiveCartItemUi? = null, //luu state option cua user, checkboxx, radio,..
//        val error: String? = null,
//    )
//
//    data class ActiveCartItemUi(
//        val food: Food,
//        val quantity: Int = 1,
//        val variations: Map<String, List<VariationOption>> = emptyMap(),
////    val selectedOption: Map<String, Set<String>> = emptyMap(),
//        val note: String = ""
//    )


}