package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CartRepository {

    private val cartRef = firestore.collection(Constance.COLLECTION_CARTS)


    override fun getCart(userId: String): Flow<ApiResponse<Cart>> = callbackFlow {
        val listener = cartRef
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Unknow"))
                }
                val cart = snapshot?.toObject(Cart::class.java)
                if (cart != null && cart.isValid()) {
                    trySend(ApiResponse.Success(cart))
                } else {
                    trySend(ApiResponse.Error("Failed to load cart"))
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addItem(userId: String, item: CartItem): ApiResponse<Unit> {
        return try {
            cartRef
                .document(userId)
                .update("cartItems", item)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to add item")
        }
    }

    override suspend fun updateDeliveryFee(userId: String, fee: Int): ApiResponse<Unit> {
        return try {
            cartRef
                .document(userId)
                .update("deliveryFee", fee)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update Fee")
        }
    }

    override suspend fun updateItemQuantity(
        userId: String,
        foodId: String,
        quantity: Int
    ): ApiResponse<Unit> {
        return try {
            val cartDoc = cartRef
                .document(userId)
                .get()
                .await()

            val cart = cartDoc?.toObject(Cart::class.java)
            if (cart != null && cart.isValid()) {
                val updatedItems = cart.cartItems.map{ item ->
                    if (item.foodId == foodId) {
                        item.copy(
                            quantity = quantity
                        )
                    } else item
                }
                val updatedCart = cart.copy(cartItems = updatedItems)
                cartRef.document(userId).set(updatedCart).await()
                ApiResponse.Success(Unit)
            } else {
                ApiResponse.Error("Cart not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update quantity")
        }

    }

    override suspend fun removeItem(userId: String, foodId: String): ApiResponse<Unit> {
        return try {
            val cartDoc = cartRef.document(userId).get().await()
            val cart = cartDoc?.toObject(Cart::class.java)

            if (cart != null && cart.isValid()) {
                val removeItem = cart.cartItems.filter { it.foodId == foodId }
                val updatedCart = cart.copy(cartItems = removeItem)

                cartRef.document(userId).set(updatedCart).await()
                ApiResponse.Success(Unit)
            } else {
                ApiResponse.Error("Cart not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to remove item")
        }
    }
    override suspend fun clearCart(userId: String): ApiResponse<Unit> {
        return try {
            cartRef.document(userId).delete().await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to clear cart")
        }
    }
}