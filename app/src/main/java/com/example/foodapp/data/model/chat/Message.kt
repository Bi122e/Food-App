//file model message
package com.example.foodapp.data.model.chat

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
)