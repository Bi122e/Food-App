package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun createUser(user: User): ApiResponse<Unit>

    suspend fun getUserById(userId: String): ApiResponse<User>
    suspend fun getCurrentUser(userId: String): Flow<ApiResponse<User>>

    suspend fun updateUser(user: User): ApiResponse<Unit>

    suspend fun updateProfileImage(userId: String, imageUrl: String): ApiResponse<Unit>

    suspend fun updateFcmToken(userId: String, token: String): ApiResponse<Unit>

    suspend fun checkEmailExist(email: String): ApiResponse<Boolean>

    suspend fun deactivateAccount(userId: String): ApiResponse<Unit>

    suspend fun changePassword(oldPassword: String, newPassword: String): ApiResponse<Unit>

    suspend fun logout()
}
