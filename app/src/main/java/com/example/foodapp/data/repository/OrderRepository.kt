package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun createOrder(order: Order): ApiResponse<String>

    suspend fun getOrderById(orderId: String): ApiResponse<Order>
    fun getOrderByUserId(userId: String): Flow<ApiResponse<List<Order>>>
    fun getOrderByRestaurantId(restaurantId: String): Flow<ApiResponse<List<Order>>>
    fun getAllOrder(): Flow<ApiResponse<List<Order>>>

    //update
    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): ApiResponse<Unit>
    suspend fun updateOrder(order: Order): ApiResponse<Unit>
    suspend fun cancelOrder(orderId: String): ApiResponse<Unit>

    //query
    suspend fun getOrderByStatus(userId: String, status: OrderStatus): ApiResponse<List<Order>>
    suspend fun getOrderHistory(userId: String, limit: Int = 10 ): ApiResponse<List<Order>>

    //delete
    suspend fun deleteOrder(orderId: String): ApiResponse<Unit>

    //statistics
    suspend fun getTotalOrdersCount(userId: String): ApiResponse<Int>
    suspend fun getTotalSpent(userId: String): ApiResponse<Int>



}