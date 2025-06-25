package com.example.foodapp.data.repository

import com.example.foodapp.data.model.User
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