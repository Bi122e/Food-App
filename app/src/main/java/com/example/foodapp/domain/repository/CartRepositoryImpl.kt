package com.example.foodapp.domain.repository

import android.util.Log
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
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
                    Log.d("CartState", "Api Error")
                    trySend(ApiResponse.Error(error.message ?: "Unknow"))
                }
                val cart = snapshot?.toObject(Cart::class.java)
                when {
                    cart == null -> {
                        trySend(ApiResponse.Empty)
                        Log.d("CartState", "empty:")
                    }
                    cart.checkValid() -> {
                        trySend(ApiResponse.Success(cart))
                        Log.d("CartState", "check valid: ${cart.toString()}")
                    }
                    else -> {
                        trySend(ApiResponse.Error("Invalid cart"))
                        Log.d("CartState", "error:")
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addItem(
        userId: String,
        item: CartItem,
        restaurant: Restaurant,
        forceClear: Boolean
    ): ApiResponse<Cart> {
        return try {
            val doc = cartRef.document(userId).get().await()
            val currentCart = doc.toObject(Cart::class.java)
            val cart =
                if (currentCart == null) { //nếu chưa có thì tạo mới
                    Cart(
                        userId = userId,
                        cartItems = listOf(item),
                        deliveryFee = restaurant.deliveryFee,
                        restaurantName = restaurant.restaurantName,
                        restaurantId = restaurant.restaurantId,
                        createdAt = null,
                        updatedAt = null,
                    )
                } else { //có rồi thì kiểm tra tiếp có thuộc nhà hàng tồn tại đó ko, ngược lại kt tieeps có thuộc nhà hàng hiện tại ko, có thì thêm mới
                    if (currentCart.canAddFromRestaurant(restaurant.restaurantId)) {
                        val mergedItems = mergeItem(currentCart.cartItems, item) //merge item cũ + mới
                        currentCart.copy(
                            cartItems = mergedItems,
                            updatedAt = null
                        )
                    } else {
                        if (!forceClear) {
                            return ApiResponse.Conflict(
                                message = "Giỏ hàng hiện tại thuộc nhà hàng khác",
                                oldRestaurantName = currentCart.restaurantName,
                                newRestaurantName = restaurant.restaurantName
                            )
                        }
                        Cart(
                            userId = userId,
                            cartItems = listOf(item),
                            deliveryFee = restaurant.deliveryFee,
                            restaurantName = restaurant.restaurantName,
                            restaurantId = restaurant.restaurantId,
                            createdAt = null,
                            updatedAt = null
                        )
                    }
                }
            val updateCart = cart.copy(
                totalPrice = cart.calculateTotalPrice(),
                price = cart.calculateSubTotalPrice()
            )
            cartRef.document(userId).set(updateCart).await()
            ApiResponse.Success(updateCart)
        } catch (e: Exception) {
            Log.d("ADDCART", "FAILED")
            ApiResponse.Error(e.message ?: "Failed to add item")
        }

//    return try {
//        val doc = cartRef.document(userId).get().await()
//        val currentCart = doc.toObject(Cart::class.java)
//        val updateCart =
//            if (currentCart == null) {
//                Log.d("ADDCART", "CURRENT == NULL")
//                //chua co cart, tao moi
//                val rawCart = Cart(
//                    userId = userId,
//                    cartItems = listOf(item),
//                    restaurantId = item.restaurantId,
//                    restaurantName = restaurant.restaurantName,
//                    deliveryFee = restaurant.deliveryFee,
//                )
//
//                //copy lai obj de dung ham calculate
//                rawCart.copy(
//                    totalPrice = rawCart.calculateTotalPrice(),
//                    price = rawCart.calculateSubTotalPrice(),
//                    createdAt = null,
//                    updatedAt = null
//                )
//
//            } else {
//                Log.d("ADDCART", "ELSE")
//                val newCart = currentCart.copy(
//                    cartItems = currentCart.cartItems + item
//                )
//
//                //copy lai obj de su dung ham calculate
//                newCart.copy(
//                    cartItems = currentCart.cartItems + item,
//                    price = newCart.calculateSubTotalPrice(),
//                    totalPrice = newCart.calculateTotalPrice(),
//                    updatedAt = null
//                )
//            }
//        cartRef
//            .document(userId)
//            .set(updateCart, SetOptions.merge())
//            .await()
//        ApiResponse.Success(Unit)

        //cach khac, co kt logic khi user click vao item da ton tai, thi tang quantity do len
        /*
        override suspend fun addItem(userId: String, item: CartItem): ApiResponse<Unit> {
    return try {
        val doc = cartRef.document(userId).get().await()
        val currentCart = doc.toObject(Cart::class.java)

        val updatedCart = if (currentCart == null) {
            // chưa có cart → tạo mới
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
    // Merge item nếu đã tồn tại
    private fun mergeItem(
        current: List<CartItem>,
        newItem: CartItem
    ): List<CartItem> {
        val index = current.indexOfFirst {
            it.foodId == newItem.foodId && it.variation == newItem.variation
        }
        return if (index != -1) {
            current.mapIndexed { i, item ->
                if (i == index) item.copy(quantity = item.quantity + newItem.quantity)
                else item
            }
        } else {
            current + newItem
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
        key: String,
        quantity: Int
    ): ApiResponse<Unit> {
        return try {
            val cartDoc = cartRef
                .document(userId)
                .get()
                .await()

            val cart = cartDoc?.toObject(Cart::class.java)
            if (cart != null && cart.checkValid()) {
                val updatedItems = cart.cartItems.map { item ->
                    if (item.key == key) {
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

    override suspend fun removeItem(userId: String, key: String): ApiResponse<Unit> {
        return try {
            val cartDoc = cartRef.document(userId).get().await()
            val cart = cartDoc?.toObject(Cart::class.java)

            if (cart != null && cart.checkValid()) {
                val removeItem = cart.cartItems.filter { it.key != key }
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