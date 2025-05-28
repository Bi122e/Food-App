package com.example.foodapp.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val isGoogleUser: Boolean = false,
    val profileImageUrl: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val isActive: Boolean = true,
    val lastLogin: String = "",
    @ServerTimestamp
    val createAt: Date? = null
) {
    constructor() : this("", "", "", false, "", "", "", true, "")

    //helper func
    fun getDisplayName(): String {
        return if (name.isNotEmpty()) name else email.substringBefore("@")
    }
    fun isProfileComplete(): Boolean {
        return name.isNotEmpty() && phoneNumber.isNotEmpty() && address.isNotEmpty()
    }
}