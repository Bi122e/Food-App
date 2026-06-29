package com.example.foodapp.domain.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Conversation(
    val customerId: String = "",
    val driverId: String = "",
    val conversationId: String = "",

    val lastMessageSenderId: String = "", //dùng để hiện message "bạn hay tài xế
    val lastMessage: String = "",
    val avatarUrl: String = "",
    val active: Boolean = true,
    val displayName: String = "",
    val messageType: MessageType = MessageType.TEXT,
     val unreadCount: Int = 0,

    @ServerTimestamp
    val lastMessageTime: Date? = null,

    @ServerTimestamp
    val createdAt: Date? = null,

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



    fun updateLastMessage(
        messageText: String,
        senderId: String,
        time: Date = Date()
    ): Conversation {
        return copy(
            lastMessage = messageText,
            lastMessageSenderId = senderId,
            lastMessageTime = time,
        )
    }

    fun incrementUnreadCount(): Conversation = copy(
        unreadCount = unreadCount + 1,
    )

    fun resetUnReadCount(): Conversation = copy(
        unreadCount = 0,
    )

    fun updateConversation() {

    }


}
