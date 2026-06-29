package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.ChatRepository
import com.example.foodapp.domain.model.Message
import com.example.foodapp.presentation.state.ChatUiState
import com.example.foodapp.presentation.state.toMessageUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()


    init {
        observeConversations()
    }


    fun observeMessages(conversationId: String) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            _chatUiState.update { it.copy(loadingMessages = true) }
            chatRepository.observeMessages(conversationId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _chatUiState.update {
                            it.copy(
                                messages = response.data.toMessageUi(userId)
                            )
                        }
                        resetMessageState()
                    }

                    is ApiResponse.Error -> {
                        _chatUiState.update { it.copy(errorMessage = true) }
                    }

                    else -> {
                        Log.d("checkVM_observeMessages", "ELSE")
                    }
                }
                _chatUiState.update { it.copy(loadingMessages = false) }
                Log.d(
                    "checkVM_observeMessages",
                    "check state: ${_chatUiState.value.messages.size}"
                )
            }

        }
    }

    private fun observeConversations() {

        viewModelScope.launch {
            val uid = authRepository.currentUserId() ?: return@launch
            chatRepository.getConversationsByUserId(uid).collectLatest { response ->
                Log.e("CHAT_VM", "collect ${System.currentTimeMillis()}")

                when (response) {
                    is ApiResponse.Success -> {
                        _chatUiState.update {
                            it.copy(
                                conversations = response.data
                            )
                        }
                        resetConversationState()
                    }

                    is ApiResponse.Error -> {
                        _chatUiState.update {
                            it.copy(
                                errorConversation = true
                            )
                        }
                    }

                    else -> {
                        Log.d("checkVM_observeConversation", "else")
                    }
                }
                resetConversationState()
                Log.d(
                    "checkVM_observeConversation",
                    "state: ${_chatUiState.value.conversations.size}"
                )
            }
        }
    }


    fun updateConversation(conversationId: String) {
        viewModelScope.launch {
            val response = chatRepository.updateConversation(conversationId = conversationId)
            when (response) {
                is ApiResponse.Success -> {
                    _chatUiState.update { it.copy(isDelivered = true) }
                }
                else -> {}
            }
         }
    }
    fun createMessage(conversationId: String) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            _chatUiState.update { it.copy(loadingCreate = true) }
            val message = Message(
                conversationId = conversationId,
                text = _chatUiState.value.text,
                senderId = userId,

            )
            val conversation = _chatUiState.value.conversations.find{ it.conversationId == conversationId} ?: return@launch
           val result = chatRepository.createMessage(message, conversation)
            when (result) {
                is ApiResponse.Success -> {
                    resetCreateState()
                }
                is ApiResponse.Error -> {
                    _chatUiState.update { it.copy(errorCreate = true) }
                }
                else -> {
                    Log.d(
                        "checkVM_createMessage",
                        "else"
                    )
                }
            }
            _chatUiState.update { it.copy(loadingCreate = false) }
        }
    }


    private fun resetMessageState() {
        _chatUiState.update {
            it.copy(
                errorMessage = false,
                loadingMessages = false
            )
        }
    }

    private fun resetConversationState() {
        _chatUiState.update {
            it.copy(
                errorConversation = false,
                loadingConversations = false,
            )
        }
    }

    private fun resetCreateState() {
        _chatUiState.update {
            it.copy(
                errorCreate = false,
                loadingCreate = false,
                text = "",
            )
        }
    }

    fun onTextChanged(text: String) {
        _chatUiState.update { it.copy(text = text) }
    }

    fun resetUnread(conversationId: String) {
        viewModelScope.launch {
            chatRepository.resetUnread(conversationId)
        }
    }

}