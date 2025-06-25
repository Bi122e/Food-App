package com.example.foodapp.data.repository

import android.util.Log
import com.example.foodapp.data.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore

object RestaurantRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun addRestaurant(
        restaurant: Restaurant,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        firestore.collection("restaurant")
            .document(restaurant.restaurantId)
            .set(restaurant)
            .addOnSuccessListener {
                Log.d("FireStore", "Restaurant added succesfully ")
                onSuccess
            }
            .addOnFailureListener { exception ->
                Log.d("FireStore", "Error adding restaurant", exception)
                onError(exception)
            }
    }
}