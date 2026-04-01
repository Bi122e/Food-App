package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
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

    override suspend fun addItem(
        userId: String,
        item: CartItem,
        food: Food,
        restaurant: Restaurant
    ): ApiResponse<Unit> {
        return try {
            food.toCart
            val doc = cartRef.document(userId).get().await()
            val currentCart = doc.toObject(Cart::class.java)
            val updateCart =
                if (currentCart == null) {
                    //chua co cart, tao moi
                    val rawCart = Cart(
                        userId = userId,
                        cartItems = listOf(item),
                        restaurantId = item.restaurantId,
                        restaurantName = restaurant.restaurantName,
                        deliveryFee = restaurant.deliveryFee,
                    )

                    //copy lai obj de dung ham calculate
                    rawCart.copy(
                        totalPrice = rawCart.calculateTotalPrice(),
                        price = rawCart.calculateSubTotalPrice(),
                        createdAt = null,
                        updatedAt = null
                    )

                } else {
                    val newCart = currentCart.copy(
                        cartItems = currentCart.cartItems + item
                    )

                    //copy lai obj de su dung ham calculate
                    newCart.copy(
                        cartItems = currentCart.cartItems + item,
                        price = newCart.calculateSubTotalPrice(),
                        totalPrice = newCart.calculateTotalPrice(),
                        updatedAt = null
                    )
                }
            cartRef
                .document(userId)
                .update("cartItems", updateCart)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to add item")
        }

        //cach khac, co kt logic khi user click vao item da ton tai, thi tang quantity do len
        /*
        override suspend fun addItem(userId: String, item: CartItem): ApiResponse<Unit> {
    return try {
        val doc = cartRef.document(userId).get().await()
        val currentCart = doc.toObject(Cart::class.java)

        val updatedCart = if (currentCart == null) {
            // 👉 chưa có cart → tạo mới
            Cart(
                userId = userId,
                cartItems = listOf(item),
                totalPrice = item.price.toDouble(),
                restaurantId = item.restaurantId
            )
        } else {
            val updatedItems = mergeItem(currentCart.cartItems, item)

            currentCart.copy(
                cartItems = updatedItems,
                totalPrice = calculateTotal(updatedItems)
            )
        }

        cartRef.document(userId).set(updatedCart).await()

        ApiResponse.Success(Unit)

    } catch (e: Exception) {
        ApiResponse.Error(e.message ?: "Failed to add item")
    }
}
fun mergeItem(
    current: List<CartItem>,
    newItem: CartItem
): List<CartItem> {

    val index = current.indexOfFirst {
        it.foodId == newItem.foodId &&
        it.variation == newItem.variation
    }

    return if (index != -1) {
        current.mapIndexed { i, item ->
            if (i == index) {
                item.copy(quantity = item.quantity + newItem.quantity)
            } else item
        }
    } else {
        current + newItem
    }
}
        * */
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
                val updatedItems = cart.cartItems.map { item ->
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
                val removeItem = cart.cartItems.filter { it.foodId != foodId }
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