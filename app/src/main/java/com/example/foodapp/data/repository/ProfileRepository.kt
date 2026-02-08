package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.AdminProfile
import com.example.foodapp.domain.model.CustomerProfile
import com.example.foodapp.domain.model.DriverProfile
import com.example.foodapp.domain.model.RestaurantProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createCustomerProfile(profile: CustomerProfile): ApiResponse<Unit>
    suspend fun getCustomerProfile(uid: String): ApiResponse<CustomerProfile>
    suspend fun updateCustomerProfile(profile: CustomerProfile): ApiResponse<Unit>

    suspend fun createRestaurantProfile(profile: RestaurantProfile): ApiResponse<Unit>
    suspend fun getRestaurantProfile(uid: String): ApiResponse<RestaurantProfile>
    suspend fun updateRestaurantProfile(profile: RestaurantProfile): ApiResponse<Unit>

    suspend fun createDriverProfile(profile: DriverProfile): ApiResponse<Unit>
    suspend fun getDriverProfile(uid: String): ApiResponse<DriverProfile>
    suspend fun updateDriverProfile(profile: DriverProfile): ApiResponse<Unit>

    suspend fun createAdminProfile(profile: AdminProfile): ApiResponse<Unit>
    suspend fun getAdminProfile(uid: String): ApiResponse<AdminProfile>
}
