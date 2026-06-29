package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Conversation
import com.example.foodapp.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    //conversation
    suspend fun createConversation(conversation: Conversation): ApiResponse<String>

    suspend fun createMessage(message: Message, conversation: Conversation): ApiResponse<Unit>


    fun observeMessages(conversationId: String): Flow<ApiResponse<List<Message>>>

    fun getConversationsByUserId(userId: String): Flow<ApiResponse<List<Conversation>>>


    suspend fun resetUnread(conversationId: String): ApiResponse<Unit>
    suspend fun updateConversation(conversationId: String): ApiResponse<Unit>
}