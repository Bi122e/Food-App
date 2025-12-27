//file model favorite
package com.example.foodapp.data.model.user

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Favorite(
    val itemId: String = "",
     @ServerTimestamp
    val createdAt: Date? = null
) {
 }