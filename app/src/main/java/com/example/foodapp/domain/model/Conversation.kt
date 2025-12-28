package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Conversation(
    val userId: String = "",
    val restaurantId: String = "",
    val conversationId: String = "",
    val participants: List<String> = emptyList(),
    @ServerTimestamp
    val lastMessageSenderId: String = "",
    val lastMessage: String = "",
    @ServerTimestamp
    val lastMessageTime: Date? = null,
    val unreadCount: Int = 0,
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
    /*
    useid

    ----

    mesid
    conid
    senderid
    isread
    text
    img
    type
    up
    cr
    * */
) {

    fun isValid(): Boolean {
        return userId.isNotEmpty() &&
                restaurantId.isNotEmpty() &&
                conversationId.isNotEmpty() &&
                participants.size == 2
    }

    fun isParticipant(userId: String): Boolean {
        return participants.contains(userId)
    }

    fun getOtherParticipantId(currentUserId: String): String? {
        return participants.firstOrNull { it != currentUserId }
    }

    fun hasUnreadMessages(): Boolean {
        return unreadCount > 0
    }

    fun updateLastMessage(
        messageText: String,
        senderId: String,
        time: Date = Date()
    ): Conversation {
        return copy(
            lastMessage = messageText,
            lastMessageSenderId = senderId,
            lastMessageTime = time,
            updatedAt = Date()
        )
    }

    fun incrementUnreadCount(): Conversation = copy(
        unreadCount = unreadCount + 1,
    )

    fun resetUnReadCount(): Conversation = copy(
        unreadCount = 0,
    )

    companion object {

        fun create(
            customerId: String,
            restaurantId: String,
        ): Conversation {
            return Conversation(
                userId = customerId,
                restaurantId = restaurantId,
                participants = listOf(customerId, restaurantId),
                createdAt = Date()
            )
        }
    }
}
