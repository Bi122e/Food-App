package com.example.foodapp.presentation.extentions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.foodapp.core.UiState

@Composable
fun <T> UiStateHandler(
    state: UiState<T>,
    onSuccess: @Composable (T) -> Unit
) {
    when (state) {
        is UiState.Empty -> {
            Text("Empty")
        }
        is UiState.Error -> {
            Text(state.message)
        }
        is UiState.Success -> {
            onSuccess(state.data)
        }
        is UiState.Idle -> {}
        is UiState.Loading -> {
            Text("Loading")
        }
    }
}
