package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createCustomerProfile(profile: com.example.foodapp.domain.model.CustomerProfile): ApiResponse<Unit>
    suspend fun getCustomerProfile(uid: String): ApiResponse<com.example.foodapp.domain.model.CustomerProfile>
    suspend fun updateCustomerProfile(profile: com.example.foodapp.domain.model.CustomerProfile): ApiResponse<Unit>

    suspend fun createRestaurantProfile(profile: com.example.foodapp.domain.model.RestaurantProfile): ApiResponse<Unit>
    suspend fun getRestaurantProfile(uid: String): ApiResponse<com.example.foodapp.domain.model.RestaurantProfile>
    suspend fun updateRestaurantProfile(profile: com.example.foodapp.domain.model.RestaurantProfile): ApiResponse<Unit>

    suspend fun createDriverProfile(profile: com.example.foodapp.domain.model.DriverProfile): ApiResponse<Unit>
    suspend fun getDriverProfile(uid: String): ApiResponse<com.example.foodapp.domain.model.DriverProfile>
    suspend fun updateDriverProfile(profile: com.example.foodapp.domain.model.DriverProfile): ApiResponse<Unit>

    suspend fun createAdminProfile(profile: com.example.foodapp.domain.model.AdminProfile): ApiResponse<Unit>
    suspend fun getAdminProfile(uid: String): ApiResponse<com.example.foodapp.domain.model.AdminProfile>
}
