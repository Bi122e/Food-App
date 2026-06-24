package com.example.foodapp.presentation.state

  import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.AppNotification

data class NotificationUiState(

    val notifications: UiState<List<AppNotification>> = UiState.Loading,
    val isLoading: Boolean = false,

    )
