package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.ProfileRepository
import com.example.foodapp.domain.model.AdminProfile
import com.example.foodapp.domain.model.CustomerProfile
import com.example.foodapp.domain.model.DriverProfile
import com.example.foodapp.domain.model.RestaurantProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProfileRepository {

    private val customerRef = firestore.collection("customers")
    private val restaurantRef = firestore.collection("restaurants")
    private val driverRef = firestore.collection("drivers")
    private val adminRef = firestore.collection("admins")


    override suspend fun createCustomerProfile(profile: CustomerProfile): ApiResponse<Unit> {
        return try {
            customerRef.document(profile.uid).set(profile).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to create customer profile")
        }
    }

    override suspend fun getCustomerProfile(uid: String): ApiResponse<CustomerProfile> {
        return try {
            val doc = customerRef.document(uid).get().await()
            val profile = doc.toObject(CustomerProfile::class.java)
            if (profile != null) {
                ApiResponse.Success(profile)
            } else {
                ApiResponse.Error("Customer profile not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get customer profile")
        }
    }

    override suspend fun updateCustomerProfile(profile: CustomerProfile): ApiResponse<Unit> {
        return try {
            customerRef.document(profile.uid).set(profile).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update customer profile")
        }
    }

    override suspend fun createRestaurantProfile(profile: RestaurantProfile): ApiResponse<Unit> {
        return try {
            restaurantRef.document(profile.uid).set(profile).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to create restaurant profile")
        }
    }

    override suspend fun getRestaurantProfile(uid: String): ApiResponse<RestaurantProfile> {
        return try {
            val doc = restaurantRef.document(uid).get().await()
            val profile = doc.toObject(RestaurantProfile::class.java)
            if (profile != null) {
                ApiResponse.Success(profile)
            } else {
                ApiResponse.Error("Restaurant profile not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get restaurant profile")
        }
    }

    override suspend fun updateRestaurantProfile(profile: RestaurantProfile): ApiResponse<Unit> {
        return try {
            restaurantRef.document(profile.uid).set(profile).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update restaurant profile")
        }
    }

    override suspend fun createDriverProfile(profile: DriverProfile): ApiResponse<Unit> {
         return try {
            driverRef.document(profile.uid).set(profile).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to create driver profile")
        }
    }

    override suspend fun getDriverProfile(uid: String): ApiResponse<DriverProfile> {
        return try {
            val doc = driverRef.document(uid).get().await()
            val profile = doc.toObject(DriverProfile::class.java)
            if (profile != null) {
                ApiResponse.Success(profile)
            } else {
                ApiResponse.Error("Driver profile not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get driver profile")
        }
    }

    override suspend fun updateDriverProfile(profile: DriverProfile): ApiResponse<Unit> {
        return try {
            driverRef.document(profile.uid).set(profile).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update driver profile")
        }
    }

    override suspend fun createAdminProfile(profile: AdminProfile): ApiResponse<Unit> {
         return try {
            adminRef.document(profile.uid).set(profile).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to create admin profile")
        }
    }

    override suspend fun getAdminProfile(uid: String): ApiResponse<AdminProfile> {
        return try {
            val doc = adminRef.document(uid).get().await()
            val profile = doc.toObject(AdminProfile::class.java)
            if (profile != null) {
                ApiResponse.Success(profile)
            } else {
                ApiResponse.Error("Admin profile not found")
            }
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get admin profile")
        }
    }
}