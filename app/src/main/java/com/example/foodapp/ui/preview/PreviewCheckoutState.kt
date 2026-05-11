package com.example.foodapp.ui.preview

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.CustomerProfile
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.User
import com.example.foodapp.domain.model.UserProfile
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.presentation.state.CheckoutUiState

object PreviewCheckoutState {


    val previewCheckout = CheckoutUiState(
        user = User(
            uid = "dsds",
            email = "dfsdf@gmail.com",
            profile = UserProfile(
                customer = CustomerProfile(
                    uid = "dsds",
                    name = "name",
                    phone = "090",
                    address = "dsd"
                )
            )
        ),
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
                        ),
                        "id1" to listOf(
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
                        ),

                    )
                ),
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
                        ),
                        "id1" to listOf(
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
                            VariationOption(
                                name = "var2",
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
                            VariationOption(
                                name = "var2",
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
                            VariationOption(
                                name = "var2",
                                price = 1,
                                id = "op1",
                                description = "p",
                                available = true,
                                valid = true
                            ),
                        ),

                        )
                )


            ),
            totalPrice = 31,
            price = 13,
            deliveryFee = 12,
            restaurantId = "res1",
            restaurantName = "res",
        ),
        restaurant = Restaurant(
            restaurantId = "resid",
            restaurantName = "nha hang xam"
        )
    )
}