package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = authRepository.login(email, password)
            _uiState.value = response.toUiState()
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logOut()
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = authRepository.register(name, email, password)
            _uiState.value = response.toUiState()
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = authRepository.changePassword(oldPassword, newPassword)
            _uiState.value = response.toUiState()
        }
    }
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = authRepository.loginWithGoogle(idToken)
            _uiState.value = response.toUiState()
        }
    }

}