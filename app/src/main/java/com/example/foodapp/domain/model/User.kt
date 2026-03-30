package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val uid: String = "",
    val email: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val isActive: Boolean = true,
    val fcmToken: String = "",
    val isGoogleUser: Boolean = false,
    val profile: UserProfile? = null,
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val lastLogin: Date? = null,
    val updatedAt: Date? = null
) {
    fun isValid(): Boolean {
        return uid.isNotEmpty() && email.isNotEmpty()
    }

    fun isAdmin(): Boolean = role == UserRole.ADMIN
    fun isRestaurantOwner(): Boolean = role == UserRole.RESTAURANT
    fun updateFcmToken(token: String): User = copy(fcmToken = token, updatedAt = Date())
    fun updateLastLogin(): User = copy(lastLogin = Date(), updatedAt = Date())
    fun deactivate(): User = copy(isActive = false, updatedAt = Date())
    fun activate(): User = copy(isActive = true, updatedAt = Date())

    //flow cập nhật để kiểm tra đúng profile tương ứng với role hiện tại.
    fun isComplete(): Boolean {
        if (profile == null) return false
        
        return when (role) {
            UserRole.CUSTOMER -> {
                profile.customer?.let { 
                    it.name.isNotEmpty() && it.phone.isNotEmpty() && it.address.isNotEmpty() 
                } ?: false
            }
            UserRole.DRIVER -> {
                profile.driver?.let {
                    it.name.isNotEmpty() && it.phone.isNotEmpty() && 
                    it.vehicleType.isNotEmpty() && it.licensePlate.isNotEmpty()
                } ?: false
            }
            UserRole.RESTAURANT -> {
                profile.restaurant?.let {
                    it.name.isNotEmpty() && it.phone.isNotEmpty()
                } ?: false
            }
            UserRole.ADMIN -> {
                profile.admin?.let {
                    it.name.isNotEmpty()
                } ?: false
            }
        }
    }
}

data class UserProfile(
    val customer: CustomerProfile? = null,
    val restaurant: RestaurantProfile? = null,
    val driver: DriverProfile? = null,
    val admin: AdminProfile? = null
)

data class AdminProfile(
    val uid: String = "",
    val name: String = "",
    val permission: List<String> = emptyList()
)

data class CustomerProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val profileUrl: String = ""
)

data class RestaurantProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val profileUrl: String = "",
)

data class DriverProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val profileUrl: String = "",
    val vehicleType: String = "",
    val licensePlate: String = "",
    val isAvailable: Boolean = true
)