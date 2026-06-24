package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.NotificationRepository
import com.example.foodapp.domain.model.AppNotification
import com.example.foodapp.presentation.state.NotificationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val authRepository: AuthRepository,
): ViewModel() {

    private val _notificationUiState = MutableStateFlow(NotificationUiState())
    val notificationUiState = _notificationUiState.asStateFlow()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {
        viewModelScope.launch {
            _notificationUiState.update { it.copy(notifications = UiState.Loading) }

            val userId = authRepository.currentUserId() ?: return@launch

            notificationRepository.getNotifications(userId).collectLatest { response ->

                when (response) {
                    is ApiResponse.Success -> {
                        Log.d("checkVM_observeNotifications",
                            "data = ${response.data.joinToString(separator = "\n")}}"
                        )
                        _notificationUiState.update {
                            it.copy(
                                notifications = UiState.Success(response.data)
                            )
                        }
                    }
                    is ApiResponse.Error -> {
                        Log.d("checkVM_observeNotifications",
                            "error = ${response.message}"
                        )
                        _notificationUiState.update {
                            it.copy(
                                notifications = UiState.Error(response.message)
                            )
                        }
                    }
                    else -> {
                        Log.d("checkVM_observeNotifications",
                            "else"
                        )
                    }
                }
                Log.d("checkVM_observeNotifications",
                    "state = ${_notificationUiState
                        .value
                        .notifications
                        .getDataOrNull()
                        ?.joinToString(separator = "\n")}}"
                )
//checkVM_setMarkasRead
            }
        }
     }

    fun createNotification(notification: AppNotification) {
        viewModelScope.launch {
            if (_notificationUiState.value.isLoading) return@launch
             _notificationUiState.update { it.copy(isLoading = true) }
            val userId = authRepository.currentUserId() ?: return@launch
            val response = notificationRepository.createNotification(
                notification = notification.copy(userId = userId)
            )
            when (response) {
                is ApiResponse.Success -> {
                    Log.d("checkVM_createNotification", "success")
                }
                is ApiResponse.Error -> {
                    Log.d("checkVM_createNotification", "error ${response.message}")
                }
                else -> {
                    Log.d("checkVM_createNotification", "else")
                }
            }
            _notificationUiState.update { it.copy(isLoading = false) }
        }
    }

    fun setMarkasRead(notificationId: String) {
        Log.d("checkVM_setMarkasRead", "run, id: $notificationId")
        viewModelScope.launch {
            if (_notificationUiState.value.isLoading) return@launch
            _notificationUiState.update { it.copy(isLoading = true) }

            when (val response = notificationRepository.markAsRead(notificationId)) {
                is ApiResponse.Success -> {
                    Log.d("checkVM_setMarkasRead", "success")
                }
                is ApiResponse.Error -> {
                    Log.d("checkVM_setMarkasRead", "error ${response.message}")
                }
                else -> {
                    Log.d("checkVM_setMarkasRead", "else")
                }
            }
            _notificationUiState.update { it.copy(isLoading = false) }
        }
    }

    fun deactivateNotification(notificationId: String) {
        viewModelScope.launch {
            if (_notificationUiState.value.isLoading) return@launch
            _notificationUiState.update { it.copy(isLoading = true) }

            when (val response = notificationRepository.deactivateNotification(notificationId)) {
                is ApiResponse.Success -> {
                    Log.d("checkVM_setDeactivate", "success")
                }
                is ApiResponse.Error -> {
                    Log.d("checkVM_setDeactivate", "error ${response.message}")
                }
                else -> {
                    Log.d("checkVM_setDeactivate", "else")
                }
            }
            _notificationUiState.update { it.copy(isLoading = false) }
        }
    }
}