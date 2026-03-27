package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.UserProfileCombine
import kotlinx.coroutines.flow.Flow

interface  UserProfileCombineRepository {

    fun getUserProfile(uid: String): Flow<ApiResponse<UserProfileCombine>>

    suspend fun updateCustomerProfile(profile: UserProfileCombine): ApiResponse<Unit>
}