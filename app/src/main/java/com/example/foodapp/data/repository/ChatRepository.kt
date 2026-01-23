package com.example.foodapp.data.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.domain.model.Conversation
import com.example.foodapp.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    //conversation
    suspend fun createConversation(conversation: Conversation): ApiResponse<String>

    suspend fun getConversationById(conversationId: String): ApiResponse<Conversation>

    fun getConversationsByUserId(userId: String): Flow<ApiResponse<List<Conversation>>>

    suspend fun updateConversation(conversation: Conversation): ApiResponse<Boolean>

    suspend fun deleteConversation(conversationId: String, currentUserId: String): ApiResponse<Boolean>


    //message
    suspend fun sendMessage(message: Message): ApiResponse<String>

    fun getMessagesByConversationId(conversationId: String): Flow<ApiResponse<List<Message>>>

    suspend fun markMessageAsRead(messageId: String): ApiResponse<Boolean>

    suspend fun markAllMessageAsRead(conversationId: String, userId: String): ApiResponse<Boolean>

    suspend fun deleteMessage(messageId: String): ApiResponse<Boolean>

    //query
    suspend fun getUnreadMessageCount(conversationId: String, userId: String): ApiResponse<Int>

    suspend fun searchMessage(conversationId: String, query: String): ApiResponse<List<Message>>

    fun getLastMessage(conversationId: String): Flow<ApiResponse<Message?>>
}