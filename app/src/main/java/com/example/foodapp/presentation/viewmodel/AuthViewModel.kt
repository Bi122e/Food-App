package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.AuthResult
import com.example.foodapp.core.UiState
import com.example.foodapp.core.flatMap
import com.example.foodapp.core.map
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<AuthResult>>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
//            val response = authRepository.login(email, password)
//            val state = response.toUiState()
//            _uiState.value = state
//            Log.d("AuthViewModel", "Login = $state")

            val finalResponse = authRepository.login(email, password)
                .flatMap { uid ->
                    userRepository.updateLastLoginAndToken(uid).map{uid}
                }
                .flatMap { uid ->
                    userRepository.getUserById(uid)
                }

            _uiState.value = finalResponse.toUiState() {user ->
                AuthResult.LoggedIn(user)
            }

            Log.d("AuthViewModel", "Current UI State: ${_uiState.value}")        }

    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logOut()
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val finalResponse = authRepository.register(email, password)
                .flatMap { uid ->
                    userRepository.createUser(uid, email)
                        .flatMap { uid ->
                            userRepository.getUserById(uid)
                        }
                }
            _uiState.value = finalResponse.toUiState() { data ->
                AuthResult.LoggedIn(data)
            }
            Log.d("AuthViewModel", "Current UI State: ${_uiState.value}")
        }
    }
//    fun register(name: String, email: String, password: String) {
//        viewModelScope.launch {
//            _uiState.emit(UiState.Loading)
//            _uiState.emit(
//                authRepository.register(name, email, password).toUiState()
//            )
//        }
//    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val response = authRepository.changePassword(oldPassword, newPassword)
            _uiState.value = response.toUiState() {
                AuthResult.PasswordChange
            }
        }
    }
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
             val finalResponse = authRepository.loginWithGoogle(idToken)
                 .flatMap { uid ->
                     userRepository.updateLastLoginAndToken(uid).map{uid}
                 }
                .flatMap { uid ->
                    userRepository.getUserById(uid)
                }
            _uiState.value = finalResponse.toUiState() { data ->
                AuthResult.LoggedIn(data)
            }
            Log.d("AuthViewModel", "Current UI State: ${_uiState.value}")        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }

}