package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderStatus
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class OrderRepositoryImpl: OrderRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val orderCollection = firestore.collection(Constance.COLLECTION_ORDERS)

    override suspend fun getOrderById(orderId: String): ApiResponse<Order> {
        return try {
            val orderRef = orderCollection.document(orderId).get().await()
            val order = orderRef.toObject(Order::class.java)
                ?: return ApiResponse.Error("Order not found")
            ApiResponse.Success(order)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "")
        }
    }
    override suspend fun createOrder(order: Order): ApiResponse<String> {
        return try {
            val snapshot = orderCollection.document()
            val orderId = snapshot.id
            val orderWithId = order.copy(
                orderId = orderId,
                createdAt = Date(),
                updatedAt = Date())
            snapshot.set(orderWithId).await()
            ApiResponse.Success(orderId)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to create Order")
        }
    }

    override fun getAllOrder(): Flow<ApiResponse<List<Order>>> = callbackFlow{
        val listener = orderCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ApiResponse.Error(error.message ?: "Failed to get order"))
            }
//            if (snapshot != null) {
//                val order = snapshot.documents.mapNotNull {
//                    it.toObject(Order::class.java)
//                }
//                ApiResponse.Success(order)
//            } else {
//                ApiResponse.Error("Order not found")
//            }
            val orders = snapshot?.documents?.mapNotNull {
                it.toObject(Order::class.java)
            } ?: emptyList()
            ApiResponse.Success(orders)
        }
        awaitClose { listener.remove() }
    }

    override fun getOrderByUserId(userId: String): Flow<ApiResponse<List<Order>>> = callbackFlow {
        val listener = orderCollection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Failed to get order by user id"))
                return@addSnapshotListener
                }
                val orders = snapshot?.documents?.mapNotNull {
                    it.toObject(Order::class.java)
                } ?: emptyList()
                trySend(ApiResponse.Success(orders))
            }
        awaitClose { listener.remove() }
    }

    override fun getOrderByRestaurantId(restaurantId: String): Flow<ApiResponse<List<Order>>> = callbackFlow {
        val listener = orderCollection
            .whereEqualTo("restaurantId", restaurantId)
            .orderBy("createAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Failed to get order by restaurantId"))
                    return@addSnapshotListener
                }
                val orders = snapshot?.documents?.mapNotNull {
                    it.toObject(Order::class.java)
                } ?: emptyList()

                trySend(ApiResponse.Success(orders))
            }
        awaitClose { listener.remove() }
    }

    override suspend fun cancelOrder(orderId: String): ApiResponse<Unit> {
        updateOrderStatus(orderId, OrderStatus.CANCELLED)
        return ApiResponse.Success(Unit)
    }


    override suspend fun getOrderByStatus(
        userId: String,
        status: OrderStatus
    ): ApiResponse<List<Order>> {
        return try {
            val snapshot = orderCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", status)
                .orderBy("createAt", Query.Direction.DESCENDING)
                .get()
                .await()
            val orders = snapshot.documents.mapNotNull {
                it.toObject(Order::class.java)
            }
            ApiResponse.Success(orders)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get order by status")
        }
    }

    override suspend fun getOrderHistory(userId: String, limit: Int): ApiResponse<List<Order>> {
        return try {
            val snapshot = orderCollection
                .whereEqualTo("userId", userId)
                .orderBy("createAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            val orders = snapshot.documents.mapNotNull {
                it.toObject(Order::class.java)
            }
            ApiResponse.Success(orders)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get order history")
        }
    }


    override suspend fun getTotalSpent(userId: String): ApiResponse<Int> {
        return try {
            val snapshot = orderCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", OrderStatus.DELIVERED)
                .get()
                .await()
            val orders = snapshot.documents.mapNotNull {
                it.toObject(Order::class.java)
            }
            val total = orders.sumOf { it.total }
            ApiResponse.Success(total)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get total spent")
        }
    }


    override suspend fun getTotalOrdersCount(userId: String): ApiResponse<Int> {
        return try {
            val count = orderCollection
                .whereEqualTo("userId", userId)
                .count()
                .get(AggregateSource.SERVER)
                .await()
            //            ApiResponse.Success(snapshot.size())
            ApiResponse.Success(count.count.toInt())
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get total order count")
        }
    }

    override suspend fun updateOrder(order: Order): ApiResponse<Unit> {
        return try {
            val updatedOrder = order.copy(updatedAt = Date())
            orderCollection
                .document(order.orderId)
                .set(updatedOrder)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update order")
        }
    }

    override suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus
    ): ApiResponse<Unit> {
        return try {
//            val orderRef = orderCollection.document(orderId)
//            val queryOrder = orderRef.get().await()
//            val updatedOrder = queryOrder.toObject(Order::class.java).copy(
//                status = status,
//                updatedAt = Date()
//            )
//            orderRef.set(updatedOrder)
//            ApiResponse.Success(updatedOrder)
            val updates = mutableMapOf<String, Any>(
                "status" to status.name,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (status == OrderStatus.DELIVERING) {
                updates["deliveredAt"] = FieldValue.serverTimestamp()
            }
            orderCollection.document(orderId).update(updates).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update order status")
        }
    }

    override suspend fun deleteOrder(orderId: String): ApiResponse<Unit> {
        return try {
            orderCollection.document(orderId).delete().await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to delete order")
        }
    }
}