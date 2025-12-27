//file messagemapper.kt
package com.example.foodapp.data.mapper

import com.example.foodapp.data.model.chat.Message
import com.example.foodapp.data.model.chat.MessageUI
import java.text.SimpleDateFormat

fun Message.toMessageUI(
    currentId: String = ""
): MessageUI {
    return MessageUI(
        message = messageId,
        isMine = senderId == currentId,
        time = createdAt?.let { SimpleDateFormat("HH::mm").format(it) }.orEmpty()

    )
}