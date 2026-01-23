package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse


interface AuthRepository {

    suspend fun login(email: String, password: String): ApiResponse<Unit>

    suspend fun register(name: String, email: String, password: String): ApiResponse<Unit>

    suspend fun loginWithGoogle(idToken: String): ApiResponse<Unit>

    suspend fun changePassword(oldPassword: String, newPassword: String): ApiResponse<Unit>

    suspend fun logOut()
}


