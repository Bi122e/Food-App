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
}