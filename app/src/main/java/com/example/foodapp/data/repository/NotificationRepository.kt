package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.AppNotification
 import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getNotifications(userId: String): Flow<ApiResponse<List<AppNotification>>>

    suspend fun createNotification(notification: AppNotification): ApiResponse<Unit>

    suspend fun deleteNotification(notificationId: String): ApiResponse<Unit>

    suspend fun markAsRead(notificationId: String): ApiResponse<Unit>

    fun getUnreadCount(userId: String): Flow<ApiResponse<Int>>

//    suspend fun updateNotification(notificationId: String): ApiResponse<Unit>
    suspend fun deactivateNotification(notificationId: String): ApiResponse<Unit>
}