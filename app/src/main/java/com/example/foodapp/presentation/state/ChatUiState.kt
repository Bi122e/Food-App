package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Conversation
import com.example.foodapp.domain.model.Message

data class ChatUiState(
    val isLoading: Boolean = false,
    val conversation: List<Conversation> = emptyList(),
    val currentConversation: Conversation? = null,
    val message: List<Message> = emptyList(),

    val messageInput: String = "",
    val isSending: Boolean = false,
    val unreadCount: Int = 0,
    val error: String? = null
) {
}