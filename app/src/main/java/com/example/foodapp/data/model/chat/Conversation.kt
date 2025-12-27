//FILE conversation
package com.example.foodapp.data.model.chat

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Conversation(
    val conversationId: String = "",
    val customerId: String = "",
    val restaurantId: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: Date? = null,
    @ServerTimestamp
    val timestamp: Date? = null
)