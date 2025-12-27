//file model category
package com.example.foodapp.data.model.category

 import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Category(
    val id: String = "",
    val name: String = "",
    val iconUrl: String = "",
    val slug: String = "",
    @ServerTimestamp val createdAt: Date? = null,
    val updatedAt: Date? = null
)
