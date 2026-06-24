package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse


interface AuthRepository {

    suspend fun login(email: String, password: String): ApiResponse<String>

    suspend fun register( email: String, password: String): ApiResponse<String>

    suspend fun loginWithGoogle(idToken: String): ApiResponse<String>

    suspend fun changePassword(oldPassword: String, newPassword: String): ApiResponse<Unit>

    suspend fun logOut()
    fun currentUserId(): String?


//     fun onNewToken(
//        token: String
//    )
//    - cài lại app
//    - clear data
//    - đổi thiết bị
//    - Firebase refresh token, để luôn đồng bộ token mới.
 }


