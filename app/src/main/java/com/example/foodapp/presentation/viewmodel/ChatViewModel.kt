package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.ChatRepository
import com.example.foodapp.domain.model.Message
import com.example.foodapp.presentation.state.ChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var messageJob: Job? = null
    //Alternative hiện đại hơn (khỏi cần Job)

    fun loadConversations(userId: String) {
        viewModelScope.launch {
            chatRepository.getConversationsByUserId(userId)
                .collect { response ->
                    _uiState.value = when (response) {
                        is ApiResponse.Loading -> {
                            _uiState.value.copy(isLoading = true)
                        }

                        is ApiResponse.Success -> {
                            _uiState.value.copy(
                                isLoading = false,
                                conversation = response.data
                            )
                        }

                        is ApiResponse.Error -> {
                            _uiState.value.copy(
                                isLoading = false,
                                error = response.message
                            )
                        }

                        is ApiResponse.Empty -> {
                            _uiState.value.copy(isLoading = false)
                        }
                    }
                }
        }
    }

    fun loadMessages(conversationId: String, userId: String) {
        messageJob?.cancel()

        messageJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val response = chatRepository.getConversationById(conversationId)) {
                is ApiResponse.Success -> {
                    _uiState.value = _uiState.value.copy(
                        currentConversation = response.data
                    )
                }

                else -> {}
            }
            chatRepository.getMessagesByConversationId(conversationId)
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }

                        is ApiResponse.Success -> {
                            _uiState.value.copy(
                                isLoading = false,
                                message = response.data
                            )
                        }

                        is ApiResponse.Error -> {
                            _uiState.value.copy(
                                isLoading = false,
                                error = response.message
                            )
                        }

                        is ApiResponse.Empty -> {
                        }
                    }
                }
        }
        markAllAsRead(conversationId, userId)
    }


    private fun markAllAsRead(conversationId: String, userId: String) {
        viewModelScope.launch {
            chatRepository.markAllMessageAsRead(conversationId, userId)
            loadUnreadCount(conversationId, userId)
        }
    }
    private fun loadUnreadCount(conversationId: String, userId: String) {
        viewModelScope.launch {
            when (val response = chatRepository.getUnreadMessageCount(conversationId, userId)) {
                is ApiResponse.Success -> {
                    _uiState.value = _uiState.value.copy(
                        unreadCount = response.data )
                }
                else -> {}
            }
        }
    }

    //input
    fun updateMessageInput(text: String) {
        _uiState.value = _uiState.value.copy(messageInput = text)
    }

    //send message
    fun sendMessage(conversationId: String, senderId: String) {
        val text = _uiState.value.messageInput.trim()
        if (text.isEmpty()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)
            val message = Message.createTextMessage(conversationId, senderId, text)

            when (val response = chatRepository.sendMessage(message)) {
                is ApiResponse.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        messageInput = "")
                }
                is ApiResponse.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.message
                    )
                }
                else -> {}
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }


}