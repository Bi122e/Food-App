package com.example.foodapp.core.utils

import com.example.foodapp.domain.model.Message
 import com.example.foodapp.domain.model.MessageType

fun factorMessage(senderId: String, conversationId: String): Message {
    return Message(
        senderId =senderId,
        conversationId = conversationId,
        type = MessageType.TEXT,
        text = "Xin chào quý khác, đơn hàng của bạn sẽ được giao trong thời gian sớm nhất."
    )
}