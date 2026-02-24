package com.example.foodapp.domain

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Review(
    val reviewId: String = "",
    val foodId: String = "",
    val userName: String = "",
    val userId: String = "",
    val userAvatar: String = "",
    val rating: Double = 0.0,
    val comment: String = "",
    @ServerTimestamp
    val createAt: Date? = null
)
