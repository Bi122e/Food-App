package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val conversationId: String,
    val isRead: Boolean = false,
    val text: String = "",
    val imgUrl: String = "",
    val type: MessageType = MessageType.TEXT,
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    enum class MessageType {
        TEXT,
        IMAGE,
        SYSTEM,
    }
    fun isValid(): Boolean {
        return messageId.isNotEmpty() &&
                conversationId.isNotEmpty() &&
                senderId.isNotEmpty() &&
                when(type) {
                    MessageType.TEXT -> text.isNotEmpty()
                    MessageType.IMAGE -> imgUrl.isNotEmpty()
                    MessageType.SYSTEM -> text.isNotEmpty()
                }
    }
    fun getReviewText(): String {
        return when(type) {
            MessageType.TEXT -> text
            MessageType.IMAGE -> "Hinh anh"
            MessageType.SYSTEM -> text
        }
    }

    fun markAsRead(): Message {
        return copy(isRead = true, updatedAt = Date())
    }

    fun isMine(currentSenderId: String): Boolean {
        return senderId == currentSenderId
    }

    fun isSystemMessage(): Boolean = type == MessageType.SYSTEM
    fun isTextMessage(): Boolean = type == MessageType.TEXT
    fun isImgMessage(): Boolean = type == MessageType.IMAGE


    //Factory Message
    companion object {

        fun createTextMessage(
            conversationId: String,
            senderId: String,
            text: String,
        ): Message {
            return Message(
                conversationId =conversationId,
                senderId = senderId,
                text = text,
                type = MessageType.TEXT,
                createdAt = Date()
            )
        }

        fun createSystemMessage(
            conversationId: String,
            text: String,
        ): Message {
            return Message(
                conversationId =conversationId,
                text = text,
                senderId = "system",
                type = MessageType.SYSTEM,
                createdAt = Date()
            )
        }
    }


}