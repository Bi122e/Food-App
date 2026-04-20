package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCart(userId: String): Flow<ApiResponse<Cart>>

    suspend fun addItem( userId: String, item: CartItem, restaurant: Restaurant, forceClear: Boolean): ApiResponse<Cart>

    suspend fun updateItemQuantity(userId: String, key: String, quantity: Int): ApiResponse<Unit>

    suspend fun removeItem(userId: String, key: String): ApiResponse<Unit>

    suspend fun clearCart(userId: String): ApiResponse<Unit>

    suspend fun updateDeliveryFee(userId: String, fee: Int): ApiResponse<Unit>

//    suspend fun updateCart(cart: Cart): ApiResponse<Unit>
//
//    suspend fun createCart(cart: Cart): ApiResponse<Unit>
}