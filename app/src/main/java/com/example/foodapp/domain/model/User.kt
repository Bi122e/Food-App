package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val profileUrl: String = "",
    val isGoogleUser: Boolean = false,
    val isActive: Boolean = true,
    val role: UserRole = UserRole.CUSTOMER,
    val fcmToken: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val lastLogin: Date? = null,
    val updatedAt: Date? = null
) {
    fun isValid(): Boolean {
        return  uid.isNotEmpty() && email.isNotEmpty() && isEmailValid()
    }

    private fun isEmailValid(): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }
    fun isProfileComplete(): Boolean {
        return name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()
    }
    fun isPhoneValid(): Boolean {
        return phone.matches(Regex("^0[35789][0-9]{8}$"))
    }
    fun getProfilePercentage(): Int {
        var count = 0
        val total = 5
        if (name.isNotEmpty()) count++
        if (phone.isNotEmpty() && isPhoneValid()) count++
        if (address.isNotEmpty()) count++
        if (profileUrl.isNotEmpty()) count++

        return (count * 100) / total
    }

    fun getMissingFields(): List<String> {
        val missing = mutableListOf<String>()
        if (name.isEmpty()) missing.add("name")
        if (phone.isEmpty() || !isPhoneValid()) missing.add("phone")
        if (address.isEmpty()) missing.add("address")
        return missing
    }
    fun isAdmin(): Boolean = role == UserRole.ADMIN
    fun isRestaurantOwner(): Boolean = role == UserRole.RESTAURANT
    fun updateFcmToken(token: String): User = copy(fcmToken = token, updatedAt = Date())
    fun updateLastLogin(): User = copy(lastLogin = Date(), updatedAt = Date())
    fun deactivate(): User = copy(isActive = false, updatedAt = Date())
    fun activate(): User = copy(isActive = true, updatedAt = Date())


}