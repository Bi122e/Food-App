package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val uid: String = "",
    val email: String = "",
//    val role: UserRole = UserRole.CUSTOMER,
    val isActive: Boolean = true,
    val fcmToken: String = "",
    val isGoogleUser: Boolean = false,
     val name: String = "",
    val phone: String = "",
    val address: String = "",
    val gender: String = "",
    val profileUrl: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val lastLogin: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null
) {
    fun isValid(): Boolean {
        return uid.isNotEmpty() && email.isNotEmpty()
    }


    fun isComplete(): Boolean {
        return name.isNotEmpty() && phone.isNotEmpty()
    }
}