package com.example.foodapp.domain.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val conversationId: String = "",
    val isRead: Boolean = false,
    val text: String = "",
//    val senderName: String = "", // ko nen luu vi user co the doi ten, nen senderId roi goi .name luon
    val reactions: Map<String, String> = emptyMap(), //key = userId, value = emoji, biet ai react
    val imgUrl: String = "",
    val type: MessageType = MessageType.TEXT,

    @ServerTimestamp
    val createdAt: Date? = null,
    @ServerTimestamp
    val updatedAt: Date? = null
) {


    fun isValid(): Boolean {
//        return messageId.isNotEmpty() &&
        return conversationId.isNotEmpty() &&
                senderId.isNotEmpty() &&
                when (type) {
                    MessageType.TEXT -> text.isNotEmpty()
                    MessageType.IMAGE -> imgUrl.isNotEmpty()
                    MessageType.SYSTEM -> text.isNotEmpty()
                }
    }

    @Exclude
    fun getConversationPreview(): String {
        return when (type) {
            MessageType.TEXT -> text
            MessageType.IMAGE -> "Hinh anh"
            MessageType.SYSTEM -> text
        }
    }



    @Exclude
    fun isSystemMessage(): Boolean = type == MessageType.SYSTEM
    @Exclude
    fun isTextMessage(): Boolean = type == MessageType.TEXT
    @Exclude
    fun isImgMessage(): Boolean = type == MessageType.IMAGE




}
enum class MessageType {
    TEXT,
    IMAGE,
    SYSTEM,
}