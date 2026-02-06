package com.example.foodapp.presentation.viewmodel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.example.foodapp.core.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class CounterViewModel @Inject constructor(): ViewModel() {
    private val _count = MutableStateFlow<UiState<Int>>(UiState.Success(1))
    val count: StateFlow<UiState<Int>> = _count.asStateFlow()

    fun increment() {
        val count = (_count.value as? UiState.Success) ?.data ?: return
        if (count < 5) {
            _count.value = UiState.Success(count+1)
        }
    }

    fun decrement() {
        val count = (_count.value as? UiState.Success)?.data ?: return
        if (count > 1) {
            _count.value = UiState.Success(count -1)
        }
    }
}