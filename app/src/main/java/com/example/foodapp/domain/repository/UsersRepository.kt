package com.example.foodapp.domain.repository

import com.example.foodapp.data.model.user.User
import com.google.firebase.firestore.FirebaseFirestore

class UsersRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun createUsers(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        
    }
}